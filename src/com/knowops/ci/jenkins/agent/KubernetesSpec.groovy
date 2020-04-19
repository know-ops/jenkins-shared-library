#!/usr/bin/env groovy
package com.knowops.ci.jenkins.agent

class KubernetesSpec extends AgentSpec {

    Boolean node = false

    KubernetesSpec(Object s) {
        super(s)
    }

    KubernetesSpec(String p, Object s) {
        super(p, s)
    }

    @Override
    void label(String l) {
        this.doLabel(l)
    }

    @Override
    void steps(Closure<?> s) {
        this.doSteps(s)
    }

    @Override
    void stage(String name, Closure<?> stg) {
        this.doStage(name, stg)
    }

    @Override
    void stages(String name, Closure<?> stgs) {
        this.doStages(name, stgs)
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

    @Override
    Boolean getNode() {
        return this.node
    }

    @Override
    void setNode(Boolean node) {
        this.node = node
    }

}
