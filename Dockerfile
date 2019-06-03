FROM gettyimages/spark:2.4.1-hadoop-3.0
MAINTAINER Botsual Konstantin

# Set JAVA_HOME
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64

# Update and install comands
RUN apt-get -y update
RUN apt-get -y upgrade
RUN apt-get -y install ssh
RUN apt-get -y install wget
RUN apt-get -y install openssh-server

RUN ssh-keygen -t rsa -f ~/.ssh/id_rsa -P '' && \
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
COPY ssh/* $HOME/.ssh/config

COPY hadoop/* $HADOOP_CONF_DIR/

RUN chmod +x $HADOOP_CONF_DIR/hadoop-env.sh

COPY scripts/* /scripts/
RUN chmod +x /scripts/start.sh
RUN chmod +x /scripts/standalone.sh
RUN chmod +x /scripts/yarn.sh
RUN chmod +x /scripts/stop.sh

RUN mkdir -p /app/hadoop/tmp && \
    mkdir -p /usr/local/hadoop_store/hdfs/namenode && \
    mkdir -p /usr/local/hadoop_store/hdfs/datanode
RUN hdfs namenode -format

ENTRYPOINT [ "/bin/bash", "-c", "service ssh start; tail -f /dev/null"]