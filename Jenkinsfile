pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                // Kéo code từ Github về
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
            }
        }

        stage('2. SCA Scan (Quét thư viện lỗi)') {
            steps {
                echo 'Đang chạy OWASP Dependency-Check...'
                // Chạy lệnh quét thư viện bằng Gradle (Dùng bat cho Windows)
                bat 'gradlew dependencyCheckAnalyze'
            }
        }

        stage('3. UI Automation Test') {
            steps {
                echo 'Bắt đầu chạy TestNG...'
                // Sửa thành bat để chạy trên máy Windows của bạn
                bat 'gradlew clean test'
            }
        }
    }

    post {
        always {
            echo 'Đang xuất báo cáo Allure Report...'
            // Gom kết quả test lại để vẽ biểu đồ
            allure includeProperties: false, results: [[path: 'build/allure-results']]
        }
    }
}