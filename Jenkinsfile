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
                )
            }
        }

        stage('3. SAST Scan (CodeQL)') {
            steps {
                script {
                    echo 'Bắt đầu thiết lập và chạy CodeQL SAST...'

                    // 1. Tải và giải nén CodeQL CLI
                    sh '''
                        if [ ! -d "codeql" ]; then
                            echo "Đang tải CodeQL..."
                            wget -q https://github.com/github/codeql-action/releases/latest/download/codeql-bundle-linux64.tar.gz
                            tar -xzf codeql-bundle-linux64.tar.gz
                        fi
                    '''

                    // 2. Tạo Database CodeQL bằng cách theo dõi quá trình build của Gradle
                    // Gradle đã được cấp quyền ở bước 2 nên có thể chạy thẳng
                    sh '''
                        export PATH=$PATH:$(pwd)/codeql
                        echo "Tạo CodeQL Database..."
                        codeql database create codeql-db --language=java --command="./gradlew clean classes" --overwrite
                    '''

                    // 3. Phân tích Database và xuất kết quả ra file .sarif
                    sh '''
                        export PATH=$PATH:$(pwd)/codeql
                        echo "Phân tích mã nguồn..."
                        codeql database analyze codeql-db java-security-and-quality.qls \
                            --format=sarif-latest \
                            --output=codeql-results.sarif
                    '''
                }
            }
        }

        stage('4. UI Automation Test') {
            steps {
                echo 'Bắt đầu chạy TestNG...'
                // Dùng lại lệnh của bạn, chỉ thêm số 4 ở tên stage
                sh 'chmod +x gradlew'
                sh './gradlew clean test'
            }
        }
    }

    post {
        always {
            echo 'Đang xuất báo cáo Allure Report và lưu file CodeQL...'

            // Xuất báo cáo Allure
            allure includeProperties: false, results: [[path: 'build/allure-results']]

            // Đính kèm file báo cáo của CodeQL để tải về từ Jenkins
            archiveArtifacts artifacts: 'codeql-results.sarif', allowEmptyArchive: true
        }
    }
}