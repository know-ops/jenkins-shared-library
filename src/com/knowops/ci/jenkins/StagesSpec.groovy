#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec implements Serializable {

    private AgentSpec agent
    private final Object post = null

    private final Object steps
    private final Map<String,StageSpec> stages = [:]

    StagesSpec(Object s) {
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

    void stage(String label, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.stages[label] = new StageSpec(this.steps)

        stg.resolveStrategy = Closure.DELEGATE_FIRST
        stg.delegate = this.stages[label]
        stg()
    }

    void call() {
        try {
            if (this.agent) {
                this.agent(this.doStages())
            } else {
                this.doStages()()
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

    private Closure<?> doStages() {
        return {
            this.stages.each { label, stg ->
                this.steps.stage(label, stg.&call)
            }
        }
    }

}
