#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class WorkflowSpec extends BaseSpec {

    private final ProjectSpec project

    WorkflowSpec(Object s) {
        super(s)
        this.project = new ProjectSpec(s)
    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> pj = null) {

        if (pj) {
            pj.resolveStrategy = Closure.DELEGATE_FIRST
            pj.delegate = this.project
            pj()
        }

        // HACK: to make sure project language detection happens in it's own pod
        this.project.language
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.agent.stages(name, stgs)
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.stages('Stages', stgs)
    }

}
