#!/usr/bin/env groovy

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

def buildNode = [:]

def call() {
    pipeline {
        agent none

        stages {
            stage('Build with Gradle') {
                matrix {
                    agent {
                        kubernetes {
                            label "k8s-${PLATFORM}-agent"
                        }
                    }

                    axes {
                        axis {
                            name 'PLATFORM'
                            values 'openjdk8', 'openjdk11', 'openjdk14'
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
