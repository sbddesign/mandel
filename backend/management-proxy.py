#!/usr/bin/env python3
"""
Simple proxy server to access LDK Node management interface with basic auth
Run this locally to securely access the management interface
"""

import http.server
import socketserver
import urllib.request
import urllib.parse
import base64
import os
import sys

# Configuration
MANAGEMENT_HOST = "mandel-mint.onrender.com"
MANAGEMENT_PORT = 8091
LOCAL_PORT = 8080
USERNAME = "admin"
PASSWORD = "mandel-secure-2024"  # Change this!

class AuthHandler(http.server.BaseHTTPRequestHandler):
    def do_GET(self):
        # Check for basic auth
        auth_header = self.headers.get('Authorization')
        if not auth_header or not auth_header.startswith('Basic '):
            self.send_auth_required()
            return
        
        # Verify credentials
        try:
            auth_string = auth_header[6:]  # Remove 'Basic '
            decoded = base64.b64decode(auth_string).decode('utf-8')
            username, password = decoded.split(':', 1)
            
            if username != USERNAME or password != PASSWORD:
                self.send_auth_required()
                return
        except:
            self.send_auth_required()
            return
        
        # Proxy the request to the management interface
        self.proxy_request()
    
    def send_auth_required(self):
        self.send_response(401)
        self.send_header('WWW-Authenticate', 'Basic realm="LDK Node Management"')
        self.send_header('Content-type', 'text/html')
        self.end_headers()
        self.wfile.write(b'<h1>401 Unauthorized</h1><p>Please provide valid credentials.</p>')
    
    def proxy_request(self):
        try:
            # Build the target URL
            target_url = f"http://{MANAGEMENT_HOST}:{MANAGEMENT_PORT}{self.path}"
            
            # Make the request to the management interface
            req = urllib.request.Request(target_url)
            req.add_header('User-Agent', 'Management-Proxy/1.0')
            
            with urllib.request.urlopen(req) as response:
                # Copy response headers
                for header, value in response.headers.items():
                    if header.lower() not in ['content-encoding', 'content-length', 'transfer-encoding']:
                        self.send_header(header, value)
                
                self.send_response(response.status)
                self.end_headers()
                
                # Copy response body
                self.wfile.write(response.read())
                
        except Exception as e:
            self.send_response(500)
            self.send_header('Content-type', 'text/html')
            self.end_headers()
            self.wfile.write(f'<h1>500 Error</h1><p>Error accessing management interface: {str(e)}</p>'.encode())

if __name__ == "__main__":
    print(f"🔐 LDK Node Management Proxy")
    print(f"   Username: {USERNAME}")
    print(f"   Password: {PASSWORD}")
    print(f"   Access at: http://localhost:{LOCAL_PORT}")
    print(f"   Target: {MANAGEMENT_HOST}:{MANAGEMENT_PORT}")
    print("")
    print("Press Ctrl+C to stop")
    print("")
    
    with socketserver.TCPServer(("", LOCAL_PORT), AuthHandler) as httpd:
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nShutting down proxy server...")
            sys.exit(0)
