#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class KubernetesSpec {

    private String label

    private final Object steps

    KubernetesSpec(Object s) {
        this.steps = s
    }

    void label(String l) {
        this.label = l
    }

}