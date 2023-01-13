def pipelineInit() {
  stage('Initiate Repo') {
    sh 'rm -rf *'
    git branch: 'main', url: "https://github.com/GurukaYadav/${COMPONENT}.git"
  }
}

def codeChecks() {
  stage('Quality Checks and Unit Tests') {
    parallel([
      qualityChecks: {
        withCredentials([usernamePassword(credentialsId: 'SONARQUBE', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
          sh "sonar-scanner -Dsonar.projectKey=${COMPONENT}  -Dsonar.host.url=http://sonarqube.roboshop.internal:9000 -Dsonar.login=${user_name} -Dsonar.password=${pass}"
          sh "sonar-quality-gate.sh ${user_name} ${user_name} sonarqube.roboshop.internal ${COMPONENT}"
        }
      },
      unitTests: {
        unitTests()
      }
    ])
  }
}

def unitTests() {
  stage('Unit Tests') {
    if ( env.APP_TYPE == 'nodejs' ) {
      sh '''
        # npm run test
        echo "run test cases"
      '''
    }
    if ( env.APP_TYPE == 'maven' ) {
      sh '''
        # mvn test
        echo "run test cases"
      '''
    }
    if ( env.APP_TYPE == 'python' ) {
      sh '''
        # mvn test
        echo "run test cases"
      '''
    }
    if ( env.APP_TYPE == 'nginx' ) {
      sh '''
        # python -m unittest
        echo "run test cases"
      '''
    }
    if ( env.APP_TYPE == 'golang' ) {
      sh '''
        # go test
        echo "run test cases"
      '''
    }

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
        zip -r ${COMPONENT}-${TAG_NAME}.zip dispatch main.go 
      '''
    }
  }
  stage('publish artifacts') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
      sh '''
        curl -v -u ${user_name}:${pass} --upload-file ${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${COMPONENT}-${TAG_NAME}.zip
      '''
    }
  }
}


