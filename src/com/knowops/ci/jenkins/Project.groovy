#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.utils.YamlParser

class Project implements Serializable {

    String type = ''
    String phase = ''
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

    void init(String phase) {

        YamlParser yaml = new YamlParser()

        Map<String,Object> proj = yaml.load(this.steps.libraryResource('config/project.yaml'))

        if (this.type) {
            this.platform = new Platform(proj[this.type].platform[phase])
        } else {
            this.platform = new Platform(proj.project.platform[phase], this.steps)
        }

        if (proj.keySet().contains('autodetect')) {
            proj.autodetect.each { k, v ->
                this.autodetect[k] = v
            }
        }

        if (proj.keySet().contains('dir')) {
            this.dir = proj.dir
        }

        this.platform.init(phase)
        this.steps.echo "${proj}"
        this.steps.echo "${this.platform.nodes}"

        this.steps.setProperty('platform', this.platform)
        // this.steps.setProperty('project', this)

    }

    void methodMissing(String projectType, Closure overrides) {
        this.type = projectType
        this.project(overrides)
    }

}
