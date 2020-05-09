#!/usr/bin/env groovy
package com.knowops.ci.jenkins

import groovy.lang.DelegatesTo

import com.knowops.ci.jenkins.utils.YamlParser

class Platform {

    String name
    Object steps

    Map<String,String> agent = [:]
    Map<String,List> nodes = [:]

    Platform(String name, Object steps) {
        this.name = name
        this.steps = steps
    }


    // void agent(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Agent) Closure<?> overrides) {

    //     overrides.resolveStrategy = Closure.DELEGATE_FIRST
    //     overrides.delegate = this.agent
    //     overrides()

    // }

    // void nodes(@DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Nodes) Closure<?> overrides) {

    //     overrides.resolveStrategy = Closure.DELEGATE_FIRST
    //     overrides.delegate = this.nodes
    //     overrides()

    // }

    void methodMissing(String name, @DelegatesTo(strategy=Closure.DELEGATE_FIRST, value=Platform) Closure<?> platform) {

        this.name = name

        platform.resolveStrategy = Closure.DELEGATE_FIRST
        platform.delegate = this
        platform()

    }

    void init(String stage) {

        YamlParser yaml = new YamlParser()

        Map<String,Object> platforms = yaml.load(this.steps.libraryResource('config/platforms.yaml'))

        this.agent = platforms[this.name]['agent']
        this.nodes = platforms[this.name]['nodes'][stage]

    }

}
