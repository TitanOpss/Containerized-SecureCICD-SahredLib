import org.devops.stages
import org.utils.SecretsUtil

class CheckmarxSast implements Serializable{
    def script
    Map config

    SecretsUtil SecretsUtil

    CheckmarxSast(def script, Map config){
        this.script=script
        this.config=config
        this.secretsUtil=new SecretsUtil(script) 
    }