#!/usr/bin/env groovy
library "jenkins-shared-library@$BRANCH_NAME"

printEnvironment {
    setName 'Jenkins Shared Library'
}
