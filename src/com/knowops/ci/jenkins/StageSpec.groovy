#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class StageSpec extends BaseSpec {

    StageSpec(Object s) {
        super(s)
    }

    void steps(Closure<?> s) {
        s.resolveStrategy = Closure.DELEGATE_FIRST
        s.delegate = this.steps

        this.stepsClosure = s
    }

}
