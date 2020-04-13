#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StageSpec {

    private AgentSpec agent
    private Object post

    private final Object steps
    private Closure stepsClosure

    StageSpec(Object s) {
        this.steps = s
    }

    void agent(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AgentSpec) Closure<?> a) {
        this.agent = new AgentSpec(this.steps)

        if (a) {
            a.resolveStrategy = Closure.DELEGATE_FIRST
            a.delegate = this.agent
            a()
        }
    }

    void steps(Closure<?> s) {
        s.resolveStrategy = Closure.DELEGATE_FIRST
        s.delegate = this.steps

        this.stepsClosure = s
    }

    void call() {
        try {
            if (this.agent) {
                this.agent(this.stepsClosure)
            } else {
                this.stepsClosure()
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
