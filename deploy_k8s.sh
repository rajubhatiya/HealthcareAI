#!/bin/bash

# GKE Deployment Script
# Usage: ./deploy_k8s.sh <PROJECT_ID> <CLUSTER_NAME> <REGION>

set -e

PROJECT_ID=$1
CLUSTER_NAME=$2
REGION=$3
IMAGE_NAME="gcr.io/$PROJECT_ID/healthcare-agent"

if [ -z "$PROJECT_ID" ] || [ -z "$CLUSTER_NAME" ] || [ -z "$REGION" ]; then
    echo "Usage: ./deploy_k8s.sh <PROJECT_ID> <CLUSTER_NAME> <REGION>"
    exit 1
fi

echo "======================================================"
echo "Deploying Healthcare Agent to GKE"
echo "Project: $PROJECT_ID"
echo "Cluster: $CLUSTER_NAME"
echo "Region:  $REGION"
echo "======================================================"

# 1. Build and Submit Docker Image to Google Cloud Build (or Container Registry)
echo "Building and submitting image to GCR..."
gcloud builds submit --tag "$IMAGE_NAME:latest" .

# 2. Get Cluster Credentials
echo "Fetching cluster credentials..."
gcloud container clusters get-credentials "$CLUSTER_NAME" --region "$REGION" --project "$PROJECT_ID"

# 3. Update Deployment Manifest with the correct image name
# Note: Using sed to replace the placeholder in deployment.yaml
echo "Updating deployment manifest..."
sed -i "s|image: .*|image: $IMAGE_NAME:latest|g" k8s/deployment.yaml

# 4. Apply Kubernetes Manifests
echo "Applying Kubernetes manifests..."
kubectl apply -f k8s/

echo "======================================================"
echo "Deployment initiated successfully!"
echo "Check status with: kubectl get pods"
echo "Check service IP with: kubectl get service healthcare-agent-service"
echo "======================================================"
