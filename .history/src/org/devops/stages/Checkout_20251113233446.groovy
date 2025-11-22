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
                script.echo "Repo: ${config.repo}"
            }
        }
       }
 
}