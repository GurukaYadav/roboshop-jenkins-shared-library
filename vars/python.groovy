def call() {
  node() {

    common.pipelineInit()

    common.codeChecks()


    if ( env.BRANCH_NAME == env.TAG_NAME )
    {
      common.publishArtifacts()
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

