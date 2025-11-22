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
      script.podTemplate(label: label, yaml: script.libraryResource('pod_agent_demo_template.taml')){
        script.node(label){
            new Checkout(script, config).run()  //source code checkout
            new Checkout(script, config).run2() //kubernetes deployment yaml checkout
            new CheckmarxSast(script, config).run() //checkmarx sast scan
            new CheckmarxSca(script, config).run() //checkmarx sca scan
            new DockerBuild(script, config).run() //docker build
            new TwistlockScan(script, config).run() //twistlock image scan
            new ImagePush(script, config).run() //image push to registry
            new PreDeploy(script, config).run() //pre-deployment steps
            new Post
               
               
                    }
      }
    }
}