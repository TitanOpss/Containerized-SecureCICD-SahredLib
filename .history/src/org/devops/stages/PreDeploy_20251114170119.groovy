package org.devops.stages
import org.utils.SecretsUtil

class PreDeploy implements Serializable{

    def script
    Map config

    SecretsUtil SecretsUtil

    PreDeploy(def script, Map config){
        this.scipt=script
        this.config=config
        this.secretsUtil=new SecretsUtil(script) 
    }



    void run(){
        script.stage("pre-deployment-steps"){
            script.container('kubectl'){
                def check
            }            
        }
    }
}