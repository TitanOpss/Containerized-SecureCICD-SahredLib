package org.devops
import org.devops.stages.*
import org.utils.SecretsUtil

class PipelineRunner implements Serializable {
    def script
    Map config

    PipelineRunner(def script, Map config) {
        this.script = script
        this.config = config
    }

    def execute() {
      def label = "dyamic-agent-${UUID.randomUUID().toString()}"
      script.
    }
}