#!/usr/bin/env groovy

import groovy.lang.DelegatesTo
import groovy.transform.Field

import com.knowops.ci.jenkins.Project

@groovy.transform.Field
String phase = 'project'

void call(String projectType, Closure<?> overrides = { }) {

    Project project = new Project(projectType, this)

    exec(project, overrides)

}

void call(Closure<?> overrides = { }) {

    Project project = new Project(this)

    exec(project, overrides)

}

private void exec(Project p, Closure<?> overrides) {

    init(p, overrides)

    workflow('project') {
        project {
            p
        }

        stages {
            agent {
                node platform.nodes.autodetect
            }

            stage('checkout') {
                steps {
                    scm checkout
                }
            }

            // stage('type') {
            //     dependsOn [
            //         'checkout',
            //     ]

            //     when {
            //         expression {
            //             project.autodetect.type
            //         }
            //     }

            //     steps {

            //     }
            // }

            // stage('language') {
            //     dependsOn [
            //         'checkout',
            //     ]

            //     when {
            //         expression {
            //             project.autodetect.language
            //         }
            //     }

            //     steps('language') {

            //     }
            // }

            // stage('tool') {
            //     dependsOn [
            //         'checkout',
            //     ]

            //     when {
            //         expression {
            //             project.autodetect.tool
            //         }
            //     }

            //     steps {

            //     }
            // }
        }
    }

}

private void init(Project project, @DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> overrides) {

    project.init(phase)

    if (overrides) {
        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = project
        overrides()
    }

}
