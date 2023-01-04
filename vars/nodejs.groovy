def call() {
  node() {

    common.pipelineInit()

    stage('Download Dependencies') {
      sh 'npm install'
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

