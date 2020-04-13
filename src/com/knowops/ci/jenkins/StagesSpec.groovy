#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec implements Serializable {

    private final Object post = null

    private final Object steps
    private final Map<String,StageSpec> stages = [:]

    StagesSpec(Object s) {
        this.steps = s
    }

    void stage(String label, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.steps.echo "stage: ${label}"
        this.stages[label] = new StageSpec()

        stg.resolveStrategy = Closure.DELEGATE_FIRST
        stg.delegate = this.stages[label]
        stg()
    }

    void call() {
        try {
            this.stages.each { label, stage ->
                this.steps.stage(label, stage)
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
