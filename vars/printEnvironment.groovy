#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.ProjectSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=ProjectSpec) Closure<?> overrides = null) {
    workflow {
        project overrides
//         stage('display environment') {
//             podTemplate(label: 'k8s-agent') {
//                 node('k8s-agent') {
//                     checkout scm

//                     sh "printenv | sort"

//                     Map<Closure> tasks = [
//                         'project': {
//                             echo """
// Name: ${project.name}
// Repository: ${project.repository}
// Language: ${project.language}
// Build Tool: ${project.buildTool}
//                             """
//                         },
//                         'environment': {
//                             sh "printenv | sort"
//                         }
//                     ]

//                     parallel(tasks)
//                 }
//             }
//         }
    }
}
