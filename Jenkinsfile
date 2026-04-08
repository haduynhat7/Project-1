pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                // Lấy code từ GitHub
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
                // Cấp quyền thực thi 1 lần duy nhất cho toàn bộ Pipeline
                sh 'chmod +x gradlew'
            }
        }

        stage('2. SCA Scan (Quét thư viện lỗi)') {
            steps {
                echo 'Đang chạy OWASP Dependency-Check...'
                // Chạy quét SCA (đã cấu hình né lỗi 403 trong build.gradle)
                sh './gradlew dependencyCheckAnalyze'
            }
        }

        stage('3. UI Automation Test') {
            steps {
                echo 'Bắt đầu chạy TestNG...'
                // Chạy test UI và xuất kết quả cho Allure
                sh './gradlew clean test'
            }
        }
    }

    post {
        always {
            echo 'Đang tổng hợp báo cáo...'

            // 1. Xuất báo cáo SCA ra giao diện Jenkins (Cần cài plugin Dependency-Check trên Jenkins)
            dependencyCheckPublisher pattern: 'build/reports/dependency-check-report.xml'

            // 2. Xuất báo cáo Allure Report
            allure includeProperties: false, results: [[path: 'build/allure-results']]
        }
    }
}