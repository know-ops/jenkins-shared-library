#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

void call(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> overrides = null) {
    workflow {
        project overrides

        stages {
            parallel true

            stage('Project') {
                agent {
                    kubernetes {
                        label 'k8s-agent'
                    }
                }

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
                agent {
                    kubernetes {
                        label 'k8s-agent'
                    }
                }

                steps {
                    sh 'printenv | sort'
                }
            }
        }
    }
}
