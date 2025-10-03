# Use the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.6

# Create the import directory
RUN mkdir -p /opt/keycloak/data/import

# Copy only the simplified realm export file
COPY keycloak-export/realm-export.json /opt/keycloak/data/import/

# Set the command to start Keycloak with realm import
CMD ["start", "--import-realm"]
