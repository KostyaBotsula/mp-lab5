
# How to run lab5

1. Start docker container in background - `sudo docker-compose up -d`
2. Run master process - `sudo docker exec -it docker-spark_master_1 /bin/bash`
3. Change directory to scripts - `cd /scripts`
4. Start sh script:
    1. For standalone mode - `sh standalone.sh`
    2. For yarn mode - `sh yarn.sh`
5. Stop all - `sh stop.sh`