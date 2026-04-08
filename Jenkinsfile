pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
            }
        }

    stage('2. SCA Scan (Snyk)') {
                steps {
                    echo 'Đang chạy Snyk Security Scan...'
                    snykSecurity(
                        snykInstallation: 'snyk-cli',
                        snykTokenId: 'snyk-token',
                        targetFile: 'build.gradle', // Chỉ quét các thư viện khai báo trong file này
                        failOnIssues: false,        // Đặt false để Build không bị FAIL nếu phát hiện lỗi bảo mật (để bạn xem report trước)
                        additionalArguments: '--all-projects'
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