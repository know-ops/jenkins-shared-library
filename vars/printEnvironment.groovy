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

    if (!project.language) {
        _ = project.language
    }
    podTemplate(label: 'k8s-agent') {
        node('k8s-agent') {
            Map<Closure> tasks = [
                'project': {
                    echo """
    Name: ${project.name}
    Repository: ${project.repository}
    Language: ${project.language}
    Build Tool: ${project.buildTool}
                    """
                },
                'environment': {
                    sh "printenv | sort"
                },
                'language': {
                    container('linguist') {
                        sh 'github-linguist'
                        sh 'github-linguist --breakdown'
                        sh 'github-linguist --json'
                        sh 'github-linguist --breakdown --json'
                    }
                }
            ]

            parallel(tasks)
        }
    }
}
