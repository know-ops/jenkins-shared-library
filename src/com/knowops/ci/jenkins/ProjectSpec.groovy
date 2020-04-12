#!/usr/bin/env groovy
package com.knowops.ci.jenkins

/**
 * Simple class to run a CI/CD pipeline
 */
class ProjectSpec implements Serializable {

    private String name
    private String repository
    private String language
    private String buildTool

    // This is the Jenkins steps object
    private final def steps

    ProjectSpec(def s) {
        this.steps = s
    }

    void setName(String n) {
        this.name = n
    }

    void setRepository(String r) {
        this.repository = r
    }

    void setLanguage(String l) {
        this.language = l
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

        return this.getRepository().replaceFirst(/^.*\/([^\/]+?).git$/, '$1')
    }

    /**
     * If set, returns the repository remote, otherwise returns the repository
     * remote from the current directory
     */
    String getRepository() {
        if (this.repository) {
            return this.repository
        }

        return this.steps.env.GIT_URL
    }

    /**
     * If set, returns the primary language used, otherwise tries to determine
     * based on the current directory
     */
    String getLanguage() {
        if (this.language) {
            return this.language
        }

        // TO-DO: attempt to auto-destect project type
        return ""
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

}
