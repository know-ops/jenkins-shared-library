#!/usr/bin/env groovy
package com.knowops.ci.jenkins

/***
* TO-DO: throw exception if steps is assigned, with either stage or stages
* TO-DO: throw exception if more than one set of steps defined
***/

import groovy.lang.DelegatesTo

class AgentSpec implements Serializable {

    private String label
    private KubernetesSpec kubernetes

    private Boolean node = false

    private final Object steps
    private final Map<String,StagesSpec> stages = [:]
    private final Map<String,StageSpec> stage = [:]
    private final Map<String,Closure> exec = [:]

    AgentSpec(Object s) {
        this.steps = s
    }

    void label(String l) {
        this.label = l
        this.node = true
    }

    void kubernetes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=KubernetesSpec) Closure<?> k8s) {
        this.kubernetes = new KubernetesSpec(this.steps)

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
            s.delegate = this.steps

            this.exec[""] = s
        }
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        stg.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.kubernetes.stage(name, sg)
        } else {
            this.stage[name] = new StageSpec(this.steps)
            stg.delegate = this.stage[name]
            stg()

            this.exec[name] = this.stage[name].&call
        }
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        stgs.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.kubrnetes.stages(name, stgs)
        } else {
            this.stages[name] = new StagesSpec(this.steps)
            stgs.delegate = this.stages[name]
            stgs()

            this.exec[name] = this.stage[name].&call
        }
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.stages("Stages", stg)
    }

    void call() {
        if (this.kubernetes) {
            this.kubernetes()
        } else if (this.node) {
            if (this.label) {
                this.steps.node(this.label) {
                    this.exec.each { name, task ->
                        if (name) {
                            this.steps.stage(name) {
                                task()
                            }
                        } else {
                            task()
                        }
                    }
                }
            } else {
                this.steps.node {
                    this.exec.each { name, task ->
                        if (name) {
                            this.steps.stage(name) {
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
                    this.steps.stage(name) {
                        task()
                    }
                } else {
                    task()
                }
            }
        }
    }

    void setNode(Boolean n) {
        this.node = n
    }

}
