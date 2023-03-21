def call() {
  env.EXTRA_OPTS=""
  node() {

    common.pipelineInit()
    if (env.BRANCH_NAME == env.TAG_NAME) {
      sh 'git checkout ${TAG_NAME}'
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








//pipeline {
//  agent any
//
//  stages {
//    stage('Download Dependencies') {
//      steps {
//        sh 'npm install'
//      }
//    }
//
//  }
//}

