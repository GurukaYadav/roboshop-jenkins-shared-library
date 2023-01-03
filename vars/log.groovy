//def info(message) {
//    echo "INFO: ${message}"
//}
//
//def warning(message) {
//    echo "WARNING: ${message}"
//}

def call(COMPONENT) {

    pipeline {
        agent any

        stages {
            stage('avinash') {
                steps {
                    echo "Guruka Avinash-${COMPONENT}"
                }
            }
            stage('arun') {
                steps {
                    echo "Guruka Arun-${COMPONENT}"
                }
            }
        }

    }

}

//The above is not great way of doing the things
