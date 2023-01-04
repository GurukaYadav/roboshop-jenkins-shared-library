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
//
//
//}

def call() {
  node() {

    stage('Download Dependencies') {
      steps {
        sh '''
          git branch: 'main', url: 'https://github.com/GurukaYadav/cart.git'
          ls -l
          npm install
        '''
      }
    }
  }
}