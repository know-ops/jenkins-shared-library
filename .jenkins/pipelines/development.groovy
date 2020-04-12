// #!/usr/bin/env groovy
// library "jenkins-shared-library@$BRANCH_NAME"

// declarativeK8sLibJenkins()

pipeline {
  agent {
    kubernetes {
      label "k8s-openjdk8-agent"
    }
  }

  stages {
    stage('Print Environment') {
      steps {
        sh 'printenv | sort'
        echo "${env.GIT_URL}"
      }
    }
  }
}