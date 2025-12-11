package org.devops.stages
import.org.

class DockerBuild implements Serializable{
    def script
    Map config
    T

    DockerBuild(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script.stage("docker-build"){
            stage.container('docker'){
                script.dir('source'){
                    script.withCredentials([
                        script.usernamePassword(credentialsId: config.Artifactory_CREDS, usernameVariable:"Artifactory_CREDS_USR", passwordVariable:"Artifactory_CREDS_PSW")
                    ]){
                        script.sh """
                              echo 'Building Docker Image for ${config.env}'
                              docker -l debug login ${config.artifactoryUrl} -u ${script.env.Artifactory_CREDS_USR} -p ${script.env.Artifactory_CREDS_PSW}
                              docker -l build --no-cache -t ${config.artifactoryUrl}/${config.repoName}/${config.env}:${config.ServiceName}:${config.imageTag} .
                        """
                    }
                }
            }
        }
    }
}