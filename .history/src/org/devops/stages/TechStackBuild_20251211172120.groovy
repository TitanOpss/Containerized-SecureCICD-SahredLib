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

    private void validat

}