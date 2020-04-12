#!/usr/bin/env groovy

import com.knowops.ci.jenkins.ProjectSpec

call(@DelegateTo(value = ProjectSpec, strategy = Closure.DELEGATE_ONLY) Closure<?> c = null) {
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
