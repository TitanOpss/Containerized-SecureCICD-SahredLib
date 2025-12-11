package org.utils

/**
 * TagGenerator creates unique tags for builds based on timestamp and build number.
 */
class TagGenerator implements Serializable {

    def script

    /**
     * Constructor for TagGenerator.
     * @param script The Jenkins pipeline script context.
     */
    TagGenerator(script) {
        this.script = script
    }

    /**
     * Generates a unique tag using current timestamp and build number.
     * @return A string tag in the format YYYYMMDD-HHMMSS-BUILDNUMBER.
     */
    String generateTag() {
        def timestamp = script.sh(script: "date +%Y%m%d-%H%M%S", returnStdout: true).trim()
        def buildNumber = script.env.BUILD_NUMBER ?: "0"
        return "${timestamp}-${buildNumber}"
    }
}