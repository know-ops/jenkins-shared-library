#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec extends BaseSpec {

    StagesSpec(Object s) {
        super(s)
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.ag.stage(name, stg)
    }

    Boolean getParallel() {
        if (this.ag.kubernetes) {
            return this.ag.kubernetes.parallel
        } else {
            return this.ag.parallel
        }
    }

    void setParallel(Boolean p) {
        if (this.ag.kubernetes) {
            this.ag.kubernetes.parallel = true
        } else {
            this.ag.parallel = true
        }
    }
}
