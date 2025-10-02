# Mandel Backend - Cashu Mint

This directory contains the backend configuration for the Mandel ecash wallet, using the Cashu mint server with LDK Node Lightning backend.

## Quick Start

1. **Copy environment file:**
   ```bash
   cp env.example .env
   ```

2. **Generate a mint private key (if needed):**
   ```bash
   python3 -c "import secrets; print('MINT_PRIVATE_KEY=' + secrets.token_hex(32))" >> .env
   ```

3. **Start the mint server:**
   ```bash
   docker compose up -d
   ```

4. **Check if the server is running:**
   ```bash
   curl http://localhost:3338/v1/info
   ```

## Configuration

The mint server uses the CDK mintd with LDK Node Lightning backend. Key configuration:

- **Lightning Backend**: LDK Node (testnet)
- **Database**: SQLite (persistent)
- **Port**: 3338
- **Network**: Bitcoin testnet via Esplora
- **Mnemonic**: Default test mnemonic (change for production)

The server automatically generates a private key and mnemonic if not provided in the `.env` file.

## API Endpoints

The mint server exposes the following main endpoints:

- `GET /v1/info` - Get mint information
- `POST /v1/mint/bolt11` - Mint new ecash tokens (Bolt11)
- `POST /v1/mint/bolt12` - Mint new ecash tokens (Bolt12)
- `POST /v1/melt/bolt11` - Melt ecash tokens (Bolt11)
- `POST /v1/melt/bolt12` - Melt ecash tokens (Bolt12)
- `POST /v1/swap` - Swap tokens
- `POST /v1/check` - Check if tokens are valid

## Development

### Viewing Logs
```bash
docker compose logs -f mintd
```

### Stopping the Server
```bash
docker compose down
```

### Restarting the Server
```bash
docker compose restart mintd
```

### Database Persistence

The mint database is persisted in a Docker volume named `mint_data`. This ensures your mint state is preserved across container restarts.

## Integration with Frontend

The mint server will be available at `http://localhost:3338` for your frontend application to connect to.
