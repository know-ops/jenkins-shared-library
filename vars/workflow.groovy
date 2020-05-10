#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.Project

void call(String id, @DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> work) {

    println id
    println project.type
    println platform.nodes

}
