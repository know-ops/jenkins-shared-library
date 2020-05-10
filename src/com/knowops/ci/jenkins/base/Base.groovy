#!/usr/bin/env groovy
package com.knowops.ci.jenkins.base

class Base implements Serializable {

    String id
    Object steps

    Base(String id, Object steps) {
        this.id = id
        this.steps = steps
    }

    void dependsOn(List ids) {

    }

    void when(Closure<?> w) {

    }

    void post(Closure<?> p) {

    }

}
