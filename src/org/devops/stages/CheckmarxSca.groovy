package org.devops.stages
import.org.utils.SecretsUtil

class CheckmarxSca implements Serializable{
    def script
    Map config

    SecretsUtil SecretsUtil

    CheckmarxSca(def script, Map config){
        this.script=script
        this.config=config
        this.secretsUtil=new SecretsUtil(script) 
    }

    void run(){
        script.stage("checkmarx-sca-scan"){
            script.container('checkmarx'){
                script.dir('source'){
                    script.withCredentials([
                        script.usernamePassword(credentialsId: config.Checkmarx_Creds , usernameVariable:"CHECKMARX_USR", passwordVariable:"CHECKMARX_PSW")
                    ]){
                        script.sh """
                               echo 'Starting Checkmarx SCA Scan'
                               checkmarx-scan --project-name ${config.projectName} --sca-server-url ${config.Checkmarx_Server_Url} --username ${script.env.CHECKMARX_USR} --password ${script.env.CHECKMARX_PSW} --src .
                        """
                    }
                }
            }
        }
    }
}