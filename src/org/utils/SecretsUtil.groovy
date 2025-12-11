package org.utils

/**
 * SecretsUtil provides utility methods for handling Jenkins credentials securely.
 */
class SecretsUtil implements Serializable {

    def script

    /**
     * Constructor for SecretsUtil.
     * @param script The Jenkins pipeline script context.
     */
    SecretsUtil(script) {
        this.script = script
    }

    /**
     * Retrieves a secret text credential and passes it to the provided closure.
     * @param credentialsId The ID of the Jenkins credential.
     * @param body A closure that receives the secret value.
     */
    def getSecretText(String credentialsId, Closure body) {
        script.withCredentials([script.string(credentialsId: credentialsId, variable: 'SECRET')]) {
            body(script.env.SECRET)
        }
    }

    /**
     * Copies a secret file credential to the workspace and returns the path.
     * @param credentialsId The ID of the Jenkins file credential.
     * @param targetFileName The name for the copied file in the workspace.
     * @return The path to the copied file.
     */
    def copySecretFileToWorkspace(String credentialsId, String targetFileName) {
        String filePath = ''
        script.withCredentials([
            script.file(credentialsId: credentialsId, variable: 'SECRET_FILE')
        ]) {
            filePath = script.env.SECRET_FILE
            script.sh "mkdir -p ${script.env.WORKSPACE}/secrets"
            script.sh "cp ${filePath} ${script.env.WORKSPACE}/secrets/${targetFileName}"
        }
        return "${script.env.WORKSPACE}/secrets/${targetFileName}"
    }

}