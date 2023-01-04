def call() {
  node() {

    stage('Download Dependencies') {
      sh '''
          ls -ltr
          npm install
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

