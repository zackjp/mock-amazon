FROM node:20-slim

# Install system dependencies
RUN apt-get update && apt-get install -y \
    git \
    curl \
    ca-certificates \
    procps \
    openjdk-17-jdk \
    unzip \
    && rm -rf /var/lib/apt/lists/*

# Install Claude Code CLI globally inside the container
RUN npm install -g @anthropic-ai/claude-code

#ARG USER_UID=501
#ARG USER_GID=501

# Create a non-root user for sandboxing
#RUN groupadd -g ${USER_GID} devuser \
#    && useradd -m -s /bin/bash -u ${USER_UID} -g ${USER_GID} devuser \
#    && mkdir -p /workspace \
#    && chown -R devuser:devuser /workspace

# Switch to non-root user
#USER devuser
WORKDIR /workspace