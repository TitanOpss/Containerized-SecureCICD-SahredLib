@Library(['Containerized-SecureCICD-SahredLib', 'AGENTS']) _        //Containerized-SecureCICD-SahredLib is the shared library name for jenkins
                                                                    //AGENTS is the library having agent pod templates (your containers definitions)
                                                                    //as this setup is for kubernetes based jenkins agents                                                                 
autopipe(

repoCredsId: 'bb-creds',
kubeconfigFile: 'app-kube',
Artifactory_CREDS: 'app-artifactory-creds',
Checkmarx_Creds: 'checkmarx-creds',
Checkmarx_Server_Url: 'http://checkmarx-server.local',
projectName: 'MyApp-SAST-Scan',
deploymentName: 'myapp-deployment',
namespace: 'myapp-namespace',
artifactoryUrl: 'http://artifactory.local',
repoName: 'myapp-docker-local',
npmrcFile: 'app-npmrc',                     //if it is npm build ( we can pass accordingly)
serviceName: 'myapp-service',                // service name for sonarqube scan
repoUrl: 'http://git.local/myorg/myapp.git',
branch: 'main',
env: 'dev',
fileRepoUrl: 'http://git.local/myorg/config-repo.git',         //for maniifest files
fileBranch:'main'           //branch for maniifest files
buildType: 'npm'                    //build type can be maven, nodejs (currently supported, we can add stages accordingly)
deployType: 'kubefiles'                   //currently kubefiles (deployment.yaml) not helm ( we can add helm charts deploy stage accordingly

)
