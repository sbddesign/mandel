Backend: Cashu CDK Mint + Keycloak + Auth Gateway

This backend runs a Cashu CDK mint (cdk-mintd) configured to use LDK Node on the Mutinynet Bitcoin test network, fronted by a simple OpenID Connect (OIDC) auth gateway that enforces an email blacklist. Keycloak provides the OIDC provider. Everything runs locally with Docker Compose and can be deployed to Railway as separate services.

Services
- cdk-mintd: Cashu Dev Kit mint daemon (Rust). Uses LDK Node for Lightning with Mutinynet.
- auth-gateway: Node.js reverse proxy enforcing OIDC auth and an email blacklist.
- keycloak: OIDC provider for user authentication.
- postgres: DB for Keycloak.

Quickstart (Local)
1) Copy env templates
   cp .env.example .env
   cp auth-gateway/.env.example auth-gateway/.env

2) Start the stack
   docker compose up --build

3) Endpoints
- Gateway: http://localhost:3000
- Mint (direct, bypassing auth): http://localhost:8080
- Keycloak: http://localhost:8081

4) Create an admin and test user
- Admin console: http://localhost:8081 → log in with KEYCLOAK_ADMIN / KEYCLOAK_ADMIN_PASSWORD from .env
- Realm: mandel (imported on boot). Add a user with an email not on BLACKLISTED_EMAILS and set a password.

5) Test a call through the gateway
- Get a token (password grant for local dev):
  curl -X POST "http://localhost:8081/realms/mandel/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "client_id=mandel-gateway" \
    -d "grant_type=password" \
    -d "username=<your-username>" \
    -d "password=<your-password>"

- Call the mint via the gateway:
  curl -H "Authorization: Bearer <access_token>" http://localhost:3000/

Authentication flow (dev)
1) Create a user in Keycloak (realm: mandel). Login to Keycloak admin at http://localhost:8081 using the admin credentials from .env.
2) Obtain a token for client mandel-gateway (public client). You can use the OIDC authorize/token endpoints with PKCE or use the admin console to test flows. Ensure the token includes the email claim.
3) Call the gateway with Authorization: Bearer <token>. Requests are blocked if the email is present in BLACKLISTED_EMAILS.

Mutinynet (LDK Node)
Provide Mutinynet LDK settings via environment variables in .env (e.g., network=mutinynet, Esplora/Electrum endpoints, seed). The Dockerfile and entrypoint forward these values to cdk-mintd. Update values for your environment.

Railway deployment
- Deploy each service (auth-gateway, cdk-mintd, keycloak, postgres) as separate Railway services.
- Use the same environment variables as in .env.example configured in each service.
- Expose only the gateway publicly; keep cdk-mintd internal or protected.

Notes
- The sample Keycloak config enables registration for convenience. For production, disable public registration and use your IdP.
- The gateway enforces a simple blacklist via the email claim in the ID/Access token.

References
- CDK mintd: https://github.com/cashubtc/cdk/tree/main/crates/cdk-mintd
- CDK Keycloak example compose: https://github.com/cashubtc/cdk/blob/main/misc/keycloak/docker-compose.yml

