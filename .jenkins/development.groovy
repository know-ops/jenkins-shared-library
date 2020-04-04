def printEnv() {
    sh 'printenv | sort'
}

pipeline {
    agent none

    stages {
        stage('Build with Gradle') {
            parallel {
                stage('JDK8') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk8-agent"
                        }
                    }

                    steps {
                        printEnv()
                    }
                }
                stage('JDK11') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk11-agent"
                        }
                    }

                    steps {
                        printEnv()
                    }
                }
                stage('JDK14') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk14-agent"
                        }
                    }

                    steps {
                        printEnv()
                    }
                }
            }
        }
    }
}