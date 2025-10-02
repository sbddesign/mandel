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

5. **Access LDK Node management interface:**
   Open your browser and go to: http://localhost:8091

## Configuration

The mint server uses the CDK mintd with LDK Node Lightning backend. Key configuration:

- **Lightning Backend**: LDK Node (MutinyNet Signet)
- **Database**: SQLite (persistent)
- **Mint API Port**: 3338
- **LDK Node Management**: 8091
- **Network**: MutinyNet Signet via Esplora
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

## Railway Deployment

This backend is configured for deployment on Railway. Follow these steps:

### 1. Deploy to Railway

1. **Connect your repository to Railway:**
   - Go to [Railway](https://railway.app)
   - Create a new project
   - Connect your GitHub repository
   - Railway will automatically detect the Dockerfile in the `/backend` directory

2. **Set environment variables:**
   - In Railway dashboard, go to your service
   - Add these environment variables:
     ```
     CDK_MINTD_URL=https://your-app-name.railway.app
     CDK_MINTD_MNEMONIC=your-secure-mnemonic-here
     ```

3. **Deploy:**
   - Railway will automatically build and deploy your container
   - The mint API will be available at your Railway URL
   - The LDK Node management interface will be available at `https://your-app-name.railway.app:8091`

### 2. Railway Configuration

The deployment uses:
- **Dockerfile**: Located in `/backend/Dockerfile`
- **Railway config**: `/backend/railway.json`
- **Ignore file**: `.railwayignore` (excludes Kotlin files)

### 3. Environment Variables

Key environment variables for Railway:
- `CDK_MINTD_URL`: Your Railway app URL
- `CDK_MINTD_MNEMONIC`: Secure mnemonic for the mint
- All other variables have sensible defaults

## Integration with Frontend

The mint server will be available at:
- **Local development**: `http://localhost:3338`
- **Railway production**: `https://your-app-name.railway.app`
