#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.core.ProjectSpec

void call(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> overrides = null) {
    workflow('kubernetes') {
        project overrides

        stages('Initial Stages') {
            parallel true

            stage('Project') {
                agent {
                    label 'k8s-agent'
                }

                steps {
                    echo """
Name: ${project.name}
Repository: ${project.repository}
Language: ${project.language}
Build Tool: ${project.buildTool}
Config: ${project.config}
                    """
                }
            }

            stage('Node Environment') {
                agent {
                    label 'k8s-agent'
                }

                steps {
                    sh 'printenv | sort'
                }
            }

            stage('Pipeline Environment') {
                agent {
                    label 'k8s-agent'
                }

                steps {
                    script {
                        def fields = env

                        fields.each { value -> 
                            println("${value}")
                        }
                    }
                }
            }
        }
    }
}
