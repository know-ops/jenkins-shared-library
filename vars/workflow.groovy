#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.WorkflowSpec

call(@DelegatesTo(strategy=Clsoure.DELEGATE_FIRST, value=WorkflowSpec) Clsoure<?> wf) {
    WorkflowSpec workflow = new WorkflowSpec()

    wf.resolveStrategy = Clsoure.DELEGATE_FIRST
    wf.delegate = workflow
    wf()
}