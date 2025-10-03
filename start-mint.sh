#!/bin/bash

# Startup script for Cashu mint with persistent storage

echo "🚀 Starting Cashu mint with persistent storage..."

# Ensure persistent data directory exists on mounted disk
mkdir -p /data

# Set the work directory environment variable
export CDK_MINTD_WORK_DIR=/data

echo "✅ Persistent storage configured"
echo "📊 Work directory: $CDK_MINTD_WORK_DIR"
echo "📁 Database will be stored at: $CDK_MINTD_WORK_DIR/cdk-mintd.sqlite"

# Start the mint server
echo "🎯 Starting CDK mintd..."
exec cdk-mintd
