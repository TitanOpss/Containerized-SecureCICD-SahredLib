package org.devops.stages
import.org.utils.TagGenerator

class DockerBuild implements Serializable{
    def script
    Map config
    TagGenerator tagGenerator
    def generatedTag

    DockerBuild(def script, Map config){
        this.script=script
        this.config=config
        this.tagGenerator = new TagGenerator(script)
    }

    void run(){
        def generatedTag = tag
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