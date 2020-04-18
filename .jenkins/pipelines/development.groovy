#!/usr/bin/env groovy
library "jenkins-shared-library@$BRANCH_NAME"

printEnvironment {
  name = 'Jenkins Shared Pipeline'
}
