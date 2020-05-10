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

    void dependsOn() {

    }

    void stages(Closure<?> s) {

    }

    void post() {

    }

    void project(Closure<Project> p) {
        this.steps.echo 'Setting project'
        this.project = p()
    }

}
