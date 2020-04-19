#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import com.knowops.ci.jenkins.agent.AgentSpec

class BaseSpec implements Serializable {

    final ProjectSpec project
    final Object script
    final AgentSpec ag

    BaseSpec(Object s) {
        this.script = s
        this.ag = new AgentSpec(this.script)
    }

    BaseSpec(String p, Object s) {
        this.script = s
        this.ag = new AgentSpec(p, this.script)
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
        this.script.echo "${this.class}.call"
        this.ag.call()
    }

}
