#!/usr/bin/env groovy

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
                                container('openjdk') {
                                    sh './gradlew -v'
                                    sh './gradlew task'
                                }
                            }
                        }
                        stage('Gradle Check') {
                            steps {
                                container('openjdk') {
                                    sh "./gradlew check"
                                }
                            }

                            post {
                                always {
                                    junit testResults: '**/build/test-results/test/*.xml', allowEmptyResults: false, keepLongStdio: true
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
