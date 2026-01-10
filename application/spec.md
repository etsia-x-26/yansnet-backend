# Cahier des Charges
## Application de Réseau Social Universitaire - UniName Connect

---

## 1. Présentation du Projet

### 1.1 Contexte
UniName Connect est une application de réseau social destinée exclusivement à la communauté universitaire, facilitant la mise en relation entre étudiants actuels et alumni sur le modèle de LinkedIn, mais adaptée au contexte académique.

### 1.2 Objectifs
- Créer un espace d'échange privilégié entre étudiants et anciens élèves
- Faciliter le mentorat et l'accompagnement professionnel
- Permettre le partage d'opportunités (stages, emplois, petits jobs)
- Favoriser l'entraide académique et professionnelle
- Renforcer le sentiment d'appartenance à la communauté universitaire

### 1.3 Périmètre
Application mobile-first (iOS/Android) avec interface web responsive, réservée aux étudiants et alumni disposant d'une adresse email universitaire validée.

---

## 2. Spécifications Fonctionnelles

### 2.1 Authentification et Sécurité

#### 2.1.1 Connexion (Login)
**Écran concerné : Login Screen**

**Fonctionnalités :**
- Connexion via email universitaire + mot de passe
- Authentification via SSO (Google, Apple)
- Option "Se souvenir de moi"
- Validation automatique du domaine email universitaire
- Gestion des sessions sécurisées

**Contraintes techniques :**
- Chiffrement des mots de passe (bcrypt, Argon2)
- Limitation des tentatives de connexion (3-5 tentatives)
- Token JWT pour la gestion des sessions
- Expiration des sessions après inactivité (30 jours)

#### 2.1.2 Récupération de Mot de Passe
**Fonctionnalités :**
- Lien "Forgot Password?" sur l'écran de connexion
- Envoi d'un lien de réinitialisation par email universitaire
- Token de réinitialisation à usage unique (validité : 1h)
- Création d'un nouveau mot de passe avec critères de sécurité

**Contraintes :**
- Validation du format email universitaire
- Questions de sécurité optionnelles
- Historique des mots de passe (ne pas réutiliser les 5 derniers)

#### 2.1.3 Gestion des Comptes
**Spécificité :** Pas de création de compte utilisateur standard

**Processus :**
- Les comptes sont pré-créés par l'administration universitaire
- Import des données depuis le système d'information de l'université
- Première connexion : activation du compte + complétion du profil
- Les alumni conservent leur accès après diplomation

---

### 2.2 Écran d'Accueil (Onboarding)

**Écran concerné : Onboarding Welcome Screen**

**Fonctionnalités :**
- Écrans de présentation avec navigation par glissement (3 écrans)
- Indicateurs de progression (page indicators)
- Bouton "Get Started" menant à la connexion
- Lien "Log In" pour utilisateurs existants
- Possibilité de passer l'onboarding

**Contenu des écrans :**
1. Bienvenue et présentation de la plateforme
2. Fonctionnalités principales (mentorat, opportunités)
3. Appel à l'action (inscription/connexion)

---

### 2.3 Fil d'Actualité (Feed Principal)

**Écran concerné : Main Community Feed**

#### 2.3.1 Structure du Feed
**Composants :**
- Barre de recherche persistante
- Filtres par catégories (All, Internships, Alumni, Clubs, Research)
- Publications mixtes : posts d'étudiants, offres d'emploi, spotlights alumni
- Bouton flottant "+" pour créer une publication
- Système de réaction (like, commentaire, partage)

#### 2.3.2 Types de Publications

**A. Posts d'Étudiants/Alumni**
- Photo de profil + nom + statut (Junior, Alumni, etc.)
- Texte du post (max 500 caractères)
- Images (max 4 par post)
- Tags/hashtags
- Interactions : like, commentaire, partage
- Timestamp relatif

**B. Offres d'Emploi/Stages**
- Badge "Promoted" ou catégorie distincte
- Logo de l'entreprise/organisation
- Titre du poste
- Description courte
- Image d'illustration
- Bouton CTA ("Apply Now", "Learn More")
- Nombre de likes, commentaires, partages

**C. Alumni Spotlight**
- Encart mis en avant visuellement
- Photo professionnelle de l'alumni
- Badge "Alumni Spotlight"
- Nom + poste actuel + promotion
- Citation/témoignage
- Boutons "Connect" et "Message"

#### 2.3.3 Algorithme de Feed
**Critères de tri :**
- Chronologique par défaut
- Boost des contenus avec interactions récentes
- Mise en avant des contenus de connexions directes
- Priorisation des offres d'emploi pertinentes (basées sur le profil)

---

### 2.4 Découverte et Mise en Relation

**Écran concerné : Discover Students and Alumni**

#### 2.4.1 Recherche et Filtres
**Fonctionnalités :**
- Barre de recherche : nom, intérêts, compétences
- Filtres multiples :
    - Département/Filière
    - Année de promotion
    - Compétences (skills)
    - Localisation géographique
    - Statut (Étudiant/Alumni)
    - Disponibilité mentorat

**Affichage des résultats :**
- Liste scrollable verticale
- Card par personne avec :
    - Photo de profil
    - Nom complet
    - Statut (Junior, Alumni, etc.) + filière + année
    - Tags de compétences (max 3 visibles)
    - Bouton "Connect" ou statut "Pending"

#### 2.4.2 Suggestions de Connexions
**Algorithme de recommandation :**
- Même filière d'études
- Compétences complémentaires
- Intérêts communs
- Localisation géographique proche
- Connexions mutuelles
- Alumni disponibles pour mentorat

#### 2.4.3 Gestion des Connexions
**États possibles :**
- Non connecté : bouton "Connect"
- En attente : bouton "Pending" (désactivé)
- Connecté : accès au profil complet + messagerie

**Notifications :**
- Demande de connexion reçue
- Connexion acceptée
- Suggestion de nouvelles connexions

---

### 2.5 Profil Utilisateur

**Écran concerné : User Profile Screen**

#### 2.5.1 Affichage du Profil
**Section En-tête :**
- Photo de profil (avatar circulaire, 128x128px min)
- Badge de statut en ligne (vert = disponible)
- Nom complet
- Badge promotion ("Class of 2022")
- Filière d'études
- Badge "Mentor Available" si applicable
- Boutons d'action : "Message" et "Connect"

**Section Bio :**
- Texte libre (max 300 caractères)
- Description personnelle, objectifs, centres d'intérêt

**Section Compétences :**
- Tags cliquables
- Max 10 compétences principales
- Possibilité d'ajouter/retirer des compétences

**Section Expérience :**
- Timeline chronologique
- Pour chaque expérience :
    - Icône (entreprise/université)
    - Titre du poste
    - Nom de l'organisation (lien hypertexte si applicable)
    - Dates (format : "Jan 2023 - Present")
    - Description courte

#### 2.5.2 Modification du Profil
**Fonctionnalités accessibles uniquement sur son propre profil :**
- Modifier la photo de profil (upload + crop)
- Éditer la bio
- Ajouter/modifier/supprimer des compétences
- Gérer les expériences professionnelles
- Ajouter des formations complémentaires
- Définir la disponibilité mentorat (toggle)
- Paramètres de confidentialité :
    - Visibilité du profil (tous étudiants/alumni ou connexions uniquement)
    - Autoriser les messages de non-connexions
    - Afficher/masquer l'email

**Validation :**
- Champs obligatoires : nom, filière, promotion
- Photo de profil : formats jpg/png, max 5MB
- Modération automatique du contenu (détection de langage inapproprié)

---

### 2.6 Messagerie

**Écran concerné : Private and Group Messaging**

#### 2.6.1 Liste des Conversations
**Fonctionnalités :**
- Barre de recherche des conversations
- Filtres : All, Mentors, Groups, Unread
- Tri par dernière activité
- Badge de notifications non lues

**Affichage par conversation :**
- Photo de profil (individuelle) ou icône de groupe
- Nom du contact/groupe
- Dernier message (preview 60 caractères)
- Timestamp relatif (2m ago, 1h ago, Yesterday, date)
- Badge de compteur de messages non lus
- Indicateur "Typing..." en temps réel
- Statut de lecture (vu/non vu)
- Badge de statut en ligne (point vert)

#### 2.6.2 Conversation Individuelle
**Interface de chat :**
- Header : photo + nom + statut en ligne
- Zone de messages scrollable
- Input de saisie avec :
    - Champ texte multiligne
    - Bouton emoji
    - Bouton pièce jointe (images, PDF max 10MB)
    - Bouton envoi
- Bulles de messages :
    - Messages envoyés : alignés à droite, fond bleu
    - Messages reçus : alignés à gauche, fond gris
    - Timestamp sous chaque message
    - Double check de lecture

**Fonctionnalités avancées :**
- Envoi d'images (max 5 par message)
- Envoi de fichiers (CV, documents)
- Réactions rapides aux messages (emoji)
- Suppression de messages (pour soi uniquement)
- Signalement de contenu inapproprié

#### 2.6.3 Messagerie de Groupe
**Spécificités :**
- Nom du groupe personnalisable
- Photo de groupe (mosaïque des membres ou image custom)
- Liste des membres visible
- Rôles : créateur (admin) + membres
- Admin peut :
    - Ajouter/retirer des membres
    - Modifier le nom et l'image du groupe
    - Épingler des messages importants
- Notifications configurables par utilisateur

**Limite :** Max 50 membres par groupe

---

### 2.7 Calendrier d'Événements

**Écran concerné : University Events Calendar**

#### 2.7.1 Liste des Événements
**Fonctionnalités :**
- Barre de recherche d'événements
- Filtres : All, Career, Social, Workshops, Alumni
- Tri chronologique (prochains événements en premier)

**Card d'événement :**
- Image d'illustration (aspect ratio 16:9)
- Badge de catégorie (Career Fair, Workshop, Social, etc.)
- Badge "Featured" pour événements mis en avant
- Titre de l'événement
- Date et heure (format : "Oct 12 • 2:00 PM")
- Lieu (adresse ou salle)
- Nombre de participants inscrits (avatars + compteur "+42")
- Bouton "RSVP"

#### 2.7.2 Détail d'un Événement
**Informations complètes :**
- Toutes les infos de la card
- Description détaillée de l'événement
- Organisateur (nom + contact)
- Liste des participants (avec photos)
- Bouton "Ajouter au calendrier" (Google Calendar, Apple Calendar)
- Section commentaires/questions
- Bouton de partage

#### 2.7.3 Gestion des RSVP
**Fonctionnalités :**
- Confirmer présence : bouton "RSVP"
- Annuler participation
- Recevoir des rappels (notification 1 jour avant, 1h avant)
- Voir les événements auxquels on participe (onglet "My Events")

---

### 2.8 Offres d'Emploi et Petits Jobs

#### 2.8.1 Publication d'Offres
**Qui peut publier :**
- Alumni vérifiés
- Services carrière de l'université
- Partenaires entreprises approuvés
- Étudiants (pour petits jobs entre étudiants)

**Formulaire de publication :**
- Type d'offre : Stage, CDI, CDD, Freelance, Petit job
- Titre du poste
- Description (max 1000 caractères)
- Compétences requises (tags)
- Localisation
- Rémunération (optionnel)
- Date limite de candidature
- Lien externe ou candidature via message interne
- Image/logo (optionnel)

#### 2.8.2 Consultation des Offres
**Affichage dans le feed :**
- Badge visuel distinctif
- Informations clés visibles
- Bouton CTA clair

**Page dédiée "Jobs" :**
- Filtres : type de contrat, domaine, localisation
- Recherche par mots-clés
- Sauvegarde d'offres favorites
- Historique des candidatures

#### 2.8.3 Candidature
**Processus :**
- Via messagerie interne OU
- Redirection vers plateforme externe
- Possibilité de joindre CV (stocké dans le profil)
- Lettre de motivation optionnelle
- Notification à l'employeur

---

### 2.9 Navigation et Architecture

#### 2.9.1 Barre de Navigation Inférieure (iOS Style)
**Icônes et Sections :**
1. **Home** (Feed) - Icône maison
2. **Discover** (Réseau) - Icône exploration
3. **Events** (Calendrier) - Icône calendrier
4. **Messages** - Icône chat avec badge notifications
5. **Profile** - Avatar utilisateur

**État actif :** Icône remplie + texte en couleur primaire

#### 2.9.2 Top App Bar
**Éléments communs :**
- Logo/icône de l'application (gauche)
- Titre de la section (centre)
- Icônes d'action (droite) :
    - Notifications (avec badge compteur)
    - Recherche globale
    - Menu options (trois points)

---

## 3. Spécifications Techniques

### 3.1 Architecture Générale

**Stack technologique recommandée :**

**Frontend :**
- Framework : React Native (iOS + Android) ou Flutter
- State Management : Redux / MobX / Context API
- UI Library : TailwindCSS (web) / Native Base / React Native Paper
- Navigation : React Navigation

**Backend :**
- API REST ou GraphQL
- Framework : Node.js (Express/NestJS) ou Django/FastAPI
- Base de données : PostgreSQL (données relationnelles) + Redis (cache)
- Stockage fichiers : AWS S3 / Google Cloud Storage
- Serveur temps réel : Socket.io / Firebase Realtime Database (pour chat)

**Authentification :**
- JWT (JSON Web Tokens)
- OAuth 2.0 pour SSO (Google, Apple)
- Intégration LDAP/Active Directory universitaire

### 3.2 Base de Données

**Schéma relationnel (simplifié) :**

**Tables principales :**
- `users` : id, email, password_hash, first_name, last_name, status (student/alumni), promotion_year, department, bio, profile_picture, is_mentor, created_at, last_login
- `connections` : id, user_id, connected_user_id, status (pending/accepted), created_at
- `posts` : id, author_id, content, media_urls (JSON), type (text/job_offer/event), created_at, updated_at
- `post_interactions` : id, post_id, user_id, type (like/comment/share), comment_text, created_at
- `messages` : id, conversation_id, sender_id, content, media_urls, is_read, created_at
- `conversations` : id, type (individual/group), name, participants (JSON or join table), created_at
- `skills` : id, user_id, skill_name
- `experiences` : id, user_id, title, company, start_date, end_date, description
- `events` : id, title, description, category, date, location, organizer_id, max_participants, image_url
- `event_rsvps` : id, event_id, user_id, status (going/interested/not_going)
- `job_offers` : id, publisher_id, title, description, type, location, salary, deadline, application_url

### 3.3 APIs et Intégrations

**Endpoints principaux :**

**Authentification :**
- `POST /auth/login` - Connexion
- `POST /auth/logout` - Déconnexion
- `POST /auth/forgot-password` - Demande réinitialisation
- `POST /auth/reset-password` - Nouveau mot de passe
- `GET /auth/verify-token` - Validation token

**Utilisateurs :**
- `GET /users/:id` - Profil utilisateur
- `PUT /users/:id` - Modification profil
- `GET /users/search` - Recherche utilisateurs
- `GET /users/suggestions` - Suggestions de connexions

**Connexions :**
- `POST /connections/request` - Demande de connexion
- `PUT /connections/:id/accept` - Accepter connexion
- `DELETE /connections/:id` - Supprimer connexion
- `GET /connections` - Liste des connexions

**Feed :**
- `GET /feed` - Fil d'actualité paginé
- `POST /posts` - Créer publication
- `PUT /posts/:id` - Modifier publication
- `DELETE /posts/:id` - Supprimer publication
- `POST /posts/:id/like` - Liker
- `POST /posts/:id/comment` - Commenter

**Messagerie :**
- `GET /conversations` - Liste conversations
- `GET /conversations/:id/messages` - Messages d'une conversation
- `POST /conversations` - Créer conversation
- `POST /messages` - Envoyer message
- `PUT /messages/:id/read` - Marquer comme lu

**Événements :**
- `GET /events` - Liste événements
- `GET /events/:id` - Détail événement
- `POST /events/:id/rsvp` - S'inscrire
- `DELETE /events/:id/rsvp` - Annuler inscription

**Emplois :**
- `GET /jobs` - Liste offres
- `POST /jobs` - Publier offre
- `POST /jobs/:id/apply` - Candidater

### 3.4 Sécurité et Confidentialité

**Mesures de sécurité :**
- Chiffrement HTTPS (TLS 1.3)
- Validation et sanitization de toutes les entrées utilisateur
- Protection contre les injections SQL (ORM/requêtes paramétrées)
- Rate limiting sur les endpoints sensibles
- Validation du domaine email universitaire
- Modération de contenu (automatique + manuelle)
- Système de signalement et blocage d'utilisateurs
- Conformité RGPD :
    - Consentement cookies
    - Droit à l'oubli (suppression compte)
    - Export des données personnelles
    - Politique de confidentialité claire

**Gestion des données sensibles :**
- Pas de stockage de données bancaires
- Hashage des mots de passe (bcrypt, Argon2)
- Chiffrement des messages privés (optionnel, end-to-end)
- Logs d'audit des accès aux données

### 3.5 Performance et Scalabilité

**Optimisations :**
- Pagination des feeds et listes (20-50 items par page)
- Lazy loading des images
- Cache Redis pour données fréquemment accédées
- CDN pour médias statiques
- Compression des images (WebP, formats adaptatifs)
- Indexation base de données (index sur foreign keys, champs de recherche)
- WebSockets pour chat temps réel (réduire polling)

**Capacité cible :**
- Supporter 10 000+ utilisateurs actifs simultanés
- Temps de réponse API < 200ms (95e percentile)
- Disponibilité 99.9%

---

## 4. Spécifications UX/UI

### 4.1 Charte Graphique

**Palette de couleurs :**
- Primaire : `#1313ec` (bleu électrique)
- Arrière-plan clair : `#f6f6f8`
- Arrière-plan sombre : `#101022`
- Texte principal : `#0d0d1b` (clair) / `#f8f8fc` (sombre)
- Texte secondaire : `#4c4c9a` / `#a0a0cc`

**Typographie :**
- Police principale : Lexend (sans-serif moderne)
- Tailles :
    - Titres : 24-32px, bold
    - Corps : 14-16px, regular
    - Labels : 12-13px, medium

**Iconographie :**
- Material Symbols Outlined
- Style épuré et moderne

### 4.2 Principes de Design

**Mobile-first :**
- Interface optimisée pour écrans 375-430px de large
- Gestes tactiles naturels (swipe, pull-to-refresh)
- Zone de pouce accessible pour actions principales

**Accessibilité :**
- Contraste minimum WCAG AA (4.5:1 pour texte)
- Tailles de cibles tactiles ≥ 44x44px
- Support du mode sombre
- Labels accessibles pour lecteurs d'écran
- Navigation au clavier (web)

**Cohérence :**
- Design system unifié (composants réutilisables)
- Animations et transitions fluides (300ms standard)
- Feedback visuel immédiat pour toutes les actions

### 4.3 Responsive Design

**Breakpoints :**
- Mobile : 320-767px
- Tablette : 768-1023px
- Desktop : 1024px+

**Adaptations tablette/desktop :**
- Sidebar navigation (remplace la barre inférieure)
- Layout multi-colonnes pour le feed
- Profils affichés en modal/overlay
- Chat en fenêtre latérale (desktop)

---

## 5. Gestion de Projet

### 5.1 Livrables

**Phase 1 - MVP (3-4 mois) :**
- Authentification (login, forgot password)
- Profils utilisateurs (affichage + édition)
- Feed basique (posts texte + images)
- Connexions entre utilisateurs
- Messagerie 1-to-1
- Recherche d'utilisateurs

**Phase 2 - Fonctionnalités Avancées (2-3 mois) :**
- Offres d'emploi/petits jobs
- Calendrier d'événements + RSVP
- Messagerie de groupe
- Notifications push
- Système de recommandations

**Phase 3 - Optimisations (1-2 mois) :**
- Amélioration algorithme de feed
- Analytics et modération
- Onboarding amélioré
- Tests utilisateurs et ajustements

### 5.2 Équipe Recommandée

**Rôles clés :**
- 1 Chef de projet / Product Owner
- 1 UX/UI Designer
- 2-3 Développeurs Frontend (React Native/Flutter)
- 2 Développeurs Backend (API)
- 1 DevOps / Ingénieur Sécurité
- 1 QA Tester
- 1 Community Manager / Modérateur

### 5.3 Méthodologie

**Approche Agile :**
- Sprints de 2 semaines
- Daily standups (15min)
- Revues de sprint et rétrospectives
- Déploiement continu (CI/CD)

**Outils :**
- Gestion projet : Jira, Trello, Linear
- Design : Figma, Sketch
- Version control : Git (GitHub/GitLab)
- Communication : Slack, Microsoft Teams

---

## 6. Critères de Succès et KPIs

### 6.1 Métriques de Performance

**Engagement :**
- Taux d'activation (complétion profil après 1ère connexion) > 70%
- DAU/MAU (Daily/Monthly Active Users) > 25%
- Temps moyen par session > 8 minutes
- Nombre moyen de connexions par utilisateur > 15

**Adoption :**
- Taux d'inscription (% des étudiants/alumni éligibles) > 60% à 6 mois
- Taux de rétention J7 > 40%, J30 > 25%
- Net Promoter Score (NPS) > 50

**Fonctionnalités :**
- Posts publiés par mois : moyenne > 2 par utilisateur actif
- Messages envoyés par jour : > 500 (pour 1000 utilisateurs)
- Taux de réponse aux demandes de connexion > 60%
- Participation aux événements (RSVP) > 30% des utilisateurs/mois

### 6.2 Critères de Qualité

**Technique :**
- Uptime > 99.5%
- Temps de chargement pages < 2 secondes
- Taux d'erreur API < 1%
- Couverture de tests > 80%

**Utilisateur :**
- Satisfaction globale > 4/5
- Taux d'abandon du parcours d'onboarding < 20%
- Nombre de signalements de contenu < 2% des posts

---

## 7. Contraintes et Risques

### 7.1 Contraintes

**Légales :**
- Conformité RGPD (données personnelles)
- Respect du droit à l'image
- Modération de contenus (loi Avia en France)
- Conditions générales d'utilisation (CGU)
- Politique de confidentialité

**Budgétaires :**
- Hébergement cloud évolutif
- Coûts de stockage médias
- Licences logicielles
- Maintenance et support

**Techniques :**
- Compatibilité iOS 14+ / Android 10+
- Support des anciens devices (3 ans max)
- Bande passante réseau variable

### 7.2 Risques Identifiés

**Adoption :**
- Faible adhésion initiale → Campagne de communication intensive, partenariats avec BDE
- Concurrence d'autres plateformes → Différenciation par le côté exclusif et universitaire

**Technique :**
- Surcharge serveurs lors du lancement → Load testing, infrastructure scalable
- Bugs critiques → Tests rigoureux, rollback rapide, monitoring 24/7

**Contenu :**
- Contenus inappropriés → Modération proactive, IA de détection + équipe humaine
- Harcèlement entre utilisateurs → Système de signalement, blocage, charte d'utilisation stricte

**Sécurité :**
- Fuite de données → Audits de sécurité réguliers, bug bounty program
- Usurpation d'identité → Validation email universitaire obligatoire

---

## 8. Évolutions Futures (Post-MVP)

### 8.1 Fonctionnalités Avancées

- **Mode vidéo :** Visioconférence intégrée pour mentorat
- **Stories éphémères :** Partage de moments quotidiens (24h)
- **Groupes thématiques :** Clubs, projets, cohortes de promotion
- **Système de badges :** Gamification (profil complet, 10 connexions, etc.)
- **Recommandations IA :** Matching mentor/mentoré basé sur ML
- **Intégration calendrier :** Sync avec Google Calendar, Outlook
- **Marketplace :** Vente/achat entre étudiants (livres, matériel)
- **Fundraising :** Cagnotte pour projets étudiants
- **Annuaire des alumni :** Cartographie interactive par secteur/entreprise

### 8.2 Extensions

- **API publique :** Permettre intégrations tierces (partenaires entreprises)
- **Application desktop :** Version Windows/macOS
- **Widget mobile :** Accès rapide aux messages
- **Version navigateur offline :** Progressive Web App (PWA)

---

## 9. Annexes

### 9.1 Glossaire

- **Alumni :** Ancien étudiant diplômé de l'université
- **RSVP :** Répondez S'il Vous Plaît (confirmation de présence)
- **Feed :** Fil d'actualité personnalisé
- **CTA :** Call-to-Action (bouton d'action)
- **SSO :** Single Sign-On (authentification unique)
- **DAU/MAU :** Daily/Monthly Active Users

### 9.2 Références

- Documentation Material Design (Google)
- Human Interface Guidelines (Apple)
- WCAG 2.1 (Accessibilité web)
- RGPD (Règlement Général sur la Protection des Données)

---

## 10. Validation et Approbation

**Ce cahier des charges doit être validé par :**
- Direction du projet
- Équipe technique (faisabilité)
- Représentants des utilisateurs (étudiants, alumni, administration)
- Service juridique
- RSSI (Responsable Sécurité des Systèmes d'Information)

**Date de validation prévue :** [À compléter]

**Signatures :**
- Chef de projet : ________________
- Sponsor/Client : ________________
- Lead Développeur : ________________

---

**Version du document :** 1.0  
**Date de dernière mise à jour :** [Date actuelle]  
**Auteur :** [Votre nom/équipe]

---

*Ce cahier des charges est un document évolutif qui sera mis à jour en fonction des retours utilisateurs et des contraintes techniques rencontrées durant le développement.*