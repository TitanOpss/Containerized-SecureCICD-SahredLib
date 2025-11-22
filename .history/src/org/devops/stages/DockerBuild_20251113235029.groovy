package org.devops.stages

class DockerBuild implements Serializable{
    def script
    Map config

    DockerBuild(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script
    }
}