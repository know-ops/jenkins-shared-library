#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.utils.YamlParser

class Project implements Serializable {

    String type = ''
    String stage = ''
    String dir = '.jenkins'
    Platform platform
    Map<String,Boolean> autodetect = [
        'language': false,
        'tool': false,
        'type': false,
    ]
    Object steps

    Project(String projectType, Object steps) {

        this.type = projectType

        this.steps = steps

    }

    Project(Object steps) {

        this.steps = steps

    }

    void autodetect(@DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> overrides) {

        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = this.autodetect
        overrides()

    }

    void platform(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Platform) Closure<?> overrides) {

        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = this.platform
        overrides()

    }

    void project(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Project) Closure<?> overrides) {

        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = this
        overrides()

    }

    void init(String stage) {

        YamlParser yaml = new YamlParser()

        Map<String,Object> core = yaml.load(this.steps.libraryResource('config/core.yaml'))

        this.platform = new Platform(core.platform, this.steps)

        if (core.spec.keySet().contains('autodetect')) {
            core.spec['autodetect'].each { k, v ->
                this.autodetect[k] = v
            }
        }

        if (core.keySet().contains('dir')) {
            this.dir = core.dir
        }

        this.steps.echo "${core}"
        this.steps.echo "${this.dir}"
        // this.platform.init(stage)

        // this.steps.setProperty('platform', this.platform)
        // this.steps.setProperty('project', this)

    }

    void methodMissing(String projectType, Closure overrides) {
        this.type = projectType
        this.project(overrides)
    }

}
