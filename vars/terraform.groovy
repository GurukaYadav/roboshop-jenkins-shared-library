def call() {
  node() {
    options {
      ansiColor('xterm')
    }
    parameters {
      choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV")
    }
    stage('Terraform init') {
      sh 'terraform init'
    }
    stage('Terraform plan') {
      sh 'terraform plan'
    }
    stage('Terraform apply') {
      sh 'terraform apply -auto-approve'
    }
  }
}