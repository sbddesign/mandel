# syntax=docker/dockerfile:1

FROM lukemathwalker/cargo-chef:latest-rust-1.80 as chef
WORKDIR /app

# Install build dependencies
RUN apt-get update && apt-get install -y cmake clang pkg-config libssl-dev git && rm -rf /var/lib/apt/lists/*

# Fetch CDK source
ARG CDK_REPO=https://github.com/cashubtc/cdk.git
ARG CDK_REF=main
RUN git clone --depth 1 --branch ${CDK_REF} ${CDK_REPO} /app/cdk

FROM chef as planner
WORKDIR /app/cdk
RUN cargo chef prepare --recipe-path recipe.json

FROM chef as builder
WORKDIR /app/cdk
COPY --from=planner /app/cdk/recipe.json recipe.json
RUN cargo chef cook --release --recipe-path recipe.json

# Build cdk-mintd only
RUN cargo build -p cdk-mintd --release

FROM debian:bookworm-slim AS runtime
WORKDIR /app
RUN apt-get update && apt-get install -y ca-certificates && rm -rf /var/lib/apt/lists/*

COPY --from=builder /app/cdk/target/release/cdk-mintd /usr/local/bin/cdk-mintd
COPY ./entrypoint.sh /usr/local/bin/entrypoint.sh
RUN chmod +x /usr/local/bin/entrypoint.sh

ENV MINT_HTTP_BIND=0.0.0.0:8080 \
    MINT_DATA_DIR=/data \
    LDK_NETWORK=mutinynet \
    LDK_ESPLORA_URL=https://mutinynet.ltbl.io/esplora \
    LDK_RGS_URL=wss://mutinynet.ltbl.io/ldk-rgs \
    LDK_PERSISTENCE_DIR=/data/ldk \
    LDK_BLOCKCHAIN_BACKEND=esplora \
    LDK_SEED_HEX=

VOLUME ["/data"]
EXPOSE 8080

ENTRYPOINT ["/usr/local/bin/entrypoint.sh"]
CMD ["cdk-mintd"]

