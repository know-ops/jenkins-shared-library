#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StagesSpec extends BaseSpec {

    Boolean parallel = false

    StagesSpec(Object s) {
        super(s)
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        if (this.parallel) {
            this.ag.parallel = true
        }
        this.ag.stage(name, stg)
    }

}
