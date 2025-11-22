#!/bin/bash
# ============================================================
# Kubernetes Pre-Deployment Validation Script
# Author: Mayur
# Description:
#   This script performs essential checks before deploying
#   workloads into a Kubernetes cluster. It validates:
#     1. Namespace existence
#     2. RBAC availability
#     3. Pod Security Policy (PSP) availability
#     4. Istio installation
# ============================================================

set -euo pipefail

# -------------------------------
# Input: Namespace to validate
# -------------------------------
NAMESPACE=$1

# -------------------------------
# Function: Check Namespace
# -------------------------------
namespaceCheck() {
    echo "Checking if namespace '$NAMESPACE' exists..."
    ns=$(kubectl get namespace "$NAMESPACE" 2>/dev/null)
    if [ "$?" -eq 0 ]; then
        echo "Namespace '$NAMESPACE' exists."
    else
        echo "Namespace '$NAMESPACE' does not exist. Please create it before proceeding."
        exit 1
    fi
}

# -------------------------------
# Function: Check RBAC
# -------------------------------
rbacCheck() {
    echo "Checking if RBAC is enabled..."
    rbac=$(kubectl api-versions | grep rbac.authorization.k8s.io)
    if [ "$?" -eq 0 ]; then
        echo "RBAC is enabled."
    else
        echo "RBAC is not enabled. Please enable it before proceeding."
        exit 1
    fi
}

# -------------------------------
# Function: Check Pod Security Policy
# -------------------------------
PSPCheck() {
    echo "Checking Pod Security Policy (PSP)..."
    PSP=$(kubectl get psp 2>/dev/null)
    if [ "$?" -eq 0 ]; then
        echo "PSP is enabled."
    else
        echo "PSP is not enabled. Please enable it before proceeding."
        exit 1
    fi
}

# -------------------------------
# Function: Check Istio Installation
# -------------------------------
IstioCheck() {
    echo "Checking Istio installation..."
    istioNamespace=$(kubectl get ns | grep istio-system | awk '{print $1}')
    if [ "$istioNamespace" == "istio-system" ]; then
        kubectl get pods -n istio-system | grep istiod | awk '{print $1}' >/dev/null
        if [ "$?" -eq 0 ]; then
            echo "Istio is installed."
        else
            echo "Istio is not installed. Please install it before proceeding."
            exit 1
        fi
    else
        echo "Istio namespace not found. Please install Istio before proceeding."
        exit 1
    fi
}

# ============================================================
# Execution Flow
# ============================================================
echo "==========================================================="
echo "Starting Kubernetes Pre-Deployment Checks"
echo "==========================================================="

namespaceCheck
rbacCheck
PSPCheck
IstioCheck

echo "==========================================================="
echo "All Pre-Deployment Checks Completed Successfully!"
echo "==========================================================="
