#!/usr/bin/env groovy
package com.knowops.ci.jenkins.agent

class KubernetesSpec extends AgentSpec {

    KubernetesSpec(Object s) {
        super(s)
    }

    @Override
    void call() {
        if (this.node) {
            this.doPodTemplate()
        } else {
            this.doExec()
        }
    }

    void doPodTemplate() {
        if (this.label) {
            this.script.podTemplate(label: label) {
                this.doNode()
            }
        } else {
            this.script.podTemplate(label: label) {
                this.doNode()
            }
        }
    }

}
