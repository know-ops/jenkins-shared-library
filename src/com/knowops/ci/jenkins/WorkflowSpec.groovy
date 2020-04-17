#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class WorkflowSpec extends BaseSpec {

    private final ProjectSpec project

    WorkflowSpec(Object s) {
        super(s)
        s.echo 'init: workflow'
        this.project = new ProjectSpec(s)
    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> pj = null) {
        this.script.echo 'cfg: workflow: project'
        if (pj) {
            pj.resolveStrategy = Closure.DELEGATE_FIRST
            pj.delegate = this.project
            pj()
        }

        // HACK: to make sure project language detection happens in it's own pod
        this.project.language
        this.script.echo 'cfg: workflow: project'
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.script.echo 'cfg: workflow: stages'
        this.ag.stages(name, stgs)
        this.script.echo 'cfg: workflow: stages'
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.stages('Stages', stgs)
    }

}
