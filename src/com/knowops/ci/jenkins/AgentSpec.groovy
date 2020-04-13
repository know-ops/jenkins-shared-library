#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

class AgentSpec implements Serializable {

    private String label
    private KubernetesSpec kubernetes

    private final Object steps

    AgentSpec(Object s) {
        this.steps = s
    }

    void label(String l) {
        this.label = l
    }

    void kubernetes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=KubernetesSpec) Closure<?> k8s) {
        this.kubernetes = new KubernetesSpec(this.steps)

        if (k8s) {
            k8s.resolveStrategy = Closure.DELEGATE_FIRST
            k8s.delegate = this.kubernetes
            k8s()
        }
    }

    void call(Closure<?> exec) {
        if (kubernetes) {
            this.kubernetes(exec)
        } else if (label) {
            this.steps.node(label, exec)
        } else {
            this.steps.node(exec)
        }
    }
}
