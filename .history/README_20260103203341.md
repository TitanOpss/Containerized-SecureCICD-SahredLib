# Containerized Secure CI/CD Shared Library

This is a Jenkins Shared Library for implementing a secure, containerized CI/CD pipeline using Kubernetes pod agents. The library provides a comprehensive pipeline that includes source code checkout, security scans, builds, containerization, and deployment to Kubernetes.

## Overview

The pipeline is designed for DevSecOps practices, incorporating multiple security scanning tools and automated deployment processes. It supports various build types (Maven, NPM, Gradle) and deployment methods (Kubernetes manifests).

## Architecture

- **Kubernetes Pod Agents**: Uses dynamic pod templates with containers for different tech stacks (Docker, Java, Node.js, Python)
- **Shared Library Structure**: Organized as a Jenkins shared library with reusable pipeline components
- **Security-First Approach**: Integrates Checkmarx SAST/SCA and Twistlock image scanning
- **Containerized Builds**: All operations run in isolated containers within Kubernetes pods

## Project Structure

```
├── pod_agent_demo_template.yaml    # Kubernetes pod template for Jenkins agents
├── main-pipe/
│   └── mainpipe.groovy            # Example pipeline script
├── src/org/devops/
│   ├── PipelineRunner.groovy      # Main pipeline orchestrator
│   └── stages/                    # Individual pipeline stages
│       ├── Checkout.groovy        # Source code and manifest checkout
│       ├── CheckmarxSast.groovy   # Checkmarx SAST scanning
│       ├── CheckmarxSca.groovy    # Checkmarx SCA scanning
│       ├── TechStackBuild.groovy  # Application build (Maven/NPM/Gradle)
│       ├── DockerBuild.groovy     # Docker image building
│       ├── TwistlockScan.groovy   # Container image security scanning
│       ├── ImagePush.groovy       # Pushing images to registry
│       ├── PreDeploy.groovy       # Pre-deployment tasks
│       ├── Deploy.groovy          # Kubernetes deployment
│       └── PostDeploy.groovy      # Post-deployment tasks
├── vars/
│   └── autoPipe.groovy            # Global pipeline function
└── resources/
    └── checklist/                 # Pre/post deployment scripts
        ├── pre-deployment.sh
        └── post-deployment.sh
```

## Prerequisites

- Jenkins with Kubernetes plugin configured
- Kubernetes cluster with Jenkins service account
- Artifactory or Docker registry access
- Checkmarx server for security scanning
- Twistlock for container scanning
- Git repositories for source code and deployment manifests

## Setup

1. **Install Shared Library**: Add this repository as a shared library in Jenkins global configuration
2. **Configure Credentials**: Set up the following Jenkins credentials:
   - `bb-creds`: Bitbucket/Git credentials
   - `app-kube`: Kubernetes config file
   - `app-artifactory-creds`: Artifactory credentials
   - `checkmarx-creds`: Checkmarx authentication
3. **Pod Template**: Ensure the pod template YAML is available in the library resources

## Usage

### Basic Pipeline Usage

```groovy
@Library(['Containerized-SecureCICD-SahredLib', 'AGENTS']) _

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
    npmrcFile: 'app-npmrc',
    serviceName: 'myapp-service',
    repoUrl: 'http://git.local/myorg/myapp.git',
    branch: 'main',
    env: 'dev',
    fileRepoUrl: 'http://git.local/myorg/config-repo.git',
    fileBranch: 'main',
    buildType: 'npm',
    deployType: 'kubefiles',
    twislock_Report_Format: 'json',
    twislock_Report_Path: 'twislock-report.json',
    twislock_Policy: 'Default'
)
```

### Configuration Parameters

| Parameter | Description | Required |
|-----------|-------------|----------|
| repoCredsId | Git credentials ID | Yes |
| kubeconfigFile | Kubernetes config file credential ID | Yes |
| Artifactory_CREDS | Artifactory credentials ID | Yes |
| Checkmarx_Creds | Checkmarx credentials ID | Yes |
| Checkmarx_Server_Url | Checkmarx server URL | Yes |
| projectName | Checkmarx project name | Yes |
| deploymentName | Kubernetes deployment name | Yes |
| namespace | Kubernetes namespace | Yes |
| artifactoryUrl | Artifactory URL | Yes |
| repoName | Artifactory repository name | Yes |
| npmrcFile | NPM config file credential ID (for NPM builds) | Conditional |
| serviceName | Application service name | Yes |
| repoUrl | Source code repository URL | Yes |
| branch | Source code branch | Yes |
| env | Environment (dev/staging/prod) | Yes |
| fileRepoUrl | Deployment manifests repository URL | Yes |
| fileBranch | Deployment manifests branch | Yes |
| buildType | Build type (maven/npm/gradle) | Yes |
| deployType | Deployment type (kubefiles/helm) | Yes |
| twislock_Report_Format | Twistlock report format | Yes |
| twislock_Report_Path | Twistlock report path | Yes |
| twislock_Policy | Twistlock scanning policy | Yes |

## Pipeline Stages

1. **Checkout**: Clones source code and deployment manifests from Git
2. **Checkmarx SAST**: Performs static application security testing
3. **Checkmarx SCA**: Performs software composition analysis
4. **Tech Stack Build**: Builds the application (Maven/NPM/Gradle)
5. **Docker Build**: Creates Docker container image
6. **Twistlock Scan**: Scans container image for vulnerabilities
7. **Image Push**: Pushes image to Artifactory registry
8. **Pre-Deploy**: Runs pre-deployment checklist scripts
9. **Deploy**: Deploys to Kubernetes using manifests
10. **Post-Deploy**: Runs post-deployment verification scripts

## Supported Build Types

- **Maven**: Requires `settingXml` credential for Maven settings
- **NPM**: Requires `npmrcFile` credential for NPM configuration
- **Gradle**: Requires `gradlePropsFile` credential for Gradle properties

## Deployment Types

- **kubefiles**: Uses Kubernetes YAML manifests
- **helm**: Helm chart deployment (planned feature)

## Security Features

- **SAST Scanning**: Checkmarx static analysis for code vulnerabilities
- **SCA Scanning**: Checkmarx dependency vulnerability analysis
- **Container Scanning**: Twistlock image vulnerability assessment
- **Secrets Management**: Secure handling of credentials and config files

## Customization

The pipeline is highly modular. Individual stages can be modified or extended by:

1. Editing the stage classes in `src/org/devops/stages/`
2. Adding new stages to the `PipelineRunner.execute()` method
3. Modifying the pod template for additional containers
4. Updating pre/post-deployment scripts in `resources/checklist/`

## Troubleshooting

- Ensure all required credentials are configured in Jenkins
- Verify Kubernetes cluster connectivity and permissions
- Check Artifactory and Checkmarx server accessibility
- Review Jenkins logs for stage-specific errors
- Validate deployment manifests in the config repository

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make changes following the existing code structure
4. Test thoroughly in a Jenkins environment
5. Submit a pull request

## License

[Specify your license here]