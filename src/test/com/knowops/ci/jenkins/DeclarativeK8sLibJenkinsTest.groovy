package com.knowops.ci.jenkins

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest

@TestInstance(Lifecycle.PER_CLASS)
class DeclarativeK8sLibJenkinsTest extends DeclarativePipelineTest {\
  @BeforeAll
  void setUp() {
    super.setUp()
  }

  @Test
  void should_execute_without_errors() throws Exception {
      def BRANCH_NAME = 'feature/testing_success'
      def script = runScript("./.jenkins/development.groovy")
      assertJobStatusSuccess()
      printCallStack()
  }

  @Test
  void should_not_execute() throws Exception {
      def BRANCH_NAME = 'master'
      def script = runScript("./.jenkins/development.groovy")
      printCallStack()
  }
}