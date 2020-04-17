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
            this.script.podTemplate(label: label) {
                this.script.node(label, this.exec)
            }
        } else {
            this.script.podTemplate {
                this.script.node(this.exec)
            }
        }
    }

}
