#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class BaseSpec implements Serializable {

    private final Object steps
    private final AgentSpec agent

    BaseSpec(Object s) {
        this.steps = s
        this.agent = new AgentSpec(this.steps)
    }

    void agent(String a) {
        switch (s) {
            case 'any':
                this.agent.node = true

                break
            case 'none':
                this.agent.node = false

                break
            default:
                // TO-DO: Throw exception for incorrect value

                break
        }
    }

    void agent(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=AgentSpec) Closure<?> a) {
        if (a) {
            a.resolveStrategy = Closure.DELEGATE_FIRST
            a.delegate = this.agent
            a()
        } else {
            this.agent.node = true
        }
    }

    void call() {
        this.agent()
    }

    void getAgent() {
        this.agent
    }
}
