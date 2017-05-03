#!groovy

node('!master && docker') {
  checkout scm

  def buildImage = docker.image('gfx2015/android')
  buildImage.inside("-u 0:0") {
    env.SONAR_PROJECT_KEY = 'bn-payment-android'
    env.SONAR_NAME = 'Bambora Native Payment SDK'
    env.SONAR_HOST_URL = 'https://sonarqube-prod-eu-west-1.aws.bambora.com'
    withCredentials([[$class: 'StringBinding',
                      credentialsId: 'sonar-token',
                      variable: 'SONAR_LOGIN']]) {
      sh './gradlew testDebug sonar'
    }
  }
}
