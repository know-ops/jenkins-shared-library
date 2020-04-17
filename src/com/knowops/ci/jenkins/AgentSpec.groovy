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
        s.echo 'init: agent'
        this.script = s
    }

    void label(String l) {
        this.script.echo "cfg: agent: ${label}"
        this.label = l
        this.node = true
        this.script.echo "cfg: agent: ${label}"
    }

    void kubernetes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=KubernetesSpec) Closure<?> k8s) {
        this.script.echo "cfg: agent: kubernetes"
        this.kubernetes = new KubernetesSpec(this.script)

        if (k8s) {
            k8s.resolveStrategy = Closure.DELEGATE_FIRST
            k8s.delegate = this.kubernetes
            k8s()
        }
        this.script.echo "cfg: agent: kubernetes: ${this.kubernetes.label}"
    }

    void steps(@DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> s) {
        this.script.echo 'cfg: agent: steps'
        s.resolveStrategy = Closure.DELEGATE_FIRST
        
        if (this.kubernetes) {
            this.kubernetes.steps(s)
        } else {
            s.delegate = this.script

            this.exec[''] = s
        }
        this.script.echo 'cfg: agent: steps'
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        this.script.echo "cfg: agent: stage: ${name}"
        stg.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.script.echo "cfg: agent: stage: kubernetes"
            this.kubernetes.stage(name, stg)
            this.script.echo "cfg: agent: stage: kubernetes"
        } else {
            this.script.echo "cfg: agent: stage: default"
            this.aStage[name] = new StageSpec(this.script)
            stg.delegate = this.aStage[name]
            stg()

            this.exec[name] = this.aStage[name].&call
            this.script.echo "cfg: agent: stage: default"
        }
        this.script.echo 'cfg: agent: stage'
    }

    void stages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.script.echo 'cfg: agent: stages'
        stgs.resolveStrategy = Closure.DELEGATE_FIRST

        if (this.kubernetes) {
            this.script.echo 'cfg: agent: stages: kubernetes'
            this.kubernetes.stages(name, stgs)
            this.script.echo 'cfg: agent: stages: kubernetes'
        } else {
            this.script.echo 'cfg: agent: stages: default'
            this.aStages[name] = new StagesSpec(this.script)
            stgs.delegate = this.aStages[name]
            stgs()

            this.exec[name] = this.aStages[name].&call
            this.script.echo 'cfg: agent: stages: default'
        }
        this.script.echo 'cfg: agent: stages'
    }

    void stages(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        this.aStages('', stg)
    }

    void call() {
        this.script.echo 'exec: starting'
        if (this.kubernetes) {
            this.script.echo 'exec: starting: kubernetes'
            this.kubernetes.call()
            this.script.echo 'exec: starting: kubernetes'
        } else if (this.node) {
            this.script.echo 'exec: starting: node'
            if (this.label) {
                this.script.echo "exec: starting: node: ${this.label}"
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
            } else {
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
