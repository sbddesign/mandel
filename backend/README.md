# Mandel Backend - Cashu Mint

This directory contains the backend configuration for the Mandel ecash wallet, using the Cashu mint server.

## Quick Start

1. **Copy environment file:**
   ```bash
   cp env.example .env
   ```

2. **Generate a mint private key:**
   ```bash
   python3 -c "import secrets; print(secrets.token_hex(32))"
   ```
   Copy the output and paste it as the value for `MINT_PRIVATE_KEY` in your `.env` file.

3. **Start the mint server:**
   ```bash
   docker-compose up -d
   ```

4. **Check if the server is running:**
   ```bash
   curl http://localhost:3338/info
   ```

## Configuration

The mint server can be configured through environment variables in the `.env` file:

- `MINT_PRIVATE_KEY`: Private key for the mint (required)
- `MINT_DB_PATH`: Path to the SQLite database (default: `/data/mint.db`)
- `MINT_HOST`: Host to bind to (default: `0.0.0.0`)
- `MINT_PORT`: Port to listen on (default: `3338`)
- `MINT_LIGHTNING_BACKEND`: Lightning backend to use (`fake`, `lnd`, `clightning`, `eclair`)
- `MINT_LIGHTNING_URL`: URL to your Lightning node (if using real Lightning)
- `MINT_LIGHTNING_TOKEN`: Authentication token for your Lightning node

## API Endpoints

The mint server exposes the following main endpoints:

- `GET /info` - Get mint information
- `POST /mint` - Mint new ecash tokens
- `POST /melt` - Melt ecash tokens (redeem for Lightning)
- `POST /check` - Check if tokens are valid
- `POST /split` - Split tokens
- `POST /swap` - Swap tokens

## Development

### Viewing Logs
```bash
docker-compose logs -f mintd
```

### Stopping the Server
```bash
docker-compose down
```

### Restarting the Server
```bash
docker-compose restart mintd
```

### Database Persistence

The mint database is persisted in a Docker volume named `mint_data`. This ensures your mint state is preserved across container restarts.

## Integration with Frontend

The mint server will be available at `http://localhost:3338` for your frontend application to connect to.
