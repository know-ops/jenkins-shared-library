pipeline {
    agent none

    stages {
        stage('Build with Gradle') {
            matrix {
                axes {
                    axis {
                        name 'JDK'
                        values 'jdk8', 'jdk11', 'jdk14'
                    }
                }

                agent {
                    label "k8s-gradle-${JDK}-agent"
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