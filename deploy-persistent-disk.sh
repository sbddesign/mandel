#!/bin/bash

echo "🚀 Deploying persistent disk configuration..."

# Check if we're in the right directory
if [ ! -f "Dockerfile" ]; then
    echo "❌ Please run this script from the project root directory"
    exit 1
fi

echo "📊 Files updated for persistent disk:"
echo "   ✅ Dockerfile - Updated to use /data/ldk_node_data"
echo "   ✅ start-mint.sh - Updated to use mounted disk"
echo "   ✅ backup-node-state.sh - Updated to backup from /data/"
echo "   ✅ README.md - Updated documentation"
echo ""
echo "🔧 Changes made:"
echo "   • Changed data directory from /opt/render/project/src/ to /data/"
echo "   • Updated environment variable CDK_MINTD_LDK_NODE_DATA_DIR"
echo "   • Updated backup and restore paths"
echo "   • Updated documentation"
echo ""
echo "📝 To deploy these changes:"
echo ""
echo "1. Commit the changes:"
echo "   git add ."
echo "   git commit -m 'Configure persistent disk for LDK Node data'"
echo ""
echo "2. Push to trigger deployment:"
echo "   git push origin main"
echo ""
echo "3. Monitor deployment:"
echo "   # Check Render dashboard or logs"
echo ""
echo "⚠️  Important: Make sure you've created the persistent disk in Render dashboard:"
echo "   • Name: ldk-node-data"
echo "   • Mount Path: /data"
echo "   • Size: 10 GB"
echo ""
echo "✅ After deployment, your LDK Node data will persist across deployments!"
