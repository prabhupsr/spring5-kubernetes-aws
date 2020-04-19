# spring5-kubernetes-aws
# TO-RUN
#Build image locally
docker build -t webflux-kuber .
docker build -t prabhupsr/prrepo:webflux-kuber .
sudo docker push prabhupsr/prrepo:webflux-kuber
docker network create prnet

#Docker image uploaded to Docker HUB
docker run   --name=webflux-kuber   --rm   --network=prnet   -p 8080:8992   -e MONGO_URL=mongodb://mongo:27017/dev   prabhupsr/prrepo:webflux-kuber
docker run --name=mongo --rm --network=prnet mongo

kubernetes in AWS:

brew install aws-iam-authenticator 
aws eks update-kubeconfig --name prcluster
kubectl apply -f mytry/webflux-kuber/kube/aws-auth-cm.yaml
kubectl create -f mytry/webflux-kuber/kube/mongo.yaml
create -f mytry/webflux-kuber/kube/webflux-kuber.yaml
get services -o wide

References:
https://learnk8s.io/spring-boot-kubernetes-guide
https://www.youtube.com/watch?v=6H5sXQoJiso&t=1758s
