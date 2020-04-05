#!/usr/bin/env groovy

def call(Map opt = [:]) {
    String name = opt.get('name','k8s-agen')
    String defaultLabel = "${name.replace('+','_')}-${UUID.randomUUID().toString()}"
    String label = opt.get('label', defaultLabel)
    String cloud = opt.get('cloud', 'kubernetes')
    def retVal = [:]

    printf label
    printf cloud

    retVal['cloud'] = cloud
    retVal['label'] = label

    return retVal
}

