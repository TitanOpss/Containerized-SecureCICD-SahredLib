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
                            script.sh "mvn clean install -DskipTests"
                        } else if (config.buildType == 'gradle'){
                            script.sh "./gradlew build -x test"
                        } else if (config.buildType == 'npm'){
                            script.sh "npm install"
                            script.sh "npm run build"
                        } else {
                            throw new IllegalArgumentException("Unsupported build type: $config.buildType")
                        }
                    }
                }
            }
        }")
    }

}