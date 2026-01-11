pipeline {
    agent any
    environment {
        DOCKERHUB_USER = 'VOTRE_PSEUDO_DOCKERHUB'
        DOCKERHUB_CRED_ID = 'dockerhub-login'
        SSH_CRED_ID = 'vmware-ssh-key'
        VM_IP = '192.168.1.95'
        VM_USER = 'NaceurBrahem'
    }
    stages {
        stage('Nettoyage') {
            steps {
                deleteDir()
                checkout scm
            }
        }
        stage('Build JAR') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew build'
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
        stage('Déploiement SSH') {
            steps {
                sshagent(credentials: [SSH_CRED_ID]) {
                    sh "ssh -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} 'docker pull ${DOCKERHUB_USER}/java-app:latest && docker stop app-java || true && docker rm app-java || true && docker run -d --name app-java -p 8080:8080 ${DOCKERHUB_USER}/java-app:latest'"
                }
            }
        }
    }
}
