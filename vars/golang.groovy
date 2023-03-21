def call() {
  env.EXTRA_OPTS=""
  node() {
    ansiColor('xterm') {

      common.pipelineInit()

      stage('Install dependencies and Build Package') {
        sh '''go mod init dispatch
            go get
            go build
      '''
      }

      common.codeChecks()

      if (env.BRANCH_NAME == env.TAG_NAME) {
//      common.publishArtifacts()
//      The below is used for immutable ami creation
        common.publishLocalArtifacts()

//      The below one is used for immutable ami creation
        common.publishAMI()
      }

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
