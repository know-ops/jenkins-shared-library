#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class WorkflowSpec implements Serializable {

    private ProjectSpec project

    private final Object steps

    WorkflowSpec(Object s) {
        this.steps = s
    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> pj) {
        this.project = new ProjectSpec(this.steps)

        if (pj) {
            pj.resolveStrategy = Closure.DELEGATE_FIRST
            pj.delegate = this.project
            pj()
        }

        // HACK: to make sure project language detection happens in it's own pod
        this.project.language
    }
}