def call() {
  node() {

    common.pipelineInit()

    stage('Compile and Package') {
      sh 'mvn clean package'
    }
  }
}

