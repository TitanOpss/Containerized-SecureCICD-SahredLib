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

rbac