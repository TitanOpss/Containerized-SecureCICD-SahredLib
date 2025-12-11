package org.devops.stages

class TwislockScan implements Serializable{
    def script
    Map config

    TwislockScan(def script, Map config){
        this.script=script
        this.config=config
    }

    void run(){
        script.stage("twislock-scan"){
            script.container('twislock'){
                script.dir('source'){
                    script.sh """
    echo 'Starting Twistlock Scan'
    twislock scan \
        --image $ \
        --policy ${config.twislock_Policy} \
        --report-format ${config.twislock_Report_Format} \
        --report-path ${config.twislock_Report_Path}
"""

                }
            }
        }
    }
}