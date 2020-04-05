def gradleValidate() {
    container('openjdk') {
        sh './gradlew -v'
        sh './gradlew task'
    }
}

def gradleBuildTest() {
    container('openjdk') {
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
                        stage('Gradle Validation') {
                            steps {
                                gradleValidate()
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
                        stage('Gradle Validation') {
                            steps {
                                gradleValidate()
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
                        stage('Gradle Validation') {
                            steps {
                                gradleValidate()
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