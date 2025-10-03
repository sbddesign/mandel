#!/bin/bash

echo "🚀 Deploying CDK_MINTD_WORK_DIR fix for persistent storage..."

# Check if we're in the right directory
if [ ! -f "Dockerfile" ]; then
    echo "❌ Please run this script from the project root directory"
    exit 1
fi

echo "📊 Files updated for CDK_MINTD_WORK_DIR:"
echo "   ✅ Dockerfile - Now uses CDK_MINTD_WORK_DIR=/data"
echo "   ✅ start-mint.sh - Simplified to use work directory"
echo "   ✅ backup-node-state.sh - Backs up entire /data directory"
echo "   ✅ README.md - Updated documentation"
echo ""
echo "🔧 Key changes made:"
echo "   • Changed from CDK_MINTD_LDK_NODE_DATA_DIR to CDK_MINTD_WORK_DIR"
echo "   • Work directory set to /data (mounted disk)"
echo "   • Database will be stored at /data/cdk-mintd.sqlite"
echo "   • LDK Node data will be created at /data/ldk_node_data/"
echo ""
echo "📝 To deploy these changes:"
echo ""
echo "1. Commit the changes:"
echo "   git add ."
echo "   git commit -m 'Fix persistent storage using CDK_MINTD_WORK_DIR'"
echo ""
echo "2. Push to trigger deployment:"
echo "   git push origin main"
echo ""
echo "3. Monitor deployment:"
echo "   # Check Render dashboard or logs"
echo ""
echo "✅ This should fix the persistence issue!"
echo "   The mint will now store its database and LDK Node data on the mounted disk."
