#!/bin/bash
set -euo pipefail

# Usage check
if [ $# -ne 1 ]; then
    echo "Usage: $0 <namespace>"
    exit 1
fi

NAMESPACE=$1

# -------------------------------
# Check Namespace
# -------------------------------
check_namespace() {
    echo "Checking namespace: $NAMESPACE"
    if ! kubectl get namespace "$NAMESPACE" >/dev/null 2>&1; then
        echo "ERROR: Namespace '$NAMESPACE' does not exist."
        exit 1
    fi
    echo "✔ Namespace exists."
}

# -------------------------------
# Check RBAC
# -------------------------------
check_rbac() {
    echo "Checking RBAC..."
    if kubectl api-versions | grep -q "rbac.authorization.k8s.io"; then
        echo "✔ RBAC is enabled."
    else
        echo "ERROR: RBAC is not enabled."
        exit 1
    fi
}

# -------------------------------
# Check Istio
# -------------------------------
check_istio() {
    echo "Checking Istio..."

    if ! kubectl get ns istio-system >/dev/null 2>&1; then
        echo "ERROR: Istio namespace not found."
        exit 1
    fi

    if kubectl get pods -n istio-system | grep -q "istiod"; then
        echo "✔ Istio is installed."
    else
        echo "ERROR: Istiod pod not found — Istio not fully installed."
        exit 1
    fi
}

# -------------------------------
# Run Checks
# -------------------------------
echo "========================================"
echo " Kubernetes Pre-Deployment Validation"
echo "========================================"

check_namespace
check_rbac
check_istio

echo "========================================"
echo "All checks passed. Safe to deploy."
echo "========================================"
