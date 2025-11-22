package org.devops.stages

class ImagePush implements Serializable{
    def script
    Map config

    ImagePush(def script, Map  config)
}