#!/usr/bin/env groovy
package com.knowops.ci.jenkins.core

import groovy.json.JsonSlurper
import groovy.json.JsonParserType

/**
 * Simple class to run a CI/CD pipeline
 */
class ProjectSpec extends BaseSpec {

    private String name
    private String repository
    private ArrayList<String> language
    private String buildTool
    private Boolean autodetect = true

    ProjectSpec(Object s) {
        super(s)
    }

    ProjectSpec(String p, Object s) {
        super(p, s)
    }

    void setName(String n) {
        this.name = n

        this.script.env.setProperty('PROJECT_NAME', this.name)
    }

    void setRepository(String r) {
        this.repository = r

        this.script.env.setProperty('PROJECT_REPOSITORY', this.repository)
    }

    void setLanguage(ArrayList<String> l) {
        this.language = l

        this.script.env.setProperty('PROJECT_LANGUAGE', this.language.join(' '))
    }

    void setBuildTool(String b) {
        this.buildTool = b
    }

    /**
     * If set, returns the project name, otherwise returns the repository name
     * from the current directory
     */
    String getName() {
        if (this.name) {
            return this.name
        }

        this.name = this.getRepository().replaceFirst(/^.*\/([^\/]+?).git$/, '$1')

        return this.name
    }

    /**
     * If set, returns the repository remote, otherwise returns the repository
     * remote from the current directory
     */
    String getRepository() {
        if (this.repository) {
            return this.repository
        }

        this.repository = this.script.scm.userRemoteConfigs[0].url

        return this.repository
    }

    /**
     * If set, returns the primary language used, otherwise tries to determine
     * based on the current directory
     */
    ArrayList<String> getLanguage() {
        if (this.language) {
            return this.language
        }

        return []
    }

    /**
     * If set, returns the buildTool to use, otherwise, attempts to determine which
     * buildTool should be used.
     */
    String getBuildTool() {
        if (this.buildTool) {
            return this.buildTool
        }

        // TO-DO: attempt to auto-detect project build tool, i.e. mvn, gradle,
        // npm, etc.
        return ""
    }

    ArrayList<String> doLanguage() {
        return this.parseJson(
            this.script.sh(script: 'github-linguist --json', returnStdout: true)
        ).keySet() as ArrayList
    }

    @NonCPS
    Object parseJson(String txt) {
        JsonSlurper jsonSlurper = new JsonSlurper().setType(JsonParserType.LAX)

        return jsonSlurper.parseText(txt)
    }

    void initStages() {
        this.ag.stage('Project: Checkout') {
            steps {
                checkout(scm).each { k, v ->
                    env.setProperty(k, v)
                }
            }
        }

        switch (this.ag.platform) {
            case 'kubernetes':
                this.ag.label('k8s-project-agent')
                if (this.autodetect) {
                    this.ag.stages('Project: Autodetect') {
                        parallel true

                        stage('Language') {
                            steps {
                                container('linguist') {
                                    String languageJson = sh (
                                        returnStdout: true,
                                        script: 'github-linguist --json'
                                    )

                                    project.language = project.parseJson(languageJson).keySet() as ArrayList

                                    env.setProperty('PROJECT_LANGUAGE', project.language.join(' '))

                                    echo "${PROJECT_LANGUAGE}"
                                }
                            }
                        }
                    }
                }

                break
        }
    }

}
