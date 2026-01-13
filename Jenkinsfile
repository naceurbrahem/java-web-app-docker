pipeline {
    agent any
    environment {
        DOCKERHUB_USER = 'naceurbrahem'
        DOCKERHUB_CRED_ID = 'dockerhub-login' // Vérifiez que cet ID existe dans Jenkins
        SSH_CRED_ID = 'vmware-ssh-key'
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
                        // Utilisation de doubles quotes pour permettre l'expansion de $PASS
                        sh "echo ${PASS} | docker login -u ${USER} --password-stdin"
                        sh "docker push ${DOCKERHUB_USER}/java-app:latest"
                    }
                }
            }
        }
        stage('Deploy to VMware') {
    steps {
        sshagent(['NaceurBrahem']) {
            sh '''
                ssh -o StrictHostKeyChecking=no NaceurBrahem@192.168.0.65 "
                    docker stop my-app || true
                    docker rm my-app || true
                    docker pull naceurbrahem/java-app:latest
                    docker run -d --name my-app -p 8081:8080 naceurbrahem/java-app:latest
                "
            '''
        }
    }
}
    }
}