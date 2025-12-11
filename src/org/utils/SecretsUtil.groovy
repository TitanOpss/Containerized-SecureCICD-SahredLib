package org.utils

class SecretsUtil implements Serializable {

    def script

    SecretsUtil(script){
        this.script=script
    }

    def getSecretText(String credentialsId, clouser body){
        script.withCredentials([script.string(credentialsId; credentialsId, variable: 'SECRET')]){
            body(script.env.SECRET)
        }
    }

    def copySecretFileToWorkspace(String credentialsId, String targetFileName){
        String filePath = ''
        script.withCredentials([
            script.file(credentialsId: credentialId, variable: 'SECRET_FILE')
        ]){
            filePath = script.env.SECRET_FILE
            script.sh "mkdir -p ${script.env.WORKSPACE}/secrets"
            script.sh "cp ${filePath} ${script.env.WORKSPACE}/secrets/${targetFileName}"
        }
        return "${script.env.WORKSPACE}/secrets/${targetFileName}"
        
            }

}