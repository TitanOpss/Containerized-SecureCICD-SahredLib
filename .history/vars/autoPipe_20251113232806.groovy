def call(Map config = [:]){
    def runner = new org.devops.PipelineRunner(this, config)
    runner.execute()
}