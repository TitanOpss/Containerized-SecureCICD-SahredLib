package org.devops.stages
class Deploy implements Serializable{
    def script
    def config

    Deploy(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script.stage("Deploy $config.deployType"){
            script.container('kubectl'){
                script.dir('files'){
                       if(config.deployType == "kubeFiles"){
                       
                       script.sh "echo deploying to ${config.deployType} on ${config.env} environment"
                       
                        script.sh"""
                                    
                                    sed -i -e 's#IMAGE_NAME#${config.artifactoryUrl}/${config.repoName}/${config.env}:${config.ServiceName}:${config.imageTag}#g' application/${config.serviceName}/deployment.yaml
                                    sed -i -e 's#IMAGE
                        """
                       }
                }
            }
        }
    }
}