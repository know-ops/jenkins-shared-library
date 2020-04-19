#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import groovy.lang.DelegatesTo

class StageSpec extends BaseSpec {

    StageSpec(Object s) {
        super(s)
    }

    StageSpec(String p, Object s) {
        super(p, s)
    }

    void steps(Closure<?> s) {
        this.ag.steps(s)
    }

}
