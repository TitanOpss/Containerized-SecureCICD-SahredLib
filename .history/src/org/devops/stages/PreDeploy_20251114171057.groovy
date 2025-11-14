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
                def checklistScript = script.libraryResource("checklist/pre-deployment.sh")
                def checklistFolder = "${script.env.WORKSPACE}/checklist"
                script.sh "mkdir -p ${checklistFolder}"

                def checklistFilePath = "${checklistFolder}/pre-deployment.sh"
                script.writeFile file: checklistFilePath, text: checklistScript

                def kubeconfigPath = secretsUtil.copySecretFileToWorkspace( config.kubeconfigFile, "kubeconfig.yaml" )

                script.echo "runnig pre-deploy task"
                script.sh "ls -lrta ${script.env.WORKSPACE}/checklist"
                script.sh "chmod 775 -R ${c}" 


            }            
        }
    }
}