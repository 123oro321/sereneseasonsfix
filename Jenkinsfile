pipeline {
    agent {
        label 'jenkins-agent'
    }
    stages {
        stage('Build') {
            steps {
                // Run Gradle on a Unix agent.
                sh "chmod u+x gradlew"
                withGradle {
                    sh "./gradlew build"
                }

            }

            post {
                success {
                    archiveArtifacts 'build/libs/*.jar'
                }
            }
        }
    }
}
