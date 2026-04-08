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

                // 1. Cấp quyền thực thi cho Gradle ngay trước khi Snyk chạy
                sh 'chmod +x gradlew'

                // 2. Chạy Snyk (Đã gỡ bỏ --all-projects để quét đơn giản và chính xác hơn)
                snykSecurity(
                    snykInstallation: 'snyk-cli',
                    snykTokenId: 'snyk-token',
                    targetFile: 'build.gradle',
                    failOnIssues: false
                ) // <--- Lúc nãy bạn bị thiếu dấu đóng ngoặc tròn ở đây
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