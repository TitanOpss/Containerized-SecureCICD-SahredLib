#!/bin/bash
# ============================================================
# Kubernetes Post-Deployment Validation Script
# Author: Mayur
# Description:
#   This script performs post-deployment checks for a given
#   application in Kubernetes. It validates:
#     1. Pod running status and restarts
#     2. Pod Security Policy (PSP) usage
#     3. Istio sidecar attachment
# ============================================================

set -euo pipefail

# -------------------------------
# Input Parameters
# -------------------------------
deploymentName=$1
namespace=$2
timeInterval=${3:-30}  # Default to 30 seconds if not provided

# -------------------------------
# Function: Check Pod Running Status
# -------------------------------
podRunningStatus() {
    cout=$(kubectl get deploy "$deploymentName" -n "$namespace" -o jsonpath="{.status.readyReplicas}")
    echo "Number of replicas of application = $cout"

    i=1
    sleep 100
    while [ $i -le $cout ]; do
        podName=$(kubectl get po --sort-by=.metadata.creationTimestamp -n "$namespace" \
                  | grep "$deploymentName" | tail -$cout | head -$i | tail -1 | awk '{print $1}')
        echo "$i pod name is $podName"

        sleep $timeInterval
        kubectl get events -n "$namespace" --field-selector involvedObject.name="$podName"

        podStatus=$(kubectl get po "$podName" -n "$namespace" -o | grep "$podName" | cut -d ' ' -f9)
        if [ "$podStatus" == "Running" ]; then
            echo "Pod $podName status is $podStatus"
            echo "Checking if pods are running without restarts..."

            restartNumberCheck1=$(kubectl get po "$podName" -n "$namespace" | grep "$podName" | cut -d ' ' -f12)
            sleep 30
            restartNumberCheck2=$(kubectl get po "$podName" -n "$namespace" | grep "$podName" | cut -d ' ' -f12)

            if [ $restartNumberCheck1 -ne $restartNumberCheck2 ]; then
                echo "Pod $podName is restarting. Please check the pod logs for more details."
                kubectl logs "$podName" -n "$namespace" --tail=50
                exit 1
                break
            else
                echo "Pod $podName is running without restarts."
            fi
        else
            echo "Pod $podName status is $podStatus. Please check the pod logs for more details."
            kubectl logs "$podName" -n "$namespace" --tail=50
            exit 1
        fi
        i=$((i+1))
    done
}

# -------------------------------
# Function: Check Pod Security Policy
# -------------------------------
PSPCheck() {
    echo "Checking PSP..."
    PSP=$(kubectl -n "$namespace" get pod "$podName" -o jsonpath="{.metadata.annotations['kubernetes\.io/psp']}")

    if [ "$?" -eq 0 ]; then
        echo "Pod $podName is using PSP: $PSP"
    else
        echo "Failed to get PSP for pod $podName"
        exit 1
    fi
}

# -------------------------------
# Function: Check Istio Sidecar
# -------------------------------
IstioCheck() {
    echo "Checking Istio..."

    count=$(kubectl get deploy "$deploymentName" -n "$namespace" -o jsonpath="{.spec.replicas}")
    echo "Number of replicas of application = $count"

    i=1
    while [ $i -le $count ]; do
        podName=$(kubectl get po -n "$namespace" | grep "$deploymentName" | awk NR==${i}{'print $1'})
        echo "Pod name is $podName"

        podLabels=$(kubectl get po "$podName" -n "$namespace" --show-labels | grep "$podName" | awk '{print $6}')
        name=$(kubectl get po -l "$podLabels" -n "$namespace" | grep "$podName" | awk '{print $1}')

        if [ "$name" == "$podName" ]; then
            echo "Pods are attached with Istio."
        else
            echo "Pods are not attached with Istio."
            exit 1
        fi
        i=$((i+1))
    done
}

# ============================================================
# Execution Flow
# ============================================================
echo "==========================================================="
echo "Starting Kubernetes Post-Deployment Checks"
echo "==========================================================="

podRunningStatus
PSPCheck
IstioCheck

echo "==========================================================="
echo "All Post-Deployment Checks Completed Successfully!"
echo "==========================================================="
