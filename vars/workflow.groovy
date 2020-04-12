#!/usr/bin/env grroovy

import groovy.lang.Closure
import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.WorkflowSpec

def call(@DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=WorkflowSpec) Closure<?> wf) {
    WorkflowSpec workflow = new WorkflowSpec(this)

    wf.resolveStrategy = Closure.DELEGATE_FIRST
    wf.delegate = workflow
    wf()
}