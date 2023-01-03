//def info(message) {
//    echo "INFO: ${message}"
//}
//
//def warning(message) {
//    echo "WARNING: ${message}"
//}

def call(CASTE) {

    pipeline {
        agent any

        stages {
            stage('avinash') {
                steps {
                    echo "Guruka Avinash ${CASTE}"
                }
            }
            stage('arun') {
                steps {
                    echo "Guruka Arun ${CASTE}"
                }
            }
        }

    }

}
