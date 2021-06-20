pipeline {
    agent any
    // agent {
    //     label "java-8"
        // label "python-3.5"
    // }
    stages  {
        // stage("Checkout") {
        //     steps {
                // 这里sh调用ci-init 初始化
        //         sh 'ci-init'
                // 这里检出仓库，默认检出分支为环境变量中的GIT_BUILD_REF
        //         checkout(
        //           [$class: 'GitSCM', branches: [[name: env.GIT_BUILD_REF]], 
        //           userRemoteConfigs: [[url: env.GIT_REPO_URL]]]
        //         )
        //     }
        // }
        // 构建jar包
        stage("BuildIt") {
            steps {
                echo "Building..."
                // 输出java版本
                sh 'java -version'
                // 调用maven 构建jar包
                sh 'mvn package'
                echo "Success building."
                // 收集构建产物，这一步成功，我们就可以在平台上看到构建产物
                // archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true // 收集构建产物
            }
        }
        // 测试
        stage("TestIt") {
            steps {
                echo "Testing..."
                // 做单元测试
                sh 'mvn test'
                echo "Finish testing."
            }
        }
        stage("DeployIt") {
            steps {
                echo 'Deploying (TBD)'
            }
        }
    }
}