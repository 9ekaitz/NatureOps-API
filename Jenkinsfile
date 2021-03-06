pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                echo '----- Build app -----'
                withMaven (maven: 'Maven') {
                    withCredentials([]) {
                        sh 'mvn clean compile'
                    }
                }
            }
        }
        stage('Testing') {
            steps {
                echo '----- Test app -----'
                withMaven (maven: 'Maven') {
                        sh 'mvn test'
                }
            }
        }
        stage('Static Analysis') {
            steps {
                withMaven(maven: 'Maven') {
                    withSonarQubeEnv(installationName:'Sonarqube-NatureOps', credentialsId: 'sonar-token') {
                        withCredentials([string(credentialsId: 'sonar-token', variable: 'SONAR_TOKEN')]) {
                                            sh 'mvn sonar:sonar \
                                                -Dsonar.host.url=https://sonarqube.natureops.eus \
                                                -Dsonar.login=${SONAR_TOKEN} \
                                                -Dsonar.sources=src/main/resources,src/main/java \
                                                -Dsonar.tests=src/test \
                                                -Dsonar.java.coveragePlugin=jacoco \
                                                -Dsonar.dynamicAnalysis=reuseReports'
                        }
                    }
                }
            }
        }
        stage('QualityGate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true, credentialsId: 'sonar-webhook'
                }
            }
        }
        stage('Deploy') {
            when {
                branch 'master'
            }
            steps {
                echo '----- Deploy app -----'
                withMaven (maven: 'Maven') {
                    withCredentials([]) {
                        sh 'mvn -Dmaven.test.skip package'
                    }
                }
                script {
                    deploy adapters: [tomcat9(credentialsId: 'tomcat-deploy-user', path: '', url: 'http://10.8.0.1:8080')], contextPath: '/', onFailure: false, war: '**/*.war'
                }
            }
        }
    }
}