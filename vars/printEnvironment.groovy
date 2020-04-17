#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> overrides = null) {
    workflow {
        project overrides

        stages('Stages') {
            parallel true

            agent {
                kubernetes {
                    label 'k8s-agent'
                }
            }

            stage('Project') {
                steps {
                    echo """
Name: ${project.name}
Repository: ${project.repository}
Language: ${project.language}
Build Tool: ${project.buildTool}
                    """
                }
            }

            stage('Environment') {
                steps {
                    sh 'printenv | sort'
                }
            }
        }
    }
}
