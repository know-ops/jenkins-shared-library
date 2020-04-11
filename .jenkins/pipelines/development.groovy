// #!/usr/bin/env groovy
// library "jenkins-shared-library@$BRANCH_NAME"

// declarativeK8sLibJenkins()

pipepline {
  agent {
    kubernetes {
      label "k8s-openjdk8-agent"
    }
  }

  stages {
    stage('Print Environment') {
      sh 'printenv | sort'
    }
  }
}