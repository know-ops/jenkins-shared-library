#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import com.knowops.ci.jenkins.agent.AgentSpec

class BaseSpec implements Serializable {

    final Object script
    final AgentSpec ag

    BaseSpec(Object s) {
        this.script = s
        this.ag = new AgentSpec(this.script)
    }

    void agent(String a) {
        switch (s) {
            case 'any':
                this.ag.node = true

                break
            case 'none':
                this.ag.node = false

                break
            default:
                // TO-DO: Throw exception for incorrect value

                break
        }
    }

    void agent(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AgentSpec) Closure<?> a) {
        if (a) {
            a.resolveStrategy = Closure.DELEGATE_FIRST
            a.delegate = this.ag
            a()
        } else {
            this.ag.node = true
        }
    }

    void call() {
        this.script.echo "excuting agent for ${this.class}"
        this.ag.call()
    }

}
