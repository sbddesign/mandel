#!/bin/bash

# Script to securely access the LDK Node management interface on Render
# This creates an SSH tunnel to access the management interface locally

echo "🔐 Setting up secure access to LDK Node management interface..."
echo ""
echo "This will create an SSH tunnel to access the management interface at:"
echo "   http://localhost:8091"
echo ""
echo "Press Ctrl+C to stop the tunnel when you're done."
echo ""

# SSH tunnel command
ssh -i ~/.ssh/id_ed25519_render -L 8091:localhost:8091 srv-d3ffi1d6ubrc73a4jpqg@ssh.oregon.render.com
