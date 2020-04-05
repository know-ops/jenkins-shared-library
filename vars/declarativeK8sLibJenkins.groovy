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

def matrixK8sLabel(Map opt = [:]) {
    String name = opt.get('name','k8s-agen')
    String defaultLabel = "${name.replace('+','_')}-${UUID.randomUUID().toString()}"
    String label = opt.get('label', defaultLabel)
    String cloud = opt.get('cloud', 'kubernetes')
    def retVal = [:]

    printf label
    printf cloud

    retVal['cloud'] = cloud
    retVal['label'] = label

    return retVal
}

def call() {
    pipeline {
        agent none

        stages {
            stage('Build with Gradle') {
                matrix {
                    agent {
                        kubernetes(matrixK8sLabel(label: "k8s-${JDK.toString()}-agent"))
                    }

                    axes {
                        axis {
                            name 'JDK'
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
