# Use the official CDK mintd image
FROM cashubtc/mintd:latest

# Set the working directory
WORKDIR /usr/src/app

# Create directory for LDK node data
RUN mkdir -p /usr/src/app/ldk_node_data

# Create persistent data directory that survives deployments
RUN mkdir -p /opt/render/project/src/ldk_node_data

# Expose ports
EXPOSE 3338 8091

# Set environment variables with defaults
ENV CDK_MINTD_URL=http://localhost:3338
ENV CDK_MINTD_LN_BACKEND=ldk-node
ENV CDK_MINTD_LISTEN_HOST=0.0.0.0
ENV CDK_MINTD_LISTEN_PORT=3338
ENV CDK_MINTD_DATABASE=sqlite
ENV CDK_MINTD_CACHE_BACKEND=memory
ENV CDK_MINTD_LDK_NODE_NETWORK=signet
ENV CDK_MINTD_LDK_NODE_ESPLORA_URL=https://mutinynet.com/api
ENV CDK_MINTD_LDK_NODE_RGS_URL=https://rgs.mutinynet.com/snapshot/0
ENV CDK_MINTD_LDK_NODE_BITCOIN_NETWORK=signet
ENV CDK_MINTD_LDK_NODE_LISTENING_ADDRESSES=0.0.0.0:9735
ENV CDK_MINTD_LDK_NODE_WEBSERVER_HOST=0.0.0.0
ENV CDK_MINTD_LDK_NODE_WEBSERVER_PORT=8091

# Set persistent data directory for LDK Node
ENV CDK_MINTD_LDK_NODE_DATA_DIR=/opt/render/project/src/ldk_node_data

# Copy startup script
COPY start-mint.sh /usr/src/app/start-mint.sh
RUN chmod +x /usr/src/app/start-mint.sh

# Note: Render handles persistent data through the filesystem

# Health check for Render
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
  CMD curl -f http://localhost:3338/v1/info || exit 1

# Start the mint server with persistent storage
CMD ["/usr/src/app/start-mint.sh"]
