#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=ProjectSpec) Closure<?> c = null) {
    def project = new ProjectSpec(this)

    if (c != null) {
        // def code = c.rehydrate(project, this, this)
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c.delegate = project
        c()
        // project.name = 'Jenkins Shared Library'
    }

    pipeline {
        agent {
            kubernetes {
                label "k8s-github-linguist-agent"
            }
        }

        stages {
            stage('print build environment') {
                parallel {
                    stage('project') {
                        steps {
                            echo """
Name: ${project.name}
Repository: ${project.repository}
Language: ${project.language}
Build Tool: ${project.buildTool}
                            """
                        }
                    }

                    stage('environment') {
                        steps {
                            sh "printenv | sort"
                        }
                    }

                    stage('language') {
                        steps {
                            container('linguist') {
                                sh 'github-linguist --breakdown'
                            }
                        }
                    }
                }
            }
        }
    }
}
