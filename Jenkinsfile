pipeline {
    agent any
    environment {
        DOCKERHUB_USER = 'naceurbrahem'
        DOCKERHUB_CRED_ID = 'dockerhub-login'
        SSH_CRED_ID = 'vmware-ssh-key'
        VM_IP = '192.168.1.95'
        VM_USER = 'NaceurBrahem'
    }
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        stage('Build WAR (Maven)') {
            steps {
                // On utilise mvn package au lieu de gradlew
                sh 'mvn clean package'
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    sh "docker build -t ${DOCKERHUB_USER}/java-app:latest ."
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CRED_ID, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "echo \$PASS | docker login -u \$USER --password-stdin"
                        sh "docker push ${DOCKERHUB_USER}/java-app:latest"
                    }
                }
            }
        }
        stage('Deploy to VMware') {
            steps {
                sshagent(credentials: [SSH_CRED_ID]) {
                    sh "ssh -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} 'docker pull ${DOCKERHUB_USER}/java-app:latest && docker stop my-app || true && docker rm my-app || true && docker run -d --name my-app -p 8081:8080 ${DOCKERHUB_USER}/java-app:latest'"
                }
            }
        }
    }
}