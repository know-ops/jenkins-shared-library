#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec extends BaseSpec {

    parallel = false

    StagesSpec(Object s) {
        super(s)
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.ag.stage(name, stg)
    }

    void setParallel(Boolean p) {
        this.script.echo 'cfg: stages: parallel'
        if (this.ag.kubernetes) {
            this.script.echo 'cfg: stages: parallel: kubernetes'
            this.ag.kubernetes.parallel = p
        } else {
            this.script.echo 'cfg: stages: parallel: default'
            this.ag.parallel = p
        }

        this.parallel = p
    }
}
