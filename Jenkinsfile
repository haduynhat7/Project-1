pipeline {
    agent any
    stages {
        stage('1. Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
            }
        }

        stage('2. SAST Scan (Quet ma nguon)') {
            steps {
                echo 'Dang quet loi bao mat tinh...'
                // Day la noi ban se them SonarQube hoac Snyk sau nay
            }
        }

        stage('3. UI Automation Test') {
            steps {
                sh 'chmod +x gradlew'
                sh './gradlew clean test'
            }
        }
    }

    post {
        always {
            // Lenh nay de hien thi bao cao Allure len Jenkins
            allure includeProperties: false, results: [[path: 'build/allure-results']]
        }
    }
}