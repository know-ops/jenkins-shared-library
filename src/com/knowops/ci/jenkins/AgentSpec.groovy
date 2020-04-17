#!/usr/bin/env groovy
package com.knowops.ci.jenkins

/***
* TO-DO: throw exception if steps is assigned, with either stage or stages
* TO-DO: throw exception if more than one set of steps defined
***/

import groovy.lang.DelegatesTo

class AgentSpec implements Serializable {

    String label
    Boolean node = false

    KubernetesSpec kubernetes

    final Object script
    final Map<String,StagesSpec> aStages = [:]
    final Map<String,StageSpec> aStage = [:]
    final Map<String,Closure> exec = [:]

    AgentSpec(Object s) {
        this.script = s
    }

    void label(String l) {
        this.label = l
        this.node = true
    }

    void kubernetes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=KubernetesSpec) Closure<?> k8s) {
        this.kubernetes = new KubernetesSpec(this.script)

        if (k8s) {
            k8s.resolveStrategy = Closure.DELEGATE_FIRST
            k8s.delegate = this.kubernetes
            k8s()
        }
    }

    void steps(@DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> s) {
        s.resolveStrategy = Closure.DELEGATE_FIRST
        if (this.kubernetes) {
            this.kubernetes.steps(s)
        } else {
            s.delegate = this.script

            this.exec[''] = s
        }
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        stg.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.kubernetes.stage(name, stg)
        } else {
            this.aStage[name] = new StageSpec(this.script)
            stg.delegate = this.aStage[name]
            stg()

            this.exec[name] = this.aStage[name].&call
        }
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        stgs.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.kubrnetes.stages(name, stgs)
        } else {
            this.aStages[name] = new StagesSpec(this.script)
            stgs.delegate = this.aStages[name]
            stgs()

            this.exec[name] = this.aStage[name].&call
        }
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.aStages("Stages", stg)
    }

    void call() {
        if (this.kubernetes) {
            this.kubernetes()
        } else if (this.node) {
            if (this.label) {
                this.script.node(this.label) {
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
            } else {
                this.script.node {
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
