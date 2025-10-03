# Use the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.6

# Copy the realm export files
COPY keycloak-export/ /opt/keycloak/data/import/

# Set the command to start Keycloak with realm import
CMD ["start", "--import-realm"]
