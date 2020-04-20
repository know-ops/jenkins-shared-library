#!/usr/bin/env grroovy

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.core.ProjectSpec
import com.knowops.ci.jenkins.core.WorkflowSpec

ProjectSpec project

void call(Closure<?> wf) {

    WorkflowSpec workflow = new WorkflowSpec(this)
    doWorkflow(workflow, wf)

}

void call(String platform, Closure<?> wf) {

    WorkflowSpec workflow = new WorkflowSpec(platform, this)
    doWorkflow(workflow, wf)

}

void doWorkflow(WorkflowSpec workflow, @DelegatesTo(strategy=Closure.DELEGATE_ONLY, value=WorkflowSpec) Closure<?> wf) {
    wf.resolveStrategy = Closure.DELEGATE_FIRST
    wf.delegate = workflow
    wf()

    workflow()
}
