package org.devops.stages
import.org.utils.SecretsUtil


class TechkStackBuild implements Serializable{
    def script
    def config
    SecretsUtil SecretsUtil
    TechkStackBuild(script, config){
        this.script=script
        this.config=config
        this.secretsUtil=new SecretsUtil(script) 
    }

    private void validateConfig(){
        if (!config.buildType){
            throw new IllegalArgumentException("Build type is required in TechStackBuild stage")
        }
    }


    void run(){
        validateConfig()

        script.stage("building $config.buildType"){
            try{
                script.container("$config.buildType"){
                    scrip.dir('source'){
                        if(config.buildType == 'maven'){
                            
                            def settingXmlPath = secretsUtil.copySecretFileToWorkspace(config.settingXml, "settings.xml")
                            scrip.sh """
                                echo "building maven..............."
                                mkdir -p ~/.m2
                                cp ${settingXmlPath} ~/.m2/settings.xml
                                mvn clean install -U -DskipTests -p${config.env}

                            
                            """
                        }

                        else if(config.buildType == 'npm'){
                            def npmrcPath = secretsUtil.copySecretFileToWorkspace(config.npmrcFile, ".npmrc")
                            scrip.sh """
                                echo "building npm..............."
                                cp ${npmrcPath} /home/jenkins/.npmrc
                                npm --version
                                npm i --verbose
                                npm run build -- --env=${config.env}
                            """
                        }
                        else if (config.buildType == 'gradle'){
                            def gradlePropsPath = secretsUtil.copySecretFileToWorkspace(config.gradlePropsFile, "gradle.properties")
                            scrip.sh """
                                echo "building gradle..............."
                                mkdir -p ~/.gradle
                                cp ${gradlePropsPath} ~/.gradle/gradle.properties
                                gradle clean build -x test -Penv=${config.env}
                            """
                        } else {
                            script.sh ""
                        }
                    }
                }
            }
        }

    }

}