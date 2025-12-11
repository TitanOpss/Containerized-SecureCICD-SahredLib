package org.utils

class TagGenerator implements Serializable {

    def script

    TagGenerator(script){
        this.script=script
    }

    String generateTag(){
        def timestamp = script.sh(script: "date +%Y%m%d-%H%M%S", returnStdout: true).trim()
        def buildNumber = script.env.BUILD_NUMBER ?: "0"
        return "${timestamp}-${buildNumber}"
    }
}