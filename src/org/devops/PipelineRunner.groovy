package org.devops
import org.devops.stages.*
import org.utils.SecretsUtil

/**
 * PipelineRunner orchestrates the execution of CI/CD pipeline stages in a Kubernetes pod.
 * It sets up a dynamic agent pod and runs security scans, builds, and deployments sequentially.
 */
class PipelineRunner implements Serializable {
    def script
    Map config

    /**
     * Constructor for PipelineRunner.
     * @param script The Jenkins pipeline script context.
     * @param config The configuration map for the pipeline.
     */
    PipelineRunner(def script, Map config) {
        this.script = script
        this.config = config
    }

    /**
     * Executes the full CI/CD pipeline by setting up a Kubernetes pod and running all stages.
     */
    def execute() {
      def label = "dynamic-agent-${UUID.randomUUID().toString()}"
      script.podTemplate(label: label, yaml: script.libraryResource('pod_agent_demo_template.yaml')){
        script.node(label){
            new Checkout(script, config).run()  //source code checkout
            new Checkout(script, config).run2() //kubernetes deployment yaml checkout
            new CheckmarxSast(script, config).run() //checkmarx sast scan
            new CheckmarxSca(script, config).run() //checkmarx sca scan
            new TechStackBuild(script, config).run() //tech stack build
            def dockerBuild = new DockerBuild(script, config)
            dockerBuild.run() //docker build
            new TwistlockScan(script, config).run() //twistlock image scan
            new ImagePush(script, config).run() //image push to registry
            new PreDeploy(script, config).run() //pre-deployment steps
            new Deploy(script, config).run() //kubernetes deployment
            new PostDeploy(script, config).run() //post-deployment steps

            // here we can add more stages accordingly



         }
      }
    }
}