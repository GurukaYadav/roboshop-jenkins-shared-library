//def info(message) {
//    echo "INFO: ${message}"
//}
//
//def warning(message) {
//    echo "WARNING: ${message}"
//}

//def call(COMPONENT) {
//
//    pipeline {
//        agent any
//
//        stages {
//            stage('avinash') {
//                steps {
//                    echo "Guruka Avinash"
//                }
//            }
//            stage('arun') {
//                steps {
//                    echo "Guruka Arun"
//                }
//            }
//        }
//
//    }
//
//}

//The above is not great way of doing the things

//def call() {
//  pipeline {
//    agent any
//
//      stages {
//        stage('avinash') {
//          steps {
//            sh "echo Guruka Avinash-${COMPONENT}"
//          }
//        }
//        stage('arun}') {
//          steps {
//            sh "echo Guruka Arun-${COMPONENT}"
//          }
//        }
//      }
//
//  }
//}

def call() {
  node() {
    stage('avinash-${COMPONENT}') {
      sh "echo Guruka Avinash Yadav-${COMPONENT}"
    }
    stage('arun-${COMPONENT}') {
      sh "echo Guruka Arun Yadav-${COMPONENT}"
    }
  }

}