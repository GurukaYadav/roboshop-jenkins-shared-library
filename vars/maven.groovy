def call() {
  env.EXTRA_OPTS="-Dsonar.java.binaries=./target"
  node() {
    ansiColor('xterm') {
      
    common.pipelineInit()

    stage('Build Package') {
      sh 'mvn clean package'
    }

    common.codeChecks()

    if ( env.BRANCH_NAME == env.TAG_NAME )
    {
//      common.publishArtifacts()
//      The below is used for immutable ami creation
      common.publishLocalArtifacts()

//      The below one is used for immutable ami creation
      common.publishAMI()
    }
    }
  }
}

