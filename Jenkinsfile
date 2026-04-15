pipeline {
    agent any

    stages {
        stage('1. Checkout Code') {
            steps {
                echo 'Đang lấy mã nguồn từ GitHub...'
                git branch: 'main', url: 'https://github.com/haduynhat7/Project-1.git'
            }
        }

        stage('2. SCA Scan (Snyk)') {
            steps {
                echo 'Đang chạy Snyk Security Scan để kiểm tra thư viện...'
                sh 'chmod +x gradlew'
                snykSecurity(
                    snykInstallation: 'snyk-cli',
                    snykTokenId: 'snyk-token',
                    targetFile: 'build.gradle',
                    failOnIssues: false // Không cho phép dừng Pipeline nếu có lỗi SCA
                )
            }
        }

        stage('3. SAST Scan (CodeQL)') {
            steps {
                script {
                    echo 'Bắt đầu thiết lập và chạy CodeQL SAST...'

                    // Tải CodeQL (nếu chưa có)
                    sh '''
                        if [ ! -d "codeql" ]; then
                            echo "Đang tải CodeQL..."
                            wget -q https://github.com/github/codeql-action/releases/latest/download/codeql-bundle-linux64.tar.gz
                            tar -xzf codeql-bundle-linux64.tar.gz
                        fi
                    '''

                    // Tạo Database CodeQL
                    sh '''
                        export PATH=$PATH:$(pwd)/codeql
                        echo "Tạo CodeQL Database..."
                        codeql database create codeql-db --language=java --command="./gradlew clean testClasses --no-daemon" --overwrite
                    '''

                    // Phân tích Database và xuất file SARIF
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

        stage('4. Start OWASP ZAP (DAST Proxy)') {
            steps {
                script {
                    echo 'Đang khởi động OWASP ZAP Proxy chạy ngầm...'
                    // Khởi động ZAP qua Docker ở cổng 8080
                    sh 'docker run -d -u root --name zap-proxy -p 8080:8080 -i ghcr.io/zaproxy/zaproxy:stable zap.sh -daemon -host 0.0.0.0 -port 8080 -config api.disablekey=true'

                    echo 'Chờ 20 giây để ZAP khởi động hoàn tất...'
                    sleep 20
                }
            }
        }

        stage('5. UI Automation Test (Selenium)') {
            steps {
                echo 'Bắt đầu chạy TestNG qua ZAP Proxy...'
                sh 'chmod +x gradlew'
                // Kịch bản Selenium sẽ chạy và đẩy traffic qua cổng 8080
                sh './gradlew clean test'
            }
        }

        stage('6. DAST Report & Clean Up ZAP') {
            steps {
                script {
                    echo 'Đang trích xuất báo cáo DAST từ ZAP...'
                    // Đợi 10 giây để ZAP xử lý xong các request cuối cùng
                    sleep 10

                    // Lấy báo cáo dạng HTML
                    sh 'curl -L http://localhost:8080/OTHER/core/other/htmlreport/? -o zap-report.html'

                    echo 'Dọn dẹp Docker Container ZAP...'
                    // Dừng và xóa ZAP
                    sh 'docker stop zap-proxy && docker rm zap-proxy'
                }
            }
        }
    }

    post {
        always {
            echo 'Đang xuất các báo cáo lên Jenkins...'

            // 1. Báo cáo Allure (Test Giao diện)
            allure includeProperties: false, results: [[path: 'build/allure-results']]

            // 2. Lưu file gốc SARIF và HTML để có thể bấm tải về xem
            archiveArtifacts artifacts: 'codeql-results.sarif, zap-report.html', allowEmptyArchive: true

            // 3. Cập nhật biểu đồ SARIF (Warnings NG Plugin) cho CodeQL
            recordIssues(
                tools: [sarif(pattern: 'codeql-results.sarif')],
                qualityGates: [[threshold: 1, type: 'TOTAL', criticality: 'NOTE']]
            )
        }

        // Khối dọn dẹp (Safety Net): Đảm bảo ZAP không bị "treo" nếu Selenium chạy lỗi giữa chừng
        cleanup {
            script {
                echo 'Kiểm tra và dọn dẹp ZAP lần cuối (nếu còn sót)...'
                sh '''
                    if [ "$(docker ps -q -f name=zap-proxy)" ]; then
                        docker stop zap-proxy && docker rm zap-proxy
                    fi
                '''
            }
        }
    }
}