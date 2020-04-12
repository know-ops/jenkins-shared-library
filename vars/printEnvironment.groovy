#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=ProjectSpec) Closure<?> c = null) {
    def project = new ProjectSpec(this)

    if (c != null) {
        def code = c.rehydrate(project, this, this)
        code.resolveStrategy = Closure.DELEGATE_ONLY
        code()
    }

    pipeline {
        agent {
            kubernetes {
                label "k8s-openjdk8-agent"
            }
        }

        stages {
            stage('project') {
                steps {
                    echo """
Name: ${project.name}
Repository: ${project.repository}
Langague: ${project.language}
Build Tool: ${project.buildTool}
                    """
                }
            }

            stage('environment') {
                steps {
                    sh "printenv | sort"
                }
            }
        }
    }
}
