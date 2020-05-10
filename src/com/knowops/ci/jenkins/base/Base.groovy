#!/usr/bin/env groovy
package com.knowops.ci.jenkins.base

class Base implements Serializable {

    String id
    Object steps

    Base(String id, Object steps) {
        this.id = id
        this.steps = steps
    }

    void project(Project p) {
        this.steps.echo 'Setting project'
        this.project = p

        this.steps.setProperty('platform', this.platform)

    }

    void dependsOn(List ids) {

    }

    void when(Closure<?> w) {

    }

    void post(Closure<?> p) {

    }

}
