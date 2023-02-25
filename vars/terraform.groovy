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
      stage('Terraform plan -var-file=env/${ENV}.tfvars') {
        sh 'terraform plan'
      }
      stage('Terraform apply') {
        sh 'terraform apply -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}