pipeline {
  agent any

  stages {
    stage('Download Dependencies') {
      steps {
        sh 'npm install'
      }
    }

  }


}

//def call() {
//  node() {
//    stage('Download Dependencies') {
//      sh '''
//        ls -ltr
//        npm install
//      '''
//    }
//  }
//
//}