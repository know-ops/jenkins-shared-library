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
                    label project.config['kubernetes']['node']['build']['label']
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
                    label project.config['kubernetes']['node']['build']['label']
                }

                steps {
                    sh 'printenv | sort'
                }
            }

            stage('Pipeline Environment') {
                agent {
                    label project.config['kubernetes']['node']['build']['label']
                }

                steps {
                    script {
                        def fields = env.getEnvironment()

                        fields.each { value -> 
                            println("${value}")
                        }
                    }
                }
            }
        }
    }
}
