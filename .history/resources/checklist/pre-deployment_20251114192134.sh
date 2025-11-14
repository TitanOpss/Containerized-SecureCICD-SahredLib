#!/bin/bash
set -euo pipefail
NAMESPACE=$1

namespaceCheck(){
    echo "checking if namespace $NAMESPACE exists..."
    ns=`kubectl get namespace $NAMESPACE`
    if [ "$?" -eq "0" ]; then

        echo "namespace $NAMESPACE exists"
    else
        echo "namespace $NAMESPACE does not exist. Please create it before proceeding."
        exit 1
    fi
}

rbacCheck(){
    echo "checking if service account has necessary permissions..."
    kubectl auth can-i create deployments --namespace $NAMESPACE
    if [ "$?" -eq "0" ]; then
        echo "service account has necessary permissions"
    else
        echo "service account does not have necessary permissions. Please ensure it has the required RBAC roles."
        exit 1
    fi
}