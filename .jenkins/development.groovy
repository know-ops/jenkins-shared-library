def printEnv() {
    sh 'printenv | sort'
    sh 'ls -la'
    container('gradle') {
        sh './gradlew -v'
        sh './gradlew task'
    }
}

def gradleBuildTest() {
    container('gradle') {
        sh "./gradlew check"
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
                            label "k8s-openjdk8-agent"
                        }
                    }

                    stages {
                        stage('Print Environment') {
                            steps {
                                printEnv()
                            }
                        }
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
                            label "k8s-openjdk11-agent"
                        }
                    }

                    stages {
                        stage('Print Environment') {
                            steps {
                                printEnv()
                            }
                        }
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
                            label "k8s-openjdk14-agent"
                        }
                    }

                    stages {
                        stage('Print Environment') {
                            steps {
                                printEnv()
                            }
                        }
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