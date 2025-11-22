package org.devops.stages

class DockerBuild implements Serializable{
    def script
    Map config

    DockerBuild(def script)
}