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
//        withCredentials([usernamePassword(credentialsId: 'SONARQUBE', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
//          sh "sonar-scanner -Dsonar.projectKey=${COMPONENT}  -Dsonar.host.url=http://sonarqube.roboshop.internal:9000 -Dsonar.login=${user_name} -Dsonar.password=${pass} ${EXTRA_OPTS}"
//          sh "sonar-quality-gate.sh ${user_name} ${pass} sonarqube.roboshop.internal ${COMPONENT}"
//        }
        echo "Code Quality Checks"
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
  env.ENV = "dev"
  stage('prepare artifacts') {
    if ( env.APP_TYPE == 'nodejs' ) {
      sh "zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip server.js node_modules"
    }
    if ( env.APP_TYPE == 'maven' ) {
      sh '''
        mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
      '''
    }
    if ( env.APP_TYPE == 'python' ) {
      sh '''
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
      '''
    }
    if ( env.APP_TYPE == 'nginx' ) {
      sh '''
        cd static
        zip -r ../${ENV}-${COMPONENT}-${TAG_NAME}.zip *
      '''
    }
    if ( env.APP_TYPE == 'golang' ) {
      sh '''
        zip -r ${ENV}-${COMPONENT}-${TAG_NAME}.zip dispatch main.go 
      '''
    }
  }
  stage('publish artifacts') {
    withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
      sh '''
        curl -v -u ${user_name}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
      '''
    }
  }
  stage('Deploy artifacts to dev env') {
    build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "${ENV}"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
  }
//  stage('Run smoke Tests') {
//    echo 'Smoke tests ran'
//  }
//  stage('mark QA release') {
//    promoteRelease("dev", "qa")
//  }
//  stage('Deploy artifacts to qa env') {
//    build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "qa"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
//  }
}

def promoteRelease(SOURCE_ENV,ENV) {
  withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
    sh """
        cp ${SOURCE_ENV}-${COMPONENT}-${TAG_NAME}.zip ${ENV}-${COMPONENT}-${TAG_NAME}.zip
        curl -v -u ${user_name}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
    """
  }
}
