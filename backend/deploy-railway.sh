#!/bin/bash

# Railway Deployment Script for Mandel Backend
# This script helps set up the backend for Railway deployment

echo "🚀 Setting up Mandel Backend for Railway deployment..."

# Check if Railway CLI is installed
if ! command -v railway &> /dev/null; then
    echo "❌ Railway CLI not found. Please install it first:"
    echo "   npm install -g @railway/cli"
    echo "   or visit: https://docs.railway.app/develop/cli"
    exit 1
fi

# Check if logged in to Railway
if ! railway whoami &> /dev/null; then
    echo "🔐 Please log in to Railway first:"
    echo "   railway login"
    exit 1
fi

echo "✅ Railway CLI found and logged in"

# Create a new Railway project
echo "📦 Creating Railway project..."
railway init

# Set environment variables
echo "🔧 Setting up environment variables..."
echo "Please set these environment variables in Railway dashboard:"
echo ""
echo "CDK_MINTD_URL=https://your-app-name.railway.app"
echo "CDK_MINTD_MNEMONIC=your-secure-mnemonic-here"
echo ""

# Deploy
echo "🚀 Deploying to Railway..."
railway up

echo "✅ Deployment complete!"
echo "Your mint server will be available at your Railway URL"
echo "LDK Node management interface: https://your-app-name.railway.app:8091"
