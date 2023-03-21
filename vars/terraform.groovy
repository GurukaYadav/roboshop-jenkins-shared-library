def call() {
  node() {
    properties([
      parameters([
        choice(choices: ['dev', 'prod'], description: "Choose Environment", name: "ENV"),
        choice(choices: ['apply', 'destroy'], description: "Choose Action", name: "ACTION"),
      ])
    ])
    ansiColor('xterm') {
//  Here the checkout(code clone) from git is automatically done in declarative pipelines but in scripted pipelines we need to specifically add a stage to clone the repo //
      stage('Code Checkout') {

        sh 'find . | sed -e "1d" | xargs rm -rf'
        git branch: 'main', url:'https://github.com/GurukaYadav/${REPO_NAME}.git'
      }
      stage('Terraform init') {
        sh 'terraform init  -backend-config=env/${ENV}-backend.tfvars'
      }
      stage('Terraform plan') {
        sh 'terraform plan -var-file=env/${ENV}.tfvars'
      }
      stage("Terraform ${ACTION}") {
        sh 'terraform ${ACTION} -auto-approve -var-file=env/${ENV}.tfvars'
      }
    }
  }
}