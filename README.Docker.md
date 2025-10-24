# 🐳 Guide Docker - ETSIA Backend

## 📦 Fichiers Docker

- **Dockerfile**: Build multi-stage de l'application
- **docker-compose.yml**: Orchestration complète (PostgreSQL + Keycloak + App)
- **.dockerignore**: Optimisation du build

## 🚀 Démarrage rapide

### Option 1: Docker Compose (Recommandé)

Lance automatiquement PostgreSQL, Keycloak et l'application:

```bash
# Démarrer tous les services
docker-compose up -d

# Voir les logs
docker-compose logs -f app

# Vérifier le statut
docker-compose ps
```

### Option 2: Build et run manuel

```bash
# Build l'image
docker build -t etsia-backend:latest .

# Run le container (nécessite PostgreSQL et Keycloak externes)
docker run -d \
  -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/yansnet-db \
  -e KEYCLOAK_SERVER_URL=http://localhost:8080 \
  --name etsia-app \
  etsia-backend:latest
```

## 📋 Services disponibles

| Service | URL | Credentials |
|---------|-----|-------------|
| **Application** | http://localhost:8085 | - |
| **Keycloak Admin** | http://localhost:8080 | admin/admin |
| **PostgreSQL** | localhost:5432 | postgres/qwertyuiop |

## 🔧 Configuration

### Variables d'environnement

Vous pouvez personnaliser via `.env` ou directement dans `docker-compose.yml`:

```bash
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/yansnet-db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=qwertyuiop

# Keycloak
KEYCLOAK_SERVER_URL=http://keycloak:8080
KEYCLOAK_REALM=yansnet
KEYCLOAK_CLIENT_ID=yansnet-client-test-01

# JVM
JAVA_OPTS=-Xmx512m -Xms256m
```

## 🏗️ Build optimisé

Le Dockerfile utilise un **build multi-stage**:

1. **Stage 1 (builder)**: Compile avec Gradle + JDK 21
2. **Stage 2 (runtime)**: Image légère avec JRE 21 seulement

Avantages:
- ✅ Image finale < 300MB (vs ~800MB avec JDK)
- ✅ Build en cache (dépendances Gradle)
- ✅ Sécurité: utilisateur non-root

## 🧪 Tests et développement

### Lancer uniquement PostgreSQL et Keycloak

```bash
docker-compose up -d postgres keycloak
./gradlew :application:bootRun  # Run en local
```

### Rebuild après modifications

```bash
docker-compose up -d --build app
```

### Accéder aux logs

```bash
# Logs de l'application
docker-compose logs -f app

# Logs Keycloak
docker-compose logs -f keycloak

# Logs PostgreSQL
docker-compose logs -f postgres
```

## 🗄️ Données persistantes

Les données PostgreSQL sont stockées dans un volume Docker:

```bash
# Voir les volumes
docker volume ls

# Supprimer les données (⚠️ destructif)
docker-compose down -v
```

## 🔍 Healthchecks

Tous les services ont des healthchecks configurés:

```bash
# Vérifier la santé des services
docker-compose ps

# Healthcheck manuel
curl http://localhost:8085/actuator/health
```

## 🛑 Arrêter les services

```bash
# Arrêter sans supprimer
docker-compose stop

# Arrêter et supprimer les containers
docker-compose down

# Tout supprimer (containers + volumes + images)
docker-compose down -v --rmi all
```

## 🐛 Troubleshooting

### L'application ne démarre pas

```bash
# Vérifier les logs
docker-compose logs app

# Vérifier que PostgreSQL est prêt
docker-compose exec postgres pg_isready -U postgres

# Vérifier Keycloak
curl http://localhost:8080/health/ready
```

### Erreur de connexion à la base

- Vérifier que PostgreSQL est lancé: `docker-compose ps`
- Vérifier l'URL de connexion dans les variables d'environnement
- Attendre ~10s que les healthchecks passent au vert

### Erreur Keycloak JWT

- Vérifier que Keycloak est accessible: `http://localhost:8080`
- Configurer le realm `yansnet` (voir `auth/README.md`)
- Vérifier l'issuer-uri dans les logs de l'application

## 📊 Monitoring

### Voir la consommation de ressources

```bash
docker stats etsia-backend etsia-postgres etsia-keycloak
```

### Entrer dans un container

```bash
# Shell dans l'application
docker-compose exec app sh

# Shell dans PostgreSQL
docker-compose exec postgres psql -U postgres -d yansnet-db

# Shell dans Keycloak
docker-compose exec keycloak bash
```

## 🚀 Production

Pour la production, pensez à:

1. **Changer les credentials** (PostgreSQL, Keycloak)
2. **Utiliser des secrets** (Docker Secrets, Kubernetes Secrets)
3. **Activer HTTPS** (reverse proxy Nginx/Traefik)
4. **Configurer les limites de ressources**:

```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
```

5. **Sauvegardes automatiques** de PostgreSQL
6. **Monitoring** (Prometheus, Grafana)
7. **Logs centralisés** (ELK Stack, Loki)

## 📝 Notes

- Le port **8085** est utilisé (configuré dans `application.properties`)
- Les migrations SQL dans `database/migration/` sont exécutées au démarrage de PostgreSQL
- Keycloak utilise PostgreSQL comme base de données (mode production-ready)
- L'application attend que PostgreSQL et Keycloak soient "healthy" avant de démarrer

---

**Dernière mise à jour**: 2025-10-21
