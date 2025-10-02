#!/bin/bash

# Backup script for LDK Node state
# This script creates a backup of the node data that can be restored if needed

echo "💾 Creating backup of LDK Node state..."

# Create backup directory with timestamp
BACKUP_DIR="backups/ldk-node-$(date +%Y%m%d-%H%M%S)"
mkdir -p "$BACKUP_DIR"

# Connect to Render service and create backup
echo "📡 Connecting to Render service..."
ssh -i ~/.ssh/id_ed25519_render srv-d3ffi1d6ubrc73a4jpqg@ssh.oregon.render.com "tar -czf - /opt/render/project/src/ldk_node_data" > "$BACKUP_DIR/ldk-node-data.tar.gz"

if [ $? -eq 0 ]; then
    echo "✅ Backup created successfully: $BACKUP_DIR/ldk-node-data.tar.gz"
    echo "📊 Backup size: $(du -h "$BACKUP_DIR/ldk-node-data.tar.gz" | cut -f1)"
else
    echo "❌ Backup failed"
    exit 1
fi

echo "🔄 To restore this backup, run:"
echo "   tar -xzf $BACKUP_DIR/ldk-node-data.tar.gz -C /opt/render/project/src/"
