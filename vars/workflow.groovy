#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.WorkflowSpec

call(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=WorkflowSpec) Closure<?> wf) {
    WorkflowSpec workflow = new WorkflowSpec()

    wf.resolveStrategy = Closure.DELEGATE_FIRST
    wf.delegate = workflow
    wf()
}