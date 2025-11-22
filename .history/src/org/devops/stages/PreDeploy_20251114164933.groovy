package org.devops.stages
import org.utils.SecretsUtil

class PreDeploy implements Serializable{

    def script
    Map config

    SecretsUtil SecretsUtil

    PreDeploy(def script, Map con)
}