def call() {
  node() {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
      ])
    ])
    ansiColor('xterm') {
      stage('Terraform init') {
        sh 'terraform init -backend-config=env/${ENV}-backend.tfvars'
      }
      stage('Terraform plan') {
        sh 'terraform plan'
      }
      stage('Terraform apply') {
        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}