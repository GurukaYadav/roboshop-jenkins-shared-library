def call() {
  node() {

    stage('Download Dependencies') {
      sh '''
        rm -rf *
        git branch: 'main', url: 'https://github.com/GurukaYadav/cart.git\'
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

