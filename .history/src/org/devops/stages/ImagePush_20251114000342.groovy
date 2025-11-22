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
                        script.usernamePassword(credentialsId: c)
                    ])
                }
            }
        }
    }
}