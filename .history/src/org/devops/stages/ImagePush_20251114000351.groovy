package org.devops.stages

class ImagePush implements Serializable{
    def script
    Map config

    ImagePush(def script, Map  config){
        this.script=script
        this.config=config
    }


    void run(){
        script.stage("docker image-push"){
            script.container('docker'){
                script.dir('source'){
                    script.withCredentials([
                        script.usernamePassword(credentialsId: config.Artifactory_CREDS, usernameVariable:"Artifactory_CREDS_USR", passwordVariable:"Artifactory_CREDS_PSW")
                    ]){
                        script.sh """
                              echo 'Pushing Docker Image for ${config.env}'
                              docker -l debug login ${config.artifactoryUrl} -u ${script.env.Artifactory_CREDS_USR} -p ${script.env.Artifactory_CREDS_PSW}
                              docker -l push ${config.artifactoryUrl}/${config.repoName}/${config.env}:${config.ServiceName}:${config.imageTag}
                        """
                    }
                    ])
                }
            }
        }
    }
}