def call() {
  node() {

    stage('Download Dependencies') {
      steps {
        sh '''
          ls -ltr
          npm install
        '''
      }
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

