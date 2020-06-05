# spring5-kubernetes-aws
# TO-RUN
#Build image locally
docker build -t webflux-kuber . /n
docker build -t prabhupsr/prrepo:webflux-kuber . /n
sudo docker push prabhupsr/prrepo:webflux-kuber /n
docker network create prnet /n

#Docker image uploaded to Docker HUB /n
docker run   --name=webflux-kuber   --rm   --network=prnet   -p 8080:8992   -e MONGO_URL=mongodb://mongo:27017/dev   prabhupsr/prrepo:webflux-kuber  \n
docker run --name=mongo --rm --network=prnet mongo \n

kubernetes in AWS:

brew install aws-iam-authenticator \n
aws eks update-kubeconfig --name prcluster \n
kubectl apply -f mytry/webflux-kuber/kube/aws-auth-cm.yaml \n
kubectl create -f mytry/webflux-kuber/kube/mongo.yaml \n
create -f mytry/webflux-kuber/kube/webflux-kuber.yaml \n
get services -o wide \n

References:
https://learnk8s.io/spring-boot-kubernetes-guide
https://www.youtube.com/watch?v=6H5sXQoJiso&t=1758s
