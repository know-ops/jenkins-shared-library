#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import groovy.lang.DelegatesTo

class WorkflowSpec extends BaseSpec {

    private final ProjectSpec project

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
        this.project.language
        this.script.echo 'cfg: workflow: project'
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.script.echo 'cfg: workflow: stages'
        this.ag.stages(name, stgs)
        this.script.echo 'cfg: workflow: stages'
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.stages('', stgs)
    }

    @NonCPS
    private void init(String platform = '') {
        this.script.echo 'init: workflow'
        if (platform) {
            this.project = new ProjectSpec(platform, this.script)
        } else {
            this.project = new ProjectSpec(this.script)
        }
    }
}
