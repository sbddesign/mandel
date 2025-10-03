#!/bin/bash

# Combine realm and users data into a single realm-export.json file
cd /opt/keycloak/data/import

# Read the users data from cdk-test-realm-users-0.json
USERS_DATA=$(cat cdk-test-realm-users-0.json | jq '.users')

# Create a combined JSON file
jq --argjson users "$USERS_DATA" '. + {users: $users}' cdk-test-realm-realm.json > realm-export.json

echo "Created combined realm-export.json with users data"
