#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class Workflow implements Serializable {

    Project project
    String phase
    String id

    Workflow(String id) {
        this.id = id
    }

    void dependsOn() {

    }

    void stages(Closure<?> s) {

    }

    void post() {

    }
}
