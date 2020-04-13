#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class KubernetesSpec implements Serializable {

    private String label

    private final Object steps

    KubernetesSpec(Object s) {
        this.steps = s
    }

    void label(String l) {
        this.label = l
    }

    void call(Closure<?> exec) {
        if (label) {
            this.steps.podTemplate(label: label, exec)
        } else {
            this.steps.podTemplate(exec)
        }
    }

}
