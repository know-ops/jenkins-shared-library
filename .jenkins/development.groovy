pipeline {
    agent none

    stages {
        stage('Build with Gradle') {
            matrix {
                agent {
                    kubernetes {
                        label "k8s-grade-jdk${JDK}-agent"
                    }
                }

                axes {
                    axis {
                        name 'JDK'
                        values '8', '11', '14'
                    }
                }

                stages {
                    stage('Testing Matrix') {
                        sh 'printenv | sort'
                    }
                }
            }
        }
    }
}