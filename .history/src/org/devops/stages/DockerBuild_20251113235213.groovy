package org.devops.stages

class DockerBuild implements Serializable{
    def script
    Map config

    DockerBuild(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script.stage("docker-build"){
            stage.container('docker'){
                script.dir('source'){
                    script.withCredentials([
                        script.usernamePassword(credentialsId: config.Artifactoy)
                    ])
                }
            }
        }
    }
}