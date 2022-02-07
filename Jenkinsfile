pipeline {
  agent none

  options {
    ansiColor("xterm")
  }

  triggers {
    cron(env.BRANCH_NAME == 'develop' ? '@weekly' : '')
  }

  stages {
    stage("Build") {
      agent {
        dockerfile {
          filename "dockerfiles/ci/Dockerfile"
        }
      }

      steps {
        sh "rm -f node_modules && ln -s /app/node_modules node_modules"
        sh "rm -rf dist"
        sh "npx shadow-cljs release app"
      }
    }

    stage("Deploy") {
      agent any

      steps {
        sh "rm -rf deploy"
        sh "git clone dokku@cofx.nl:cfb deploy"
        sh "rm -rf deploy/dist"
        sh "cp -R dist deploy"
        sh "cp -R dokku/. deploy"
        sh "cd deploy && git add . && git commit -m \"Deploy\" --allow-empty && git push"
      }
    }
  }
}
