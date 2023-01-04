def call() {
  node() {

    common.pipelineInit()

    stage('Install dependencies and Build Package') {
      sh '''go mod init dispatch
            go get
            go build
      '''
    }
  }
}





// pipeline {
//   agent any
//
//   stages {
//     stage('Install dependencies, Compile and Package') {
//       steps {
//         sh '''go mod init dispatch
//               go get
//               go build
//         '''
//       }
//     }
//
//   }
//
// }
//Here Install dependencies, Compile and Package becomes Install dependencies and build(compile)package and not packaging the code
