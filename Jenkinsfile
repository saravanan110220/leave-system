pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timeout(time: 2, unit: 'HOURS')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out code from GitHub..."
                    checkout([$class: 'GitSCM',
                        branches: [[name: '*/main']],
                        userRemoteConfigs: [[url: 'https://github.com/saravanan110220/leave-system.git']]
                    ])
                }
            }
        }

        stage('Build') {
            steps {
                script {
                    echo "Building with Maven..."
                    sh 'mvn clean compile package -DskipTests'
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo "Running JUnit tests..."
                    sh 'mvn test'
                }
            }
        }

        stage('Archive') {
            steps {
                script {
                    echo "Archiving artifacts..."
                    archiveArtifacts artifacts: 'target/leave-system-*.jar',
                                     allowEmptyArchive: true
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Recording test results..."
                junit testResults: 'target/surefire-reports/*.xml',
                      allowEmptyResults: true
            }
        }
        success {
            echo '✅ BUILD SUCCESS!'
        }
        failure {
            echo '❌ BUILD FAILED!'
        }
    }
}