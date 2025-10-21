# 🚀 Guide de Démarrage - ETSIA Backend

Ce guide vous accompagne étape par étape pour lancer l'application correctement.

---

## 📋 Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- ✅ **Docker** (version 20.10+)
- ✅ **Docker Compose** (version 2.0+)
- ✅ **Java 21** (optionnel, uniquement pour développement local)
- ✅ **Git** (pour cloner le projet)

### Vérifier les installations

```bash
docker --version          # Docker version 20.10.x ou supérieur
docker-compose --version  # Docker Compose version 2.x ou supérieur
java --version           # openjdk 21 (optionnel)
```

---

## 🎯 Option 1 : Démarrage avec Docker (Recommandé)

Cette option lance **tout automatiquement** : PostgreSQL, Keycloak et l'application.

### Étape 1 : Cloner le projet

```bash
git clone <url-du-repo>
cd etsia-backend
```

### Étape 2 : Vérifier la structure

```bash
ls -la
# Vous devez voir :
# - Dockerfile
# - docker-compose.yml
# - database/migration/
# - application/
# - auth/, common/, post/, etc.
```

### Étape 3 : Lancer tous les services

```bash
docker-compose up -d
```

**Ce qui se passe en coulisses :**
1. 🐘 PostgreSQL démarre (port 5432)
2. 📊 Exécute les migrations SQL automatiquement
3. 🔐 Keycloak démarre (port 8080)
4. 🏗️ Build l'application Spring Boot
5. 🚀 Lance l'application (port 8085)

### Étape 4 : Surveiller les logs

```bash
# Voir tous les logs
docker-compose logs -f

# Voir uniquement les logs de l'application
docker-compose logs -f app

# Voir uniquement Keycloak
docker-compose logs -f keycloak

# Voir uniquement PostgreSQL
docker-compose logs -f postgres
```

### Étape 5 : Vérifier que tout fonctionne

```bash
# Vérifier le statut des services
docker-compose ps

# Tous les services doivent afficher "Up" et "healthy"
```

**Accès aux services :**
- 🌐 Application : http://localhost:8085
- 🔐 Keycloak Admin : http://localhost:8080 (admin/admin)
- 🗄️ PostgreSQL : localhost:5432 (postgres/qwertyuiop)

### Étape 6 : Configurer Keycloak (IMPORTANT)

⚠️ **Cette étape est OBLIGATOIRE** pour que l'authentification fonctionne.

#### 6.1 Se connecter à Keycloak

1. Ouvrir http://localhost:8080
2. Cliquer sur "Administration Console"
3. Login : `admin` / Password : `admin`

#### 6.2 Créer le Realm

1. Cliquer sur le menu déroulant en haut à gauche (actuellement "Master")
2. Cliquer sur "Create Realm"
3. **Realm name** : `yansnet`
4. Cliquer sur "Create"

#### 6.3 Créer le Client

1. Dans le menu de gauche, cliquer sur "Clients"
2. Cliquer sur "Create client"
3. Remplir :
   - **Client ID** : `yansnet-client-test-01`
   - **Client type** : OpenID Connect
4. Cliquer sur "Next"
5. Activer :
   - ✅ **Client authentication** : ON
   - ✅ **Authorization** : OFF
   - ✅ **Standard flow** : ON
   - ✅ **Direct access grants** : ON
6. Cliquer sur "Next"
7. **Valid redirect URIs** : `http://localhost:8085/*`
8. **Web origins** : `http://localhost:8085`
9. Cliquer sur "Save"

#### 6.4 Configurer le Client Secret

1. Aller dans l'onglet "Credentials" du client
2. Copier le "Client Secret" (ou le définir manuellement)
3. Pour simplifier, définir le secret à : `yansnet-client-test-01`

#### 6.5 Créer un utilisateur de test (optionnel)

1. Menu "Users" → "Add user"
2. Remplir :
   - **Username** : `test@etsia.com`
   - **Email** : `test@etsia.com`
   - **Email verified** : ON
3. Cliquer sur "Create"
4. Onglet "Credentials" → "Set password"
   - **Password** : `test123`
   - **Temporary** : OFF
5. Cliquer sur "Save"

### Étape 7 : Tester l'application

#### 7.1 Vérifier la santé de l'application

```bash
curl http://localhost:8085/actuator/health
# Doit retourner : {"status":"UP"}
```

#### 7.2 Tester l'inscription

```bash
curl -X POST http://localhost:8085/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@etsia.com",
    "password": "SecurePass123!",
    "phoneNumber": "+33612345678"
  }'
```

**Réponse attendue :**
```json
{
  "userId": 1,
  "email": "john.doe@etsia.com",
  "token": "eyJhbGciOiJSUzI1NiIs..."
}
```

#### 7.3 Tester la connexion

```bash
curl -X POST http://localhost:8085/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@etsia.com",
    "password": "SecurePass123!"
  }'
```

**Réponse attendue :**
```json
{
  "userId": 1,
  "email": "john.doe@etsia.com",
  "token": "eyJhbGciOiJSUzI1NiIs..."
}
```

#### 7.4 Tester un endpoint protégé

```bash
# Remplacer <TOKEN> par le token obtenu lors du login
curl http://localhost:8085/api/users/profile \
  -H "Authorization: Bearer <TOKEN>"
```

### Étape 8 : Arrêter l'application

```bash
# Arrêter sans supprimer les données
docker-compose stop

# Arrêter et supprimer les containers (garde les données PostgreSQL)
docker-compose down

# Arrêter et tout supprimer (données incluses)
docker-compose down -v
```

---

## 🎯 Option 2 : Démarrage en Développement Local

Cette option lance PostgreSQL et Keycloak via Docker, mais l'application tourne en local.

### Étape 1 : Lancer uniquement PostgreSQL et Keycloak

```bash
docker-compose up -d postgres keycloak
```

### Étape 2 : Attendre que les services soient prêts

```bash
# Vérifier PostgreSQL
docker-compose exec postgres pg_isready -U postgres

# Vérifier Keycloak (attendre ~60 secondes)
curl http://localhost:8080/health/ready
```

### Étape 3 : Configurer Keycloak

Suivre les **Étapes 6.1 à 6.5** de l'Option 1.

### Étape 4 : Modifier application.properties (si nécessaire)

Fichier : `application/src/main/resources/application.properties`

```properties
# Database (doit pointer vers localhost)
spring.datasource.url=jdbc:postgresql://localhost:5432/yansnet-db
spring.datasource.username=postgres
spring.datasource.password=qwertyuiop

# Keycloak (doit pointer vers localhost)
keycloak.server-url=http://localhost:8080
spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/yansnet
```

### Étape 5 : Lancer l'application en local

```bash
# Option A : Via Gradle
./gradlew :application:bootRun

# Option B : Via IntelliJ IDEA
# Ouvrir le projet → Run 'Application'
```

### Étape 6 : Tester

L'application est accessible sur http://localhost:8085

Suivre les **Étapes 7.1 à 7.4** de l'Option 1.

---

## 🎯 Option 3 : Démarrage avec Base de Données Distante

Si vous utilisez la base Neon (Azure) configurée dans application.properties.

### Étape 1 : Vérifier la configuration

Fichier : `application/src/main/resources/application.properties`

```properties
# Database distante (déjà configuré)
spring.datasource.url=jdbc:postgresql://ep-snowy-water-a8xsouqx-pooler.eastus2.azure.neon.tech/neondb?user=neondb_owner&password=npg_2iUtxskjoF3M&sslmode=require&channelBinding=require
```

### Étape 2 : Lancer uniquement Keycloak

```bash
docker-compose up -d keycloak
```

⚠️ **Problème** : Keycloak a besoin de PostgreSQL. Deux solutions :

**Solution A** : Utiliser PostgreSQL local pour Keycloak

Modifier `docker-compose.yml` pour laisser Keycloak utiliser postgres local :

```yaml
keycloak:
  environment:
    KC_DB: postgres
    KC_DB_URL: jdbc:postgresql://postgres:5432/yansnet-db
```

**Solution B** : Utiliser Keycloak en mode dev sans DB externe

```bash
docker run -d \
  -p 8080:8080 \
  -e KEYCLOAK_ADMIN=admin \
  -e KEYCLOAK_ADMIN_PASSWORD=admin \
  --name keycloak \
  quay.io/keycloak/keycloak:26.0 start-dev
```

### Étape 3 : Configurer Keycloak

Suivre les **Étapes 6.1 à 6.5** de l'Option 1.

### Étape 4 : Lancer l'application

```bash
./gradlew :application:bootRun
```

---

## 🔧 Dépannage

### Problème 1 : Port déjà utilisé

**Erreur** : `Bind for 0.0.0.0:8085 failed: port is already allocated`

**Solution** :
```bash
# Trouver le processus utilisant le port
lsof -i :8085  # ou 8080, 5432

# Tuer le processus
kill -9 <PID>

# Ou changer le port dans docker-compose.yml
ports:
  - "8086:8085"  # Utiliser 8086 au lieu de 8085
```

### Problème 2 : Keycloak ne démarre pas

**Erreur** : `Keycloak unhealthy`

**Solution** :
```bash
# Voir les logs Keycloak
docker-compose logs keycloak

# Redémarrer Keycloak
docker-compose restart keycloak

# Attendre 90 secondes (start_period)
```

### Problème 3 : Application ne se connecte pas à PostgreSQL

**Erreur** : `Connection refused` ou `Unknown database`

**Solution** :
```bash
# Vérifier que PostgreSQL est prêt
docker-compose exec postgres pg_isready

# Vérifier que la base existe
docker-compose exec postgres psql -U postgres -c "\l"

# Recréer la base si nécessaire
docker-compose down -v
docker-compose up -d postgres
```

### Problème 4 : Erreur JWT / Token invalide

**Erreur** : `Unable to validate JWT` ou `Invalid token`

**Solution** :
1. Vérifier que le realm `yansnet` existe dans Keycloak
2. Vérifier que l'issuer-uri est correct :
   ```properties
   spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8080/realms/yansnet
   ```
3. Tester l'endpoint Keycloak :
   ```bash
   curl http://localhost:8080/realms/yansnet/.well-known/openid-configuration
   ```

### Problème 5 : Build Gradle échoue

**Erreur** : `Gradle build failed`

**Solution** :
```bash
# Nettoyer le cache Gradle
./gradlew clean

# Rebuild
./gradlew :application:build

# Si problème de dépendances
rm -rf ~/.gradle/caches
./gradlew --refresh-dependencies build
```

### Problème 6 : Migrations SQL ne s'appliquent pas

**Solution** :
```bash
# Vérifier que les fichiers SQL existent
ls -la database/migration/

# Supprimer et recréer la base
docker-compose down -v
docker-compose up -d postgres

# Attendre 10 secondes puis vérifier
docker-compose exec postgres psql -U postgres -d yansnet-db -c "\dt"
```

---

## 📊 Monitoring

### Voir l'utilisation des ressources

```bash
docker stats etsia-backend etsia-postgres etsia-keycloak
```

### Vérifier les logs en temps réel

```bash
# Tous les services
docker-compose logs -f

# Application uniquement
docker-compose logs -f app | grep ERROR
```

### Vérifier la base de données

```bash
# Se connecter à PostgreSQL
docker-compose exec postgres psql -U postgres -d yansnet-db

# Lister les tables
\dt

# Voir les utilisateurs créés
SELECT * FROM users;

# Quitter
\q
```

---

## 🎓 Bonnes Pratiques

### 1. Toujours vérifier les logs après démarrage

```bash
docker-compose logs -f app
```

Attendez de voir : `Started Application in X.XXX seconds`

### 2. Ne pas commit les credentials

Les credentials actuels sont pour le **développement uniquement**.

En production :
- Changer tous les mots de passe
- Utiliser des secrets (Docker Secrets, Kubernetes Secrets)
- Utiliser des variables d'environnement

### 3. Sauvegarder les données PostgreSQL

```bash
# Exporter la base
docker-compose exec postgres pg_dump -U postgres yansnet-db > backup.sql

# Restaurer
docker-compose exec -T postgres psql -U postgres yansnet-db < backup.sql
```

### 4. Utiliser des profils Spring

Créer `application-dev.properties` et `application-prod.properties` :

```bash
# Développement
./gradlew :application:bootRun --args='--spring.profiles.active=dev'

# Production (via Docker)
docker-compose up -d  # Utilise SPRING_PROFILES_ACTIVE=prod
```

---

## 🎯 Checklist de Démarrage Complet

- [ ] Docker et Docker Compose installés
- [ ] Projet cloné
- [ ] `docker-compose up -d` exécuté
- [ ] Tous les services "healthy" (`docker-compose ps`)
- [ ] Keycloak accessible (http://localhost:8080)
- [ ] Realm `yansnet` créé dans Keycloak
- [ ] Client `yansnet-client-test-01` créé
- [ ] Application accessible (http://localhost:8085)
- [ ] Test `/auth/register` réussi
- [ ] Test `/auth/login` réussi
- [ ] Token JWT reçu et valide

---

## 📞 Support

Si vous rencontrez des problèmes :

1. Vérifier les logs : `docker-compose logs -f`
2. Vérifier le statut : `docker-compose ps`
3. Consulter `CLAUDE.md` pour l'architecture
4. Consulter `auth/README.md` pour Keycloak

---

**Version** : 1.0
**Dernière mise à jour** : 2025-10-21
**Testé avec** : Docker 24.0.x, Docker Compose 2.x, Java 21
