import org.devops.stages
import org.utils.SecretsUtil

class CheckmarxSast implements Serializable{
    def script
    Map config

    SecretsUtil SecretsUtil

    CheckmarxSast(def script, Map config){
        this.script=script
        this.config=config
        this.secretsUtil=new SecretsUtil(script) 
    }

    void run(){
        script.stage("checkmarx-sast-scan"){
            script.container('checkmarx'){
                script.dir('source'){
                    script.withCredentials([
                        script.usernamePassword(credentialsId: config.Checkmarx_Creds , usernameVariable:"CHECKMARX_USR", passwordVariable:"CHECKMARX_PSW")
                    ]){
                        script.sh """
                               echo 'Starting Checkmarx SAST Scan'
                               checkmarx-scan --project-name ${config.ServiceName} --sast-server-url ${config.Checkmarx_Server_Url} --username ${script.env.CHECKMARX_USR} --password ${script.env.CHECKMARX_PSW} --src .
                        """
                    }
                }
            }
        }
    }
