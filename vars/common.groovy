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
    if ( env.APP_TYPE == 'maven' ) {
      sh '''
        mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
        zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
      '''
    }
    if ( env.APP_TYPE == 'python' ) {
      sh '''
        zip -r ${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
      '''
    }
    if ( env.APP_TYPE == 'nginx' ) {
      sh '''
        cd static
        zip -r ../${COMPONENT}-${TAG_NAME}.zip *
      '''
    }
    if ( env.APP_TYPE == 'golang' ) {
      sh '''
        zip -r ${COMPONENT}-${TAG_NAME}.zip main.go dispatch
      '''
    }
  }
  stage('publish artifacts') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
      sh '''
        curl -v -u ${user_name}:${pass} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://nexus.devops65.online:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
    }
  }
}
