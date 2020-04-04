def printEnv() {
    sh 'printenv | sort'
}

def gradleBuildTest() {
    stages {
        stage('Gradle Check') {
            sh "gradle check"
        }
    }
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

                    gradleBuildTest()
                }
                stage('JDK11') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk11-agent"
                        }
                    }

                    gradleBuildTest()
                }
                stage('JDK14') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk14-agent"
                        }
                    }

                    gradleBuildTest()
                }
            }
        }
    }
}