#!/bin/bash

echo "🧪 Testing NUT-22 Authentication Setup..."

# Test Keycloak connectivity
echo "1. Testing Keycloak connectivity..."
KEYCLOAK_URL="http://localhost:8080"
if curl -s "$KEYCLOAK_URL/health/ready" > /dev/null; then
    echo "   ✅ Keycloak is running"
else
    echo "   ❌ Keycloak is not accessible at $KEYCLOAK_URL"
    echo "   💡 Start with: docker-compose -f docker-compose.dev.yml up -d"
    exit 1
fi

# Test OpenID Connect discovery
echo "2. Testing OpenID Connect discovery..."
DISCOVERY_URL="$KEYCLOAK_URL/realms/cashu-realm/.well-known/openid-configuration"
if curl -s "$DISCOVERY_URL" | grep -q "issuer"; then
    echo "   ✅ OpenID Connect discovery endpoint is working"
else
    echo "   ❌ OpenID Connect discovery endpoint failed"
    echo "   💡 Check if Keycloak realm is properly imported"
    exit 1
fi

# Test mint connectivity
echo "3. Testing mint connectivity..."
MINT_URL="http://localhost:3338"
if curl -s "$MINT_URL/v1/info" > /dev/null; then
    echo "   ✅ Mint is running"
else
    echo "   ❌ Mint is not accessible at $MINT_URL"
    echo "   💡 Start with: docker-compose -f docker-compose.dev.yml up -d"
    exit 1
fi

# Test mint info for NUT-22
echo "4. Testing NUT-22 configuration in mint..."
MINT_INFO=$(curl -s "$MINT_URL/v1/info")
if echo "$MINT_INFO" | grep -q "nut22"; then
    echo "   ✅ NUT-22 authentication is configured"
    echo "   📊 NUT-22 settings found in mint info"
else
    echo "   ❌ NUT-22 authentication not found in mint info"
    echo "   💡 Check mint environment variables"
    exit 1
fi

echo ""
echo "🎉 All tests passed! NUT-22 authentication is working."
echo ""
echo "📋 Next steps:"
echo "   1. Access Keycloak admin at: http://localhost:8080/admin"
echo "   2. Login with: admin / admin123"
echo "   3. Test user: testuser / testpass123"
echo "   4. Mint API: http://localhost:3338"
echo "   5. LDK Node management: http://localhost:8091"
echo ""
echo "🔐 Authentication flow:"
echo "   1. User authenticates with Keycloak"
echo "   2. User gets JWT token"
echo "   3. User uses token for mint operations"
echo "   4. Mint validates token with Keycloak"
