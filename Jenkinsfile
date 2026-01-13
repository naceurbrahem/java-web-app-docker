pipeline {
    agent any
    environment {
        DOCKERHUB_USER = 'naceurbrahem'
        DOCKERHUB_CRED_ID = 'dockerhub-login' 
        SSH_CRED_ID = 'NaceurBrahem' 
        VM_IP = '192.168.0.65'
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
                sh 'mvn clean package'
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    sh "docker build -t ${DOCKERHUB_USER}/java-app:latest ."
                    withCredentials([usernamePassword(credentialsId: DOCKERHUB_CRED_ID, passwordVariable: 'PASS', usernameVariable: 'USER')]) {
                        sh "echo ${PASS} | docker login -u ${USER} --password-stdin"
                        sh "docker push ${DOCKERHUB_USER}/java-app:latest"
                    }
                }
            }
        }
        stage('Deploy to VMware') {
            steps {
                sshagent([SSH_CRED_ID]) {
                    // Utilisation de triple double-quotes pour gérer proprement les commandes SSH imbriquées
                    sh """
                        ssh -o StrictHostKeyChecking=no ${VM_USER}@${VM_IP} "
                            docker stop my-app || true && \
                            docker rm my-app || true && \
                            docker pull ${DOCKERHUB_USER}/java-app:latest && \
                            docker run -d --name my-app -p 8081:8080 ${DOCKERHUB_USER}/java-app:latest
                        "
                    """
                }
            }
        }
    }
    post {
        success {
            echo 'Déploiement réussi sur VMware !'
        }
        failure {
            echo 'Le pipeline a échoué. Vérifiez les logs.'
        }
    }
}