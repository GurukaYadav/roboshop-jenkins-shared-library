def pipelineInit() {
  stage('Initiate Repo') {
//    sh 'rm -rf *'
    sh 'find . | sed -e "1d" | xargs rm -rf'
    git branch: 'main', url: "https://github.com/GurukaYadav/${COMPONENT}.git"
    if (env.BRANCH_NAME == env.TAG_NAME) {
      git checkout ${TAG_NAME}
    }
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
//  stage('Deploy artifacts to dev env') {
//    build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "${ENV}"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
//  }
//  stage('Run smoke Tests on dev env') {
//    echo 'Smoke tests ran'
//  }

  promoteRelease("dev", "qa")

  stage('Deploy artifacts to qa env') {
//    build job: 'deploy-to-any-env', parameters: [string(name: 'COMPONENT', value: "${COMPONENT}"), string(name: 'ENV', value: "qa"), string(name: 'APP_VERSION', value: "${TAG_NAME}")]
    echo "Deploy to QA env"
  }

  testRuns()

  stage('Run smoke Tests on qa env') {
    echo 'Smoke tests ran'
  }

  promoteRelease("qa", "prod")

}

def testRuns() {
  stage('testRuns') {
    parallel([
      integrationTests: {
        echo "Integration Tests"
      },
      e2eTests: {
        echo "End-to-End Tests"
      },
      penTests: {
        echo "Penetration Tests"
      }
    ])
  }
}

def promoteRelease(SOURCE_ENV,ENV) {
  stage("promoting artifacts from ${SOURCE_ENV} to ${ENV}") {
    withCredentials([usernamePassword(credentialsId: 'NEXUS3', passwordVariable: 'pass', usernameVariable: 'user_name')]) {
      sh """
          cp ${SOURCE_ENV}-${COMPONENT}-${TAG_NAME}.zip ${ENV}-${COMPONENT}-${TAG_NAME}.zip
          curl -v -u ${user_name}:${pass} --upload-file ${ENV}-${COMPONENT}-${TAG_NAME}.zip http://nexus.roboshop.internal:8081/repository/${COMPONENT}/${ENV}-${COMPONENT}-${TAG_NAME}.zip
      """
    }
  }
}

// The below functions are used for terraform immutable ami creation
def publishLocalArtifacts() {
  stage('prepare ami') {
    if (env.APP_TYPE == 'nodejs') {
      sh "zip -r ${COMPONENT}-${TAG_NAME}.zip server.js node_modules"
    }
    if (env.APP_TYPE == 'maven') {
      sh '''
        mv target/${COMPONENT}-1.0.jar ${COMPONENT}.jar
        zip -r ${COMPONENT}-${TAG_NAME}.zip ${COMPONENT}.jar
      '''
    }
    if (env.APP_TYPE == 'python') {
      sh '''
        zip -r ${COMPONENT}-${TAG_NAME}.zip *.py ${COMPONENT}.ini requirements.txt
      '''
    }
    if (env.APP_TYPE == 'nginx') {
      sh '''
        cd static
        zip -r ../${COMPONENT}-${TAG_NAME}.zip *
      '''
    }
    if (env.APP_TYPE == 'golang') {
      sh '''
        zip -r ${COMPONENT}-${TAG_NAME}.zip dispatch main.go 
      '''
    }
  }
}



def publishAMI() {
  stage('publish ami') {
    sh '''
      terraform init
      terraform apply -auto-approve  -var APP_VERSION=${TAG_NAME}
      terraform state rm 'module.immutable-app-ami.aws_ami_from_instance.ami'
      terraform destroy -auto-approve  -var APP_VERSION=${TAG_NAME}
    '''
  }
}