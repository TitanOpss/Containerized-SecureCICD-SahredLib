/**
 * autoPipe is a global Jenkins Shared Library function that initializes and runs the CI/CD pipeline.
 * @param config A map containing pipeline configuration parameters.
 */
def call(Map config = [:]) {
    def runner = new org.devops.PipelineRunner(this, config)
    runner.execute()
}