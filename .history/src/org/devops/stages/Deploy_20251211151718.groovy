package org.devops.stages
class Deploy implements Serializable{
    def script
    def config

    Deploy(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script.stage("Deploy $confi")
    }
}