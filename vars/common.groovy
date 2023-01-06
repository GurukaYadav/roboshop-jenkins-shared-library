def pipelineInit() {
  stage('Initiate Repo') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/GurukaYadav/${COMPONENT}.git"
  }
}

def publishArtifacts() {
  stage('prepare artifacts') {
    if ( env.APP_TYPE == 'nodejs' ) {
      sh "zip -r ${COMPONENT}-${TAG_NAME}.zip server.js node_modules"
    }
  }
  stage('publish artifacts') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS', passwordVariable: 'pass', usernameVariable: 'user')]) {
      sh '''
        curl -v -u ${user}:${pass} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://3.238.2.54:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
    }
  }
}
