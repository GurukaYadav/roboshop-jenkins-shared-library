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
    withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user')]) {
      sh '''
        echo "$user:$pass" | base64
        curl -v -u "${user}":"${pass}" --upload-file ${COMPONENT}-${TAG_NAME}.zip http://44.197.226.33:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
    }
  }
}
