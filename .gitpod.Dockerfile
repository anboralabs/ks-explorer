FROM mcr.microsoft.com/devcontainers/base:ubuntu-24.04

RUN cd /tmp \
&& curl -LO https://github.com/kasmtech/KasmVNC/releases/download/v1.4.0/kasmvncserver_noble_1.4.0_amd64.deb \
&& apt-get update && apt-get install -yq ./kasm*.deb && rm kasm*.deb \
&& apt-get install -yq --no-install-recommends dbus dbus-x11 gnome-keyring xfce4 xfce4-terminal xdg-utils x11-xserver-utils

ENV DISPLAY=:1
ARG USERNAME=vscode
ARG HOMEPATH=/home/$USERNAME

RUN adduser $USERNAME ssl-cert

USER $USERNAME
SHELL ["/bin/bash", "-ic"]

RUN <<SCRIPT
    set -eu

    printf '%s\n' 123456 123456 | vncpasswd -u $USER -ow
    cat > $HOME/.xinitrc << 'EOF'
    #!/bin/bash

    : "${DISPLAY:="$DISPLAY"}"
    export DISPLAY

    exec dbus-launch --exit-with-session xfce4-session
EOF
    chmod +x $HOME/.xinitrc

    mkdir -p $HOME/.vnc
    ln -sf $HOME/.xinitrc $HOME/.vnc/xstartup
    touch $HOME/.vnc/.de-was-selected

    cat > $HOME/.vnc/kasmvnc.yaml << 'EOF'
logging:
  log_writer_name: all
  log_dest: logfile
  level: 100

network:
  protocol: http
  interface: 0.0.0.0
  websocket_port: 5901
  use_ipv4: true
  use_ipv6: false
  udp:
    public_ip: auto
    port: auto
    payload_size: auto
    stun_server: auto
  ssl:
    require_ssl: false
EOF

SCRIPT


# Optional: Install Browser (Chrome)
# chrome and basic render font
# misc deps for electron and puppeteer to run
USER root
RUN cd /tmp && glink="https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb" \
	&& wget -q "$glink" \
	&& apt-get install -yq --no-install-recommends libasound2-dev libgtk-3-dev libnss3-dev \
	fonts-noto fonts-noto-cjk ./"${glink##*/}" \
	\
	# OLD: && ln -srf /usr/bin/chromium /usr/bin/google-chrome \
	# OLD: To make ungoogled_chromium discoverable by tools like flutter
	&& ln -srf /usr/bin/google-chrome /usr/bin/chromium \
	\
	# Extra chrome tweaks
	## Disables welcome screen
	&& t="$HOMEPATH/.config/google-chrome/First Run" && sudo -u $USERNAME mkdir -p "${t%/*}" && sudo -u $USERNAME touch "$t" \
	## Disables default browser prompt \
	&& t="/etc/opt/chrome/policies/managed/managed_policies.json" && mkdir -p "${t%/*}" && printf '{ "%s": %s }\n' DefaultBrowserSettingEnabled false > "$t"

# For Qt WebEngine on docker
ENV QTWEBENGINE_DISABLE_SANDBOX 1

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 21.0.6-jbr && \
    sdk default java 21.0.6-jbr"
