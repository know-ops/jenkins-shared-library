#!/usr/bin/env groovy
package com.knowops.ci.jenkins.agent

/***
* TO-DO: throw exception if steps is assigned, with either stage or stages
* TO-DO: throw exception if more than one set of steps defined
***/

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.core.StagesSpec
import com.knowops.ci.jenkins.core.StageSpec

class AgentSpec implements Serializable {

    String label
    Boolean node = false
    Boolean parallel = false

    KubernetesSpec kubernetes

    final Object script
    final String platform
    final Map<String,StagesSpec> stages = [:]
    final Map<String,StageSpec> stage = [:]
    final Map<String,Closure> exec = [:]

    AgentSpec(Object s) {
        this.init(s)
    }

    AgentSpec(String p, Object s) {
        this.init(s)
        this.platform = p
    }

    void label(String l) {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes.label(l)

                break
            default:
                this.doLabel(l)

                break
        }
    }

    void steps(Closure<?> s) {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes.steps(s)

                break
            default:
                this.doSteps(s)

                break
        }
    }

    void stage(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StageSpec) Closure<?> stg) {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes.stage(name, stg)

                break
            default:
                this.doStage(name, stg)

                break
        }
    }

    void stages(String name, Closure<?> stgs) {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes.stages(name, stgs)

                break
            default:
                this.doStages(name, stgs)

                break
        }
    }

    void stages(Closure<?> stgs) {
        this.stages('', stg)
    }

    void call() {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes()

                break
            default:
                if (this.node) {
                    this.doNode()
                } else {
                    this.doExec()
                }

                break
        }
    }

    @NonCPS
    private void init(Object s) {
        this.script = s

        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes = new KubernetesSpec(s)
                break
        }

    }

    void doLabel(l) {
        this.label = l
        this.node = true
    }

    void doSteps(@DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> s) {
        s.resolveStrategy = Closure.DELEGATE_FIRST
        s.delegate = this.script

        this.exec[''] = s
    }

    void doStage() {
        stg.resolveStrategy = Closure.DELEGATE_FIRST

        this.stage[name] = new StageSpec(this.script)

        stg.delegate = this.stage[name]
        stg()

        this.exec[name] = this.stage[name].&call
    }

    void doStages(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=StagesSpec) Closure<?> stgs) {
        stgs.resolveStrategy = Closure.DELEGATE_FIRST

        this.stages[name] = new StagesSpec(this.script)

        stgs.delegate = this.stages[name]
        stgs()

        this.exec[name] = this.stages[name].&call
    }

    void doNode() {
        if (this.label) {
            this.script.node(this.label) {
                this.doExec()
            }
        } else {
            this.script.echo 'exec: starting: node'
            this.script.node {
                this.doExec()
            }
        }
    }

    void doExec() {
        if (this.parallel) {
            this.script.parallel(this.exec)
        } else {
            this.exec.each { name, task ->
                if (name != '') {
                    this.script.stage(name) {
                        task()
                    }
                } else {
                    task()
                }
            }
        }
    }

    Boolean getNode() {
        switch (this.platform) {
            case 'kubernetes':
                return this.kubernetes.node

            default:
                return this.node

        }
    }


    void setNode(Boolean node) {
        switch (this.platform) {
            case 'kubernetes':
                this.kubernetes.node = node

                break
            default:
                this.node = node

                break
        }
    }

}
