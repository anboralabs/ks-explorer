FROM gitpod/workspace-full-vnc as vnc
RUN sudo apt-get update && \
    sudo apt-get install -y libgtk-3-dev && \
    sudo rm -rf /var/lib/apt/lists/*

USER gitpod
RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 21.0.6-jbr && \
    sdk default java 21.0.6-jbr"
