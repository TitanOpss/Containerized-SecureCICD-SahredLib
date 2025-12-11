package org.devops.stages

/**
 * Checkout stage handles checking out source code and deployment files from Git repositories.
 */
class Checkout implements Serializable {

    def script
    Map config

    /**
     * Constructor for Checkout.
     * @param script The Jenkins pipeline script context.
     * @param config The configuration map containing repository details.
     */
    Checkout(def script, Map config) {
        this.script = script
        this.config = config
    }

    /**
     * Checks out the source code repository.
     */
    void run() {
        script.stage("checkout-source-code"){
            script.dir('source'){
                script.echo "Branch: ${config.branch ?: 'master'}"
                script.echo "Repo: ${config.repoUrl}"
                script.echo "CredentialsId: ${config.repoCredsId}"
                script.git branch : config.branch ?: 'master',
                           url    : config.repoUrl,
                           credentialsId: config.repoCredsId
            }
        }
       }

       // Assuming both repos belong to the same Bitbucket project, hence same credentials ID
       /**
        * Checks out the deployment files repository.
        */
       void run2() {
        script.stage("checkout-deployFiles"){
            script.dir('files'){
                script.echo "File Branch: ${config.fileBranch ?: 'master'}"
                script.echo "File Repo URL: ${config.fileRepoUrl}"
                script.echo "Files CredentialsId: ${config.repoCredsId}"
                script.git branch : config.fileBranch ?: 'master',
                           url    : config.fileRepoUrl,
                           credentialsId: config.fileRepoCredsId
            }
        }
       }
 
}