#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class Workflow implements Serializable {

    Project project

    String id
    Object steps

    Workflow(String id, steps) {
        this.id = id
        this.steps = steps
    }

    void project(Project p) {
        this.steps.echo 'Setting project'
        this.project = p

        this.steps.setProperty('project', this.project)
        this.steps.setProperty('platform', this.project.platform)

    }

    void dependsOn() {

    }

    void stages(Closure<?> s) {

    }

    void post() {

    }

}
