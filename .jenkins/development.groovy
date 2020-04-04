pipeline {
    agent none

    stages {
        stage('Build with Gradle') {
            matrix {
                axes {
                    axis {
                        name 'JDK'
                        values '8', '11', '14'
                    }
                }

                agent {
                    kubernetes {
                        label "k8s-agent"
                    }
                }

                stages {
                    stage('Testing Matrix') {
                        steps {
                            sh 'printenv | sort'
                        }
                    }
                }
            }
        }
    }
}