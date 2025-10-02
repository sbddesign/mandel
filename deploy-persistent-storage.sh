#!/bin/bash

echo "🚀 Deploying persistent storage configuration..."

# Check if we're in the right directory
if [ ! -f "Dockerfile" ]; then
    echo "❌ Please run this script from the project root directory"
    exit 1
fi

# Check git status
echo "📊 Git status:"
git status --short

echo ""
echo "📝 To deploy the persistent storage changes, run these commands:"
echo ""
echo "1. Commit the changes:"
echo "   git commit -m 'Add persistent storage for LDK Node data'"
echo ""
echo "2. Push to trigger deployment:"
echo "   git push origin main"
echo ""
echo "3. Monitor deployment:"
echo "   # Check Render dashboard or logs"
echo ""
echo "🔧 What this deployment adds:"
echo "   ✅ Persistent LDK Node data directory"
echo "   ✅ Symlink to preserve data across deployments"
echo "   ✅ Backup script for node state"
echo "   ✅ Updated documentation"
echo ""
echo "⚠️  Note: After deployment, your existing node data will be lost"
echo "   but future deployments will preserve the data."
