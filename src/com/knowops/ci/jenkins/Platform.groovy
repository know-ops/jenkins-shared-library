#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.utils.YamlParser

class Platform {

    String name
    Object steps

    Map<String,List> nodes = [:]

    Platform(String name, Object steps) {
        this.name = name
        this.steps = steps
    }


    void nodes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST) Closure<?> overrides) {

        overrides.resolveStrategy = Closure.DELEGATE_FIRST
        overrides.delegate = this.nodes
        overrides()

    }

    void methodMissing(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Platform) Closure<?> platform) {

        this.name = name

        platform.resolveStrategy = Closure.DELEGATE_FIRST
        platform.delegate = this
        platform()

    }

    void init(String phase) {

        YamlParser yaml = new YamlParser()

        Map<String,Object> plat = yaml.load(this.steps.libraryResource('config/platform.yaml'))

        this.nodes = plat[this.name][phase]

    }

}
