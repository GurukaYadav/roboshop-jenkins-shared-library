def call() {
  node() {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
      ])
    ])
    ansiColor('xterm') {
//  Here the checkout(code clone) from git is automatically done in declarative pipelines but in scripted pipelines we need to specifically add a stage to clone the repo //
      stage('Code Checkout') {
        sh 'rm -rf *'
        sh 'git branch: \'main\', url: \'https://github.com/GurukaYadav/terraform-roboshop-mutable.git\''
      }
      stage('Terraform init') {
        sh 'terraform init'
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