#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class Workflow implements Serializable {

    Project project
    String phase
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

    void setProject(Project p) {
        this.steps.echo "Setting project with ${p.name}"
        this.project = p
    }

}
