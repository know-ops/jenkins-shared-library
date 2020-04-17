#!/usr/bin/env groovy
package com.knowops.ci.jenkins

class KubernetesSpec extends AgentSpec {

    KubernetesSpec(Object s) {
        super(s)
        this.node = true
    }

    @Override
    void call() {
        if (this.node) {
            this.script.echo 'exec: starting: node'
            if (this.label) {
                this.script.echo "exec: starting: node: ${this.label}"
                this.script.podTemplate(label: label) {
                    this.script.node(this.label) {
                        this.exec.each { name, task ->
                            if (name != '' ) {
                                this.script.stage(name) {
                                    task()
                                }
                            } else {
                                task()
                            }
                        }
                    }
                }
            } else {
                this.script.podTemplate(label: label) {
                    this.script.node {
                        this.script.echo 'exec: starting: node'
                        this.exec.each { name, task ->
                            if (name) {
                                this.script.stage(name) {
                                    task()
                                }
                            } else {
                                task()
                            }
                        }
                    }
                }
            }
        } else {
            this.exec.each { name, task ->
                if (name) {
                    this.script.stage(name) {
                        task()
                    }
                } else {
                    task()
                }
            }
        }
    }
}
