def call() {
  node() {

    common.pipelineInit()

    stage('Download Dependencies') {
      sh '''
        ls -ltr
        npm install
      '''
    }

    stage('Quality Checks and Unit Tests') {
      parallel([
        qualityChecks: {
          echo "Guruka Avinash Yadav"
        },
        unitTests: {
          echo "Avinash"
        }
      ])
    }

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

