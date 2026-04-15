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
                    echo 'Đang thiết lập và khởi động OWASP ZAP...'

                    // 1. Tải ZAP 2.16.0 (Đã sửa link chuẩn)
                    sh '''
                        if [ ! -d "ZAP_2.16.0" ]; then
                            echo "Lần đầu chạy: Đang tải phần mềm OWASP ZAP 2.16.0..."
                            wget -qO zap.tar.gz https://github.com/zaproxy/zaproxy/releases/download/v2.16.0/ZAP_2.16.0_Linux.tar.gz
                            tar -xzf zap.tar.gz
                        fi
                    '''

                    // 2. Chạy ZAP ngầm
                    echo "Khởi động OWASP ZAP Proxy ở cổng 8080..."
                    sh 'nohup ./ZAP_2.16.0/zap.sh -daemon -host 0.0.0.0 -port 8080 -config api.disablekey=true > zap.log 2>&1 &'

                    echo 'Chờ 30 giây để công cụ ZAP khởi động lên hoàn toàn...'
                    sleep 30
                }
            }
        }

        stage('5. UI Automation Test (Selenium)') {
            steps {
                echo 'Bắt đầu chạy TestNG qua cổng ZAP Proxy...'
                sh 'chmod +x gradlew'
                // Kịch bản Selenium sẽ chạy và đẩy data chui qua cổng 8080 của ZAP
                sh './gradlew clean test'
            }
        }

        stage('6. DAST Report & Clean Up ZAP') {
            steps {
                script {
                    echo 'Đang đợi ZAP xử lý các gói tin cuối cùng...'
                    sleep 10

                    echo 'Đang trích xuất báo cáo DAST từ ZAP...'
                    // Lấy báo cáo dạng HTML từ API của ZAP
                    sh 'curl -L http://localhost:8080/OTHER/core/other/htmlreport/? -o zap-report.html'

                    echo 'Ra lệnh tắt phần mềm OWASP ZAP...'
                    // Gọi API lệnh tắt ZAP
                    sh 'curl -s http://localhost:8080/JSON/core/action/shutdown/ || true'
                }
            }
        }
    }

    post {
        always {
            echo 'Đang tổng hợp và xuất các báo cáo lên giao diện Jenkins...'

            // 1. Báo cáo UI (Allure)
            allure includeProperties: false, results: [[path: 'build/allure-results']]

            // 2. Lưu file gốc (Cho phép user tải về)
            archiveArtifacts artifacts: 'codeql-results.sarif, zap-report.html', allowEmptyArchive: true

            // 3. Hiển thị biểu đồ SARIF (SAST CodeQL)
            recordIssues(
                tools: [sarif(pattern: 'codeql-results.sarif')],
                qualityGates: [[threshold: 1, type: 'TOTAL', criticality: 'NOTE']]
            )
        }

        // Khối dọn dẹp cuối cùng
        cleanup {
            script {
                echo 'Kiểm tra an toàn: Đảm bảo tiến trình ZAP đã được tắt hẳn...'
                // Đã cập nhật tên file jar theo bản 2.16.0
                sh 'pkill -f zap.sh || true'
                sh 'pkill -f zap-2.16.0.jar || true'
            }
        }
    }
}