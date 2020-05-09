#!/usr/bin/env groovy
package com.knowops.ci.jenkins.utils

@Grab('org.yaml:snakeyaml:1.26')

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.SafeConstructor

class Yaml {

    private final Yaml parser

    Yaml() {
        parser = new Yaml(new SafeConstructor())
    }

    Map<String, Object> load(String yaml) {
        return parser.load(yaml)
    }

}
