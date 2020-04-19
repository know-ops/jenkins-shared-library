#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import groovy.lang.DelegatesTo

class StagesSpec extends BaseSpec {

    StagesSpec(Object s) {
        super(s)
    }

    StagesSpec(String, p, Object s) {
        super(p, s)
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.ag.stage(name, stg)
    }

    void parallel(Boolean p) {
        if (this.ag.kubernetes) {
            this.ag.kubernetes.parallel = p
        } else {
            this.ag.parallel = p
        }
    }

}
