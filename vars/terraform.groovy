def call() {
  node() {
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