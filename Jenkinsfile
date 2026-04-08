pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
            }
        }

        stage('2. SCA Scan (Quét thư viện lỗi)') {
            steps {
                echo 'Đang chạy OWASP Dependency-Check...'
                // Cấp quyền thực thi và chạy lệnh bằng sh (dành cho Linux/Docker)
                sh 'chmod +x gradlew'
                sh './gradlew dependencyCheckAnalyze'
            }
        }

        stage('3. UI Automation Test') {
            steps {
                echo 'Bắt đầu chạy TestNG...'
                sh 'chmod +x gradlew'
                sh './gradlew clean test'
            }
        }
    }

    post {
        always {
            echo 'Đang xuất báo cáo Allure Report...'
            allure includeProperties: false, results: [[path: 'build/allure-results']]
        }
    }
}