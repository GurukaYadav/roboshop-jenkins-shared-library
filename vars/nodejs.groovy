def call() {
  node() {

    stage('Download Dependencies') {
      sh '''
          rm -rf *
          ls -ltr
      '''
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

