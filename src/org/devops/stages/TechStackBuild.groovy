package org.devops.stages
import org.utils.SecretsUtil

/**
 * TechStackBuild stage handles building the application based on the specified build type (e.g., Maven, NPM, Gradle).
 * It validates the configuration, sets up secrets, and executes the appropriate build commands in a container.
 */
class TechStackBuild implements Serializable {
    def script
    def config
    SecretsUtil secretsUtil

    /**
     * Constructor for TechStackBuild.
     * @param script The Jenkins pipeline script context.
     * @param config The configuration map containing build parameters.
     */
    TechStackBuild(script, config) {
        this.script = script
        this.config = config
        this.secretsUtil = new SecretsUtil(script)
    }

    /**
     * Validates the configuration to ensure required parameters are present.
     */
    private void validateConfig() {
        if (!config.buildType) {
            throw new IllegalArgumentException("Build type is required in TechStackBuild stage")
        }
    }


    /**
     * Executes the build process based on the build type.
     */
    void run() {
        validateConfig()

        script.stage("building $config.buildType") {
            try {
                script.container("$config.buildType") {
                    script.dir('source') {
                        if (config.buildType == 'maven') {
                            // Copy Maven settings from secrets
                            def settingXmlPath = secretsUtil.copySecretFileToWorkspace(config.settingXml, "settings.xml")
                            script.sh """
                                echo "building maven................."
                                mkdir -p ~/.m2
                                cp ${settingXmlPath} ~/.m2/settings.xml
                                mvn clean install -U -DskipTests -P${config.env}
                            """
                        } else if (config.buildType == 'npm') {
                            // Copy NPM config from secrets
                            def npmrcPath = secretsUtil.copySecretFileToWorkspace(config.npmrcFile, ".npmrc")
                            script.sh """
                                echo "building npm................."
                                cp ${npmrcPath} /home/jenkins/.npmrc
                                npm --version
                                npm i --verbose
                                npm run build -- --env=${config.env}
                            """
                        } else if (config.buildType == 'gradle') {
                            // Copy Gradle properties from secrets
                            def gradlePropsPath = secretsUtil.copySecretFileToWorkspace(config.gradlePropsFile, "gradle.properties")
                            script.sh """
                                echo "building gradle................."
                                mkdir -p ~/.gradle
                                cp ${gradlePropsPath} ~/.gradle/gradle.properties
                                gradle clean build -x test -Penv=${config.env}
                            """
                        } else {
                            script.sh "echo 'no external build is required.............'"
                        }
                    }
                }
            } catch (Exception e) {
                script.error "Build failed for ${config.buildType}: ${e.getMessage()}"
            }
        }
    }

}