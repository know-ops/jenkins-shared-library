#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.core.WorkflowSpec

void call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=WorkflowSpec) Closure<?> wf) {

    WorkflowSpec workflow = new WorkflowSpec(this)

    wf.resolveStrategy = Closure.DELEGATE_FIRST
    wf.delegate = workflow
    echo 'init'
    wf()

    echo 'exec'
    workflow()

}