#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class KubernetesSpec extends AgentSpec {

    KubernetesSpec(Object s) {
        super(s)
        this.node = true
    }

    @Override
    void call() {
        if (label) {
            this.steps.podTemplate(label: label) {
                this.steps.node(label, this.exec)
            }
        } else {
            this.steps.podTemplate {
                this.steps.node(this.exec)
            }
        }
    }

}
