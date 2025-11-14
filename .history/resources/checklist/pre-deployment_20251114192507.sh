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
   echo "checking if RBAC is enabled..."
    rbac=`kubectl api-versions | grep rbac.authorization.k8s.io`
    if [ "$?" -eq "0" ]; then
        echo "RBAC is enabled"
    else
        echo "RBAC is not enabled. Please enable it before proceeding."
        exit 1
    fi
    }


PSPCheck(){
    echo "checking ISTIO"
    istioNamespace=`kubectl get ns | grep istio-system | awk '{print $1}'`
    if [ "$istioNamespace" == "istio-system" ]; then
        kubectl get po -n istio-system | grep istiod | awk '{print $3}' | grep Running
}