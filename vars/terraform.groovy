def call() {
  node() {
    parameters {
      choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV")
    }
    ansiColor('xterm') {
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
}