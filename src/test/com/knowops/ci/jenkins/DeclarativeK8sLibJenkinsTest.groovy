package com.knowops.ci.jenkins

import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.ProjectSource.projectSource

import com.lesfurets.jenkins.unit.declarative.DeclarativePipelineTest
import org.junit.Before
import org.junit.Test
import static com.lesfurets.jenkins.unit.MethodCall.callArgsToString
import static org.junit.Assert.assertTrue
import static org.junit.Assert.assertFalse
import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull

class DeclarativeK8sLibJenkinsTest extends DeclarativePipelineTest {
  String sharedLibs = ''

  @Before
  void setUp() throws Exception {
    super.setUp() 

    binding.setVariable('env', env)
    binding.setVariable('scm', new String())

    def library = library().name('jenkins-shared-library')
                          .defaultVersion('<notNeeded>')
                          .allowOverride(true)
                          .implicit(false)
                          .targetPath('<notNeeded>')
                          .retriever(projectSource())
                          .build()
    
    helper.registerSharedLibrary(library)
  }

  @Test
  void should_execute_without_errors() throws Exception {
      def script = runScript("./src/test/resources/pipelines/declarativeK8sLibJenkins.groovy")
      assertJobStatusSuccess()
      printCallStack()
  }

  @Test
  void should_not_execute() throws Exception {
      def script = runScript("./src/test/resources/pipelines/declarativeK8sLibJenkins.groovy")
      printCallStack()
  }
}