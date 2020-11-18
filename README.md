"# pgr301_devops" 

#How did I solve the task:
1. Make a barebone simple project
2. Push to git
3. Make a docker file for allowing to run docker locally on the PC
4. Added travis .yml 
5. Added tests and tested building via travis.
6. Push the local docker image to google docker registry to see that everything works.
7. Create a multi-staging docker file.
8. Create google service account which Travis can use
9. Download the service account JSON file
10. Encrypt the JSON file with travis encrypt
11. Add configurations like "before-install and script" on travis file so it builds and pushes the docker image
12. Create a meaningful app which has enough features to run metrics
13. Setup the series database 'influxdb' for storing metrics
14. Setup metrics in the program and query them in influxDB (http://localhost:8083/)
15. Add the ELK stack
16. Encrypt logz_io token


#Assignemnt 1
##Setting secrets:
1. Travis needs to deploy the container image to google cloud platform. Due to this, Travis needs a service account for being permitted to push such docker image. So you'll need to retrieve a JSON key for a service account, and encrypt this file with travis by the following command:
"travis encrypt-file google-key.json" (I had to add args --pro for pushing to travis-ci.com instead of travis-ci.org )
2. The logz_io token is neccesairy so we can push logs to app.logz.io, however this token should be kept secret. Solution is to use global variable which is encrypted by travis
so it's safe to push to git & travis can propery extract the enviroment variable LOGZ_TOKEN & LOGZ_URL. How to encrypt the token (it will also put the encrypted output in .travis.yml:
travis encrypt MY_SECRET_ENV=super_secret --add env.global


##Metrics included
todo: Add the metrics and short description
1. DistributionSummary - For viewing how many tasks has been completed or opened



#Decisions I've made:
- adoptopenjdk/openjdk11:alpine-slim is used as a base image. As openjdk:8-jdk-alpine is not kotlin, i chosed this image instead.


#Instructions
How to build image:
docker build . --tag helloworld --build-arg JAR_FILE=target/pgr301_devops-0.0.1-SNAPSHOT.jar


how to run docker container:
docker run -p 8081:8080 helloworld

//Todo:
- Add a paging list according to enterprise lecture
- Add travis test status in github readme.
- Todo: Make my own metrics
- Useful guide for making more benas for monitoring: https://dimitr.im/mastering-spring-boot-actuator#distribution-summaries

