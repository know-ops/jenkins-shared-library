#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class WorkflowSpec implements Serializable {

    private ProjectSpec project
    private AgentSpec agent
    private StagesSpec stages
    private Object post

    private final Object steps

    WorkflowSpec(Object s) {
        this.steps = s
    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=ProjectSpec) Closure<?> pj = null) {
        this.project = new ProjectSpec(this.steps)

        if (pj) {
            pj.resolveStrategy = Closure.DELEGATE_FIRST
            pj.delegate = this.project
            pj()
        }

        // HACK: to make sure project language detection happens in it's own pod
        this.project.language
    }

    void agent(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AgentSpec) Closure<?> a = null) {
        this.agent = new AgentSpec(this.steps)

        if (a) {
            a.resolveStrategy = Closure.DELEGATE_FIRST
            a.delegate = this.agent
            a()
        }
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.stages = new StagesSpec(this.steps)

        stgs.resolveStrategy = Closure.DELEGATE_FIRST
        stgs.delegate = this.stages
        stgs()
    }

    void call() {
        // TO-DO: Throw exception if nothing to do

        if (this.stages) {
            try {
                if (this.agent) {
                    this.agent.call(this.stagesClosure())
                } else {
                    this.stages.call()
                }
            } catch (e) {
                if (!this.post) {
                    throw e
                }
            } finally {
                if (this.post) {
                    this.post()
                }
            }
        }
    }

    @NonCPS
    private Closure<?> stagesClosure() {
        return this.stages.&call
    }
}
