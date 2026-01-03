#!/bin/bash

set -euo pipefail

deploymentName=$1
namespace=$2
timeInterval=${3:-30}

# -------------------------------
# Check pod running + restarts
# -------------------------------
podRunningStatus() {

    replicas=$(kubectl get deploy "$deploymentName" -n "$namespace" -o jsonpath='{.status.readyReplicas}')
    echo "Ready replicas = $replicas"

    pods=$(kubectl get po -n "$namespace" -l app="$deploymentName" -o jsonpath='{.items[*].metadata.name}')

    for podName in $pods; do
        echo "Checking pod: $podName"
        sleep $timeInterval

        podStatus=$(kubectl get po "$podName" -n "$namespace" -o jsonpath='{.status.phase}')

        if [[ "$podStatus" != "Running" ]]; then
            echo "Pod $podName not running. Status: $podStatus"
            kubectl logs "$podName" -n "$namespace" --tail=50
            exit 1
        fi

        # Restart check
        r1=$(kubectl get po "$podName" -n "$namespace" -o jsonpath='{.status.containerStatuses[0].restartCount}')
        sleep 10
        r2=$(kubectl get po "$podName" -n "$namespace" -o jsonpath='{.status.containerStatuses[0].restartCount}')

        if [[ $r1 -ne $r2 ]]; then
            echo " Pod $podName is restarting."
            kubectl logs "$podName" -n "$namespace" --tail=50
            exit 1
        fi

        echo "✔ Pod $podName healthy with no restarts."
    done
}

# -------------------------------
# PSP Check
# -------------------------------
PSPCheck() {
    podName=$1
    psp=$(kubectl get pod "$podName" -n "$namespace" -o jsonpath="{.metadata.annotations['kubernetes\.io/psp']}")
    echo "PSP used by $podName = $psp"
}

# -------------------------------
# Istio Sidecar Check
# -------------------------------
IstioCheck() {

    pods=$(kubectl get po -n "$namespace" -l app="$deploymentName" -o jsonpath='{.items[*].metadata.name}')

    for podName in $pods; do
        echo "Checking Istio sidecar for $podName..."

        istioAttached=$(kubectl get po "$podName" -n "$namespace" \
            -o jsonpath='{.spec.containers[*].name}' | grep -c 'istio-proxy')

        if [[ $istioAttached -ge 1 ]]; then
            echo "✔ Istio sidecar present."
        else
            echo " Istio sidecar NOT attached."
            exit 1
        fi
    done
}

# -------------------------------
# Execution Flow
# -------------------------------
echo "========= Starting Post Deployment Checks ========="

podRunningStatus

for podName in $(kubectl get po -n "$namespace" -l app="$deploymentName" -o jsonpath='{.items[*].metadata.name}'); do
    PSPCheck "$podName"
done

IstioCheck

echo "========= All checks passed successfully! ========="
