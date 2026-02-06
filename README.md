# Healthcare AI Platform

A microservices-based AI platform for healthcare applications, featuring RAG (Retrieval-Augmented Generation), multimodal AI capabilities, and document processing.

## 🏗️ Architecture

### Microservices
- **gateway-service** (Port 8080) - API Gateway with routing, rate limiting, and circuit breaker
- **discovery-service** (Port 8761) - Eureka service registry
- **retrieval-service** (Port 8083) - RAG queries, AI orchestration, video/image generation
- **embedding-service** - Document embedding and vector storage
- **document-processor** - Async document processing via Kafka
- **shared-lib** - Common utilities and models

### Infrastructure
- **PostgreSQL** (Port 5432) - Vector database with pgvector extension
- **Kafka + Zookeeper** (Ports 9092, 2181) - Event streaming
- **Redis** (Port 6379) - Caching and rate limiting
- **Zipkin** (Port 9411) - Distributed tracing
- **Prometheus** (Port 9090) - Metrics collection
- **Grafana** (Port 3000) - Observability dashboards
- **Loki + Promtail** (Port 3100) - Log aggregation

## 🚀 Quick Start

### Prerequisites
- Docker & Docker Compose
- Java 21
- Maven 3.x
- Google Cloud Service Account (for Vertex AI features)

### Setup

1. **Clone and navigate to project**
```bash
cd HealthCareAIAgent
```

2. **Configure environment variables**
Create `.env` file:
```bash
OPENAI_API_KEY=your_openai_key
OPENAI_ORG_KEY=your_org_key
OPENAI_PROJECT_ID=your_project_id
GEMINI_PROJECT_ID=your_gcp_project_id
```

3. **Add Google Cloud credentials**
Place your service account key at:
```
secrets/my-project-key..json
```

4. **Build services**
```bash
mvn clean package -DskipTests
```

5. **Start infrastructure**
```bash
docker-compose up -d
```

## 📋 Available Endpoints

### Health & AI Services
```bash
# Vegetarian diet plan generation
GET http://localhost:8080/api/v1/health/vegetarian-diet/{request}

# Image generation
GET http://localhost:8080/api/v1/health/multimodel-media/image/{prompt}

# Video generation (Vertex AI)
POST http://localhost:8080/api/v1/health/multimodel-media/video/{prompt}
Content-Type: application/json

# Text to audio
POST http://localhost:8080/api/v1/health/textToAudio
Content-Type: application/json
{
  "text": "Your text here"
}

# Audio to text
POST http://localhost:8080/api/v1/health/audioToText
Content-Type: multipart/form-data
audio: <file>
```

### RAG (Retrieval-Augmented Generation)
```bash
# Ingest document
POST http://localhost:8080/api/rag/ingest
Content-Type: multipart/form-data
file: <pdf-file>

# Query documents
POST http://localhost:8080/api/rag/query
Content-Type: application/json
{
  "query": "Your question here"
}
```

## 🔄 Execution Flow

### 1. Document Ingestion Flow
```
User Upload → Gateway → Retrieval Service
                           ↓
                    Save to Upload Dir
                           ↓
                    Publish to Kafka
                           ↓
                    Document Processor
                           ↓
                    Extract & Chunk Text
                           ↓
                    Publish Chunks to Kafka
                           ↓
                    Embedding Service
                           ↓
                    Generate Embeddings (OpenAI)
                           ↓
                    Store in PGVector DB
```

### 2. RAG Query Flow
```
User Query → Gateway → Retrieval Service
                          ↓
                   Generate Query Embedding
                          ↓
                   Vector Similarity Search (PGVector)
                          ↓
                   Retrieve Top-K Documents
                          ↓
                   Build Context + Prompt
                          ↓
                   Send to OpenAI Chat API
                          ↓
                   Return AI Response
```

### 3. Video Generation Flow
```
User Request → Gateway → Retrieval Service
                            ↓
                     GeminiVideoClient
                            ↓
                     Load GCP Credentials
                            ↓
                     Get Access Token
                            ↓
                     Call Vertex AI Imagen API
                            ↓
                     Poll for Completion
                            ↓
                     Return Video URL/Data
```

## 🛠️ Technology Stack

- **Framework**: Spring Boot 3.4.2, Spring Cloud 2024.0.0
- **AI/ML**: Spring AI 1.0.3, OpenAI API, Google Vertex AI
- **Database**: PostgreSQL with pgvector extension
- **Messaging**: Apache Kafka 3.8.1
- **Service Discovery**: Netflix Eureka
- **API Gateway**: Spring Cloud Gateway
- **Observability**: Zipkin, Prometheus, Grafana, Loki
- **Containerization**: Docker, Docker Compose

## 📊 Monitoring

- **Eureka Dashboard**: http://localhost:8761
- **Grafana**: http://localhost:3000
- **Prometheus**: http://localhost:9090
- **Zipkin**: http://localhost:9411
- **Kafka UI**: http://localhost:9192

## 🔧 Development

### Build specific service
```bash
cd retrieval-service
mvn clean package
```

### Rebuild Docker image
```bash
docker-compose build retrieval-service
docker-compose up -d retrieval-service
```

### View logs
```bash
docker logs -f retrieval-service
docker logs -f gateway-service
```

## 📝 Configuration

### Key Configuration Files
- `docker-compose.yml` - Service orchestration
- `application.yml` - Service-specific configs
- `.env` - Environment variables
- `secrets/` - Credential files

### Database Connection
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vectordb
    username: postgres
    password: root
```

## 🐛 Troubleshooting

### Service won't start
```bash
# Check logs
docker logs <service-name>

# Restart services
docker-compose restart
```

### Port conflicts
```bash
# Check port usage
netstat -ano | findstr :8080

# Stop conflicting process
taskkill /PID <pid> /F
```

### Eureka registration issues
Wait 30-60 seconds for service registration to propagate through the discovery service.

## 📄 License

This project is licensed under the MIT License.
