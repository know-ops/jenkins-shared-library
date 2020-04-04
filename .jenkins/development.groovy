def printEnv() {
    sh 'printenv | sort'
}

def gradleBuildTest() {
    container('gradle') {
        sh "sleep 300 && gradle version"
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
                            label "gradle-jdk8"
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
                            label "gradle-jdk11"
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
                            label "gradle-jdk14"
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