package com.knowops.ci.jenkins

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest

@TestInstance(Lifecycle.PER_CLASS)
class DeclarativeK8sLibJenkinsTest extends DeclarativePipelineTest {
  String sharedLibs = ''

  @BeforeAll
  void setUp() {
    super.setUp()

    def library = library().name('jenkins-shared-library')
                          .defaultVersion('<notNeeded>')
                          .allowOverride(true)
                          .implicit(false)
                          .targetPath('<notNeeded>')
                          .retrriever(projectSource())
                          .build()
    
    helper.registerSharedLibrary(library)
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