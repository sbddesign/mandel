# Use the official Keycloak image
FROM quay.io/keycloak/keycloak:25.0.6

# Install jq for JSON processing
USER root
RUN microdnf install -y jq
USER keycloak

# Copy the realm export files
COPY keycloak-export/ /opt/keycloak/data/import/

# Copy the combine script
COPY combine-realm.sh /opt/keycloak/data/import/
RUN chmod +x /opt/keycloak/data/import/combine-realm.sh

# Create a combined realm-export.json file that Keycloak expects
RUN cd /opt/keycloak/data/import && ./combine-realm.sh

# Set the command to start Keycloak with realm import
CMD ["start", "--import-realm"]
