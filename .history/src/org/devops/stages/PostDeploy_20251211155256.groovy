package org.devops.stages
import org.utils.SecretUtil

class PostDeploy implements Serializable {

    def script
    def config

    PostDeploy(script, config){
        this.script=script
        this.config=config
        this.secretsUtil = new SecretUtil(script)
    
    }

    void run () {
        script.stage('Post Deploy'){
            script.container('kubectl'){
                script.sh"echo 'Running postdeploychecks'"
                def checklistsScript = script.libraryResource("checklist/post-deployment.sh")

                def checklistFolder =  "${script.env.WORKSPACE}/checklist"
                script.sh "mkdir -p ${checklistFolder}"

                def checklistFilePath = "${checklistFolder}/post-deployment.sh"
                script.writeFile file: checklistFilePath, text: checklistsScript

                def kubeconfigPath = secretsUtil.copySecretFileToWorkspace( config.kubeconfigFile, "kubeconfig.yaml" )

                script.sh "echo 'runnig post deployment checklist script'"
                script.sh "ls -lrta ${script.env.WORKSPACE}/checklist"
                script.sh "chmod 775 -R ${checklistFolder}"
                script.sh "mkdir -p /home/jenkins/.kube"
                script.sh "echo 'script file path: ${kubeconfigPath}'"

                if (script.fileExists(kubeconfigPath)) {
                    script.sh "cp ${kubeconfigPath} /home/jenkins/.kube/config; grep cluster /home/jenkins/.kube/config"
                } else {
                    script.error "Kubeconfig file not found at path: ${kubeconfigPath}"
                }

                def postd = script.sh (script: "checklist/post-deployment.sh ${config.deploymentname} ${config.namespace}", returnStatus: true)
                if (postd != 0) {
                    script.error "Post Deployment checks failed!"
           
            }
            else
            {
                script.echo "Post Deployment checks passed!"
            }
            }
        }
    }


}