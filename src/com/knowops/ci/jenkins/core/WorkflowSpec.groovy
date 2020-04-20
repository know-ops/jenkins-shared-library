#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import groovy.lang.DelegatesTo

class WorkflowSpec extends BaseSpec {

    ProjectSpec project

    WorkflowSpec(Object s) {
        super(s)
        this.init()
    }

    WorkflowSpec(String p, Object s) {
        super(p, s)
        this.init(p)
    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> pj = null) {
        if (pj) {
            pj.resolveStrategy = Closure.DELEGATE_FIRST
            pj.delegate = this.project
            pj()
        }

        // HACK: to make sure project language detection happens in it's own pod
        // this.project.language
        this.project.initStages()
        this.script.setProperty('project', this.project)
        this.script.echo "${this.class}.project"
    }

    void stages(String name, Closure<?> stgs) {
        this.ag.stages(name, stgs)

    }

    void stages(Closure<?> stgs) {
        this.stages('', stgs)
    }

    @Override
    void call() {
        this.script.echo "workflow: ${this.class}.call"
        this.project.call()
        this.ag.call()
    }

    @NonCPS
    private void init(String platform = '') {
        if (platform) {
            this.project = new ProjectSpec(platform, this.script)
        } else {
            this.project = new ProjectSpec(this.script)
        }
    }

}
