#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class BaseSpec implements Serializable {

    final Object steps
    final AgentSpec ag

    BaseSpec(Object s) {
        this.steps = s
        this.ag = new AgentSpec(this.steps)
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
        this.ag()
    }

}
