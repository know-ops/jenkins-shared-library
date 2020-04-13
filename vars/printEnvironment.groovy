#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=ProjectSpec) Closure<?> overrides = null) {
    workflow {
        project overrides

        agent {
            kubernetes {
                label 'k8s-agent'
            }
        }

//         stages {
//             stage('display environment') {
//                 checkout scm

//                 sh "printenv | sort"

//                 Map<Closure> tasks = [
//                     'project': {
//                         echo """
// Name: ${project.name}
// Repository: ${project.repository}
// Language: ${project.language}
// Build Tool: ${project.buildTool}
//                         """
//                     },
//                     'environment': {
//                         sh "printenv | sort"
//                     }
//                 ]

//                 parallel(tasks)
//             }
//         }
    }
}
