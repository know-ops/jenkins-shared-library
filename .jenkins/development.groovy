def printEnv() {
    sh 'printenv | sort'
}

def gradleBuildTest() {
    container('gradle') {
        sh "gradle -v"
        sh "gradle check"
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

                    stages {
                        stage('Gradle Check') {
                            steps {
                                gradleBuildTest()
                            }
                        }
                    }
                }
                stage('JDK11') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk11-agent"
                        }
                    }

                    stages {
                        stage('Gradle Check') {
                            steps {
                                gradleBuildTest()
                            }
                        }
                    }
                }
                stage('JDK14') {
                    agent {
                        kubernetes {
                            label "k8s-gradle-jdk14-agent"
                        }
                    }

                    stages {
                        stage('Gradle Check') {
                            steps {
                                gradleBuildTest()
                            }
                        }
                    }
                }
            }
        }
    }
}