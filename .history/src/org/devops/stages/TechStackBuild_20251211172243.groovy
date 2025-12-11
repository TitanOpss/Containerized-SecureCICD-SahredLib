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

        script.stage("building $config.bu")
    }

}