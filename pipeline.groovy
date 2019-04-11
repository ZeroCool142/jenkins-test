node {
    def mvnHome
    stage('Preparation') { // for display purposes
        // Get some code from a GitHub repository
        git 'https://github.com/ZeroCool142/jenkins-test.git'
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'maven360'
    }
    stage('Build') {
        // Run the maven build
        if (isUnix()) {
            sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
        } else {
            bat(/"${mvnHome}\bin\mvn" -Dmaven.test.failure.ignore clean package/)
        }
    }
    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
        // archive 'target/*.jar'
    }
    always {
        emailtext(
                to: "ilya.kislov142@gmail.com",
                mimeType: 'text/html',
                subject: "[JENKINS] '${env.JOB_NAME}'",
                attachLog: true,
                attachmentsPattern: '**/*.properties',
                body: "test job ${currentBuild.result}"
        )
    }
}