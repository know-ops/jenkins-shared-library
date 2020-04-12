#!/usr/bin/env groovy
library "jenkins-shared-library@$BRANCH_NAME"

workflow {
    project {
        name = 'Jenkins Shared Library'
    }
}
