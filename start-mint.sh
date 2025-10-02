#!/bin/bash

# Startup script for Cashu mint with persistent LDK Node data

echo "🚀 Starting Cashu mint with persistent storage..."

# Ensure persistent data directory exists on mounted disk
mkdir -p /data/ldk_node_data

# Create symlink from working directory to persistent directory
# This ensures LDK Node data persists across deployments
if [ ! -L /usr/src/app/ldk_node_data ]; then
    echo "📁 Creating symlink to persistent data directory..."
    ln -sf /data/ldk_node_data /usr/src/app/ldk_node_data
fi

# Set the LDK Node data directory environment variable
export CDK_MINTD_LDK_NODE_DATA_DIR=/data/ldk_node_data

echo "✅ Persistent storage configured"
echo "📊 LDK Node data directory: $CDK_MINTD_LDK_NODE_DATA_DIR"

# Start the mint server
echo "🎯 Starting CDK mintd..."
exec cdk-mintd
