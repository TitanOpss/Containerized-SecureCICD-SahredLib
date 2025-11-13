package org.devops.stages


class Checkout implements Serializable {

    def script
    Map config

       Checkout(def script, Map config) {

        this.script=script
        this.config=config
       }

       void run(){
        script.stage("checkout-source-code")
       }
 
}