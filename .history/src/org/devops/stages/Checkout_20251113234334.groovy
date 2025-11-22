package org.devops.stages


class Checkout implements Serializable {

    def script
    Map config

       Checkout(def script, Map config) {

        this.script=script
        this.config=config
       }

       void run(){
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
                                                                                            //
       void run2(){ 
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