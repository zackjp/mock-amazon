FROM node:20-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    git \
    curl \
    wget \
    ca-certificates \
    procps \
    openjdk-17-jdk \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Android SDK environment variables
ENV ANDROID_HOME=/opt/android-sdk \
    ANDROID_SDK_ROOT=/opt/android-sdk \
    ANDROID_ADB_SERVER_HOST=host.docker.internal

ENV PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools

# Install Android SDK command-line tools (pinned: build 9862592)
RUN mkdir -p $ANDROID_HOME/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-9862592_latest.zip \
        -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d /tmp/cmdline-tools-extract && \
    mv /tmp/cmdline-tools-extract/cmdline-tools $ANDROID_HOME/cmdline-tools/latest && \
    rm -rf /tmp/cmdline-tools.zip /tmp/cmdline-tools-extract

# Accept SDK licenses and install required packages
RUN yes | sdkmanager --licenses > /dev/null && \
    sdkmanager \
        "platform-tools" \
        "platforms;android-36" \
        "build-tools;35.0.0"

# ADB wrapper: routes all adb calls to the Mac host's ADB server over the Docker bridge.
# The host must run: adb kill-server && adb -a nodaemon server start
RUN printf '#!/bin/bash\nexec /opt/android-sdk/platform-tools/adb -H host.docker.internal "$@"\n' \
    > /usr/local/bin/adb && chmod +x /usr/local/bin/adb

# Install Claude Code CLI globally inside the container
RUN npm install -g @anthropic-ai/claude-code

WORKDIR /workspace
