#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.Workflow

void call(String id, @DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> work) {

    Workflow wf = new Workflow(id)

    work.resolveStrategy = Closure.DELEGATE_FIRST
    work.delegate = wf
    work()

    println wf.id
    println wf.phase

}
