# 🔧 Configuration - ETSIA Backend

Ce guide explique comment configurer l'application en local et en production sans exposer les secrets.

---

## 📋 Fichiers de configuration

### Fichiers commitables sur Git (sûrs) ✅

- **`.env.example`** : Template de variables d'environnement
- **`application.properties`** : Configuration avec variables d'environnement
- **`application.properties.template`** : Template Spring Boot
- **`.gitignore`** : Exclut les fichiers secrets

### Fichiers locaux (NE PAS commiter) ❌

- **`.env`** : Vos valeurs locales réelles
- **`application-dev.properties`** : Backup de votre config locale
- **`application-local.properties`** : Configuration locale alternative

---

## 🚀 Configuration pour développement local

### Option 1 : Utiliser le fichier `.env` (Recommandé)

1. **Copier le template** :
```bash
cp .env.example .env
```

2. **Remplir vos valeurs** dans `.env` :
```properties
DB_PASSWORD=votre_mot_de_passe
KEYCLOAK_ADMIN_PASSWORD=admin
KEYCLOAK_CLIENT_SECRET=votre_secret_keycloak
```

3. **Charger les variables** :
```bash
# Linux/Mac - Charger les variables
export $(cat .env | xargs)

# Ou utiliser un outil comme direnv
# https://direnv.net/

# Windows PowerShell
Get-Content .env | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        [System.Environment]::SetEnvironmentVariable($matches[1], $matches[2])
    }
}
```

4. **Lancer l'application** :
```bash
./gradlew :application:bootRun
```

---

### Option 2 : Utiliser `application-dev.properties`

1. **Le fichier existe déjà** avec vos valeurs actuelles
2. **Lancer avec le profil dev** :
```bash
./gradlew :application:bootRun --args='--spring.profiles.active=dev'
```

---

### Option 3 : Variables d'environnement directes

```bash
# Définir les variables
export DB_PASSWORD=qwertyuiop
export KEYCLOAK_CLIENT_SECRET=sgMns0T0segANXRZxfy1LjZOUeIJ83Lp

# Lancer l'application
./gradlew :application:bootRun
```

---

## 🐳 Configuration avec Docker Compose

Les variables sont déjà configurées dans `docker-compose.yml` :

```yaml
app:
  environment:
    DB_URL: jdbc:postgresql://postgres:5432/yansnet-db
    DB_USERNAME: postgres
    DB_PASSWORD: qwertyuiop
    KEYCLOAK_CLIENT_SECRET: sgMns0T0segANXRZxfy1LjZOUeIJ83Lp
```

**Pour utiliser un fichier `.env` avec Docker Compose** :

```yaml
app:
  env_file:
    - .env
```

Puis lancer :
```bash
docker-compose up -d
```

---

## 🌐 Configuration pour production

### Variables d'environnement requises

| Variable | Description | Exemple |
|----------|-------------|---------|
| `DB_URL` | URL de connexion PostgreSQL | `jdbc:postgresql://prod-db:5432/yansnet` |
| `DB_USERNAME` | Utilisateur PostgreSQL | `app_user` |
| `DB_PASSWORD` | Mot de passe PostgreSQL | `************` |
| `KEYCLOAK_SERVER_URL` | URL du serveur Keycloak | `https://auth.example.com` |
| `KEYCLOAK_ADMIN_PASSWORD` | Mot de passe admin Keycloak | `************` |
| `KEYCLOAK_CLIENT_SECRET` | Secret du client OAuth2 | `************` |
| `JWT_ISSUER_URI` | URI de l'émetteur JWT | `https://auth.example.com/realms/yansnet` |

### Déploiement sur différentes plateformes

#### **Heroku**

```bash
heroku config:set DB_URL=jdbc:postgresql://...
heroku config:set DB_PASSWORD=***
heroku config:set KEYCLOAK_CLIENT_SECRET=***
```

#### **AWS Elastic Beanstalk**

Configurer dans le fichier `.ebextensions/environment.config` :
```yaml
option_settings:
  - namespace: aws:elasticbeanstalk:application:environment
    option_name: DB_PASSWORD
    value: your_password
```

#### **Kubernetes**

Créer un Secret :
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: etsia-secrets
type: Opaque
stringData:
  DB_PASSWORD: qwertyuiop
  KEYCLOAK_CLIENT_SECRET: sgMns0T0segANXRZxfy1LjZOUeIJ83Lp
```

Référencer dans le Deployment :
```yaml
env:
  - name: DB_PASSWORD
    valueFrom:
      secretKeyRef:
        name: etsia-secrets
        key: DB_PASSWORD
```

#### **Docker Swarm**

```bash
echo "qwertyuiop" | docker secret create db_password -
echo "sgMns0T0segANXRZxfy1LjZOUeIJ83Lp" | docker secret create keycloak_secret -
```

---

## 🔐 Bonnes pratiques de sécurité

### ✅ À FAIRE

1. **Toujours utiliser des variables d'environnement** en production
2. **Ne jamais commiter** `.env` ou `application-dev.properties`
3. **Changer les secrets** avant de déployer en production
4. **Utiliser des secrets managers** (Vault, AWS Secrets Manager, etc.)
5. **Limiter les permissions** des utilisateurs DB
6. **Activer HTTPS** en production
7. **Faire des backups** réguliers

### ❌ À ÉVITER

1. ❌ Commiter des mots de passe en clair sur Git
2. ❌ Utiliser les mêmes secrets en dev et prod
3. ❌ Partager les secrets par email/Slack
4. ❌ Laisser les valeurs par défaut (`admin`/`admin`)
5. ❌ Exposer les endpoints sensibles sans authentification

---

## 🆘 Dépannage

### Problème : Variables d'environnement non chargées

**Solution** :
```bash
# Vérifier que les variables sont définies
echo $DB_PASSWORD

# Si vide, recharger le .env
export $(cat .env | xargs)
```

### Problème : Application ne démarre pas (erreur de connexion DB)

**Vérifier** :
1. PostgreSQL est démarré : `docker-compose ps`
2. Les variables sont correctes : `echo $DB_URL`
3. Le mot de passe correspond : tester avec `psql`

### Problème : Keycloak JWT invalide

**Vérifier** :
1. `KEYCLOAK_CLIENT_SECRET` correspond à celui dans Keycloak
2. Le realm `yansnet` existe
3. L'issuer URI est correct

---

## 📚 Ressources

- **Spring Boot Externalized Configuration** : https://docs.spring.io/spring-boot/reference/features/external-config.html
- **12-Factor App Config** : https://12factor.net/config
- **Docker Secrets** : https://docs.docker.com/engine/swarm/secrets/

---

## 📞 Pour les nouveaux développeurs

1. **Cloner le repo** :
```bash
git clone <url>
cd etsia-backend
```

2. **Copier le template** :
```bash
cp .env.example .env
```

3. **Demander les secrets** à un membre de l'équipe (NE PAS les partager publiquement)

4. **Remplir le `.env`** avec les valeurs reçues

5. **Lancer l'application** :
```bash
./gradlew :application:bootRun
```

---

**Dernière mise à jour** : 2025-10-21
**Version** : 1.0
