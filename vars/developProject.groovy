#!/usr/bin/env groovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.Project

String stage = 'development'

void call(String projectType, Closure<?> overrides) {

    Project project = new Project(projectType, this)

    exec(project, overrides)

}

void call(Closure<?> overrides) {

    Project project = new Project(this)

    exec(project, overrides)

}

void call(String projectType) {

    Project project = new Project(projectType, this)

    exec(project, stage)

}

void call() {

    Project project = new Project(this)

    exec(project, stage)

}

private void exec(Project project, Closure<?> overrides) {

    init(project, overrides)
    println project.type
    println project.autodetect
    // auto(project)

}

private void init(Project project, @DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> overrides = { }) {

    init(project)

    if (overrides) {
        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = project
        overrides()
    }

}

private void init(Project project) {

    project.init(stage)

}

private void auto(Project project) {

    if (project.type == 'Multi') {
        project.autodetect['type'] = true
    } else if (project.type) {
        project.autodetect = [
            'type': false,
            'language': false,
            'tool': false,
        ]
    }

    if (!project.type || project.type == 'Multi') {
        project.autodetect.each { selection, auto ->
            if (auto) {
                detect(project)

                return true￼
￼
            }

        }

    }

}

private void detect(Project project) {

    workflow('project') {
        agent 'project'

        stages {
            stage('checkout') {
                steps {
                    scm checkout
                }
            }

            stage('type') {
                dependsOn [
                    'checkout',
                ]

                when {
                    expression {
                        project.autodetect['type']
                    }
                }

                steps {

                }
            }

            stage('language') {
                dependsOn: [
                    'checkout',
                ]

                when {
                    expression {
                        project.autodetect['language']
                    }
                }

                steps('language') {

                }
            }

            stage('tool') {
                dependsOn: [
                    'checkout',
                ]

                when {
                    expression {
                        project.autodetect['tool']
                    }
                }

                steps {

                }
            }
        }
    }

}
