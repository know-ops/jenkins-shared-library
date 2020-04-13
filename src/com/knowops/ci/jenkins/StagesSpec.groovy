#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec implements Serialiable {

    private final Object post = null

    private final Object steps
    private final Map<StageSpec> stages = [:]

    StagesSpec(Object s) {
        this.steps = s
    }

    void stage(String label, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stage) {
        this.stages[label] = new StageSpec()

        stage.resolveStrategy = Closure.DELEGATE_FIRST
        stage.delegate = this.stages[label]
        stage()
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
