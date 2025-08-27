# 1. Stop and remove all containers
docker stop $(docker ps -aq)
docker rm $(docker ps -aq)

# 2. Remove all images
docker rmi $(docker images -q)

# 3. Optional: Remove unused volumes and networks
docker volume prune -f
docker network prune -f
docker system prune -a -f --volumes
