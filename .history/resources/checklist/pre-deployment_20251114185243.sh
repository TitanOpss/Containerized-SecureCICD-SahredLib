#!/bin/bash
set -euo pipefail
NAMESPACE=$1

namespaceCheck(){
    echo "checking if namespace $NAMESPACE exists..."
    ns=`kubectl get namespace $
}