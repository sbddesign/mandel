#!/usr/bin/env bash
set -euo pipefail

# Prepare data directories
mkdir -p "${MINT_DATA_DIR:-/data}"
mkdir -p "${LDK_PERSISTENCE_DIR:-/data/ldk}"

ARGS=(
  "--bind" "${MINT_HTTP_BIND:-0.0.0.0:8080}"
  "--data-dir" "${MINT_DATA_DIR:-/data}"
  "--ldk-network" "${LDK_NETWORK:-mutinynet}"
  "--ldk-esplora" "${LDK_ESPLORA_URL:-https://mutinynet.ltbl.io/esplora}"
  "--ldk-rgs" "${LDK_RGS_URL:-wss://mutinynet.ltbl.io/ldk-rgs}"
  "--ldk-persistence-dir" "${LDK_PERSISTENCE_DIR:-/data/ldk}"
  "--ldk-blockchain-backend" "${LDK_BLOCKCHAIN_BACKEND:-esplora}"
)

if [[ -n "${LDK_SEED_HEX:-}" ]]; then
  ARGS+=("--ldk-seed-hex" "${LDK_SEED_HEX}")
fi

exec "$@" "${ARGS[@]}"

