def call() {
  node() {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
      ])
    ])
    ansiColor('xterm') {
      stage('Terraform init') {
        sh 'terraform init'
      }
      stage('Terraform plan') {
        sh 'terraform plan -var-file=env/${ENV}.tfvars'
      }
      stage('Terraform apply') {
        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}