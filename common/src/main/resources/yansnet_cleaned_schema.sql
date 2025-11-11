-- Création de domaines pour des contraintes communes
CREATE DOMAIN email_type AS VARCHAR(255) CHECK (VALUE ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$');
CREATE DOMAIN phone_type AS VARCHAR(50) CHECK (VALUE ~* '^\+?[0-9\s.-]+$');
CREATE DOMAIN positive_int AS INT CHECK (VALUE >= 0);

-- Création des types ENUM
CREATE TYPE media_type AS ENUM ('image', 'video');
CREATE TYPE message_type AS ENUM ('image', 'video', 'voice');
CREATE TYPE conversation_type AS ENUM ('private', 'public');
CREATE TYPE conversation_role AS ENUM ('admin', 'user');

-- Table batches
CREATE TABLE batches
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table departments
CREATE TABLE departments
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- Table user_categories
CREATE TABLE user_categories
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(30) NOT NULL,
    description VARCHAR(255)
);

-- Table channels
CREATE TABLE channels
(
    id              SERIAL PRIMARY KEY,
    title           VARCHAR(150) NOT NULL UNIQUE,
    description     TEXT,
    total_followers positive_int DEFAULT 0
);

-- Table users
CREATE TABLE users
(
    id              SERIAL PRIMARY KEY,
    email           email_type   NOT NULL UNIQUE,
    phone_number    phone_type,
    password        VARCHAR(255) NOT NULL,
    is_active       BOOLEAN      DEFAULT TRUE,
    is_blocked      BOOLEAN      DEFAULT FALSE,
    total_followers positive_int DEFAULT 0,
    total_following positive_int DEFAULT 0,
    total_posts     positive_int DEFAULT 0,
    category_id     INT REFERENCES user_categories (id),
    department_id   INT REFERENCES departments (id),
    batch_id        INT REFERENCES batches (id)
);

-- Table posts
CREATE TABLE posts
(
    id             SERIAL PRIMARY KEY,
    content        TEXT      NOT NULL,
    deleted_at     TIMESTAMP,
    created_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    total_likes    positive_int       DEFAULT 0,
    total_comments positive_int       DEFAULT 0,
    user_id        INT REFERENCES users (id),
    channel_id     INT REFERENCES channels (id)
);

-- Table media
CREATE TABLE media
(
    id          SERIAL PRIMARY KEY,
    type        media_type   NOT NULL,
    url         VARCHAR(255) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT NOW(),
    post_id     INT REFERENCES posts (id),
    UNIQUE (post_id, url)
);

-- Table comments
CREATE TABLE comments
(
    id                  SERIAL PRIMARY KEY,
    content             TEXT,
    created_at          TIMESTAMP    DEFAULT NOW(),
    deleted_at          TIMESTAMP,
    is_deleted          BOOLEAN      DEFAULT FALSE,
    total_likes         positive_int DEFAULT 0,
    is_edited           BOOLEAN      DEFAULT FALSE,
    updated_at          TIMESTAMP,
    post_id             INT REFERENCES posts (id),
    user_id             INT REFERENCES users (id),
    reply_to_comment_id INT REFERENCES comments (id),
    CHECK ((is_deleted AND deleted_at IS NOT NULL) OR (NOT is_deleted AND deleted_at IS NULL))
);

-- Table post_likes
CREATE TABLE post_likes
(
    user_id INT REFERENCES users (id),
    post_id INT REFERENCES posts (id),
    PRIMARY KEY (user_id, post_id)
);

-- Table comment_likes
CREATE TABLE comment_likes
(
    user_id    INT REFERENCES users (id),
    comment_id INT REFERENCES comments (id),
    PRIMARY KEY (user_id, comment_id)
);

-- Table channel_followers
CREATE TABLE channel_followers
(
    user_id    INT REFERENCES users (id),
    channel_id INT REFERENCES channels (id),
    PRIMARY KEY (user_id, channel_id)
);

-- Table user_follows
CREATE TABLE user_follows
(
    follower_id INT REFERENCES users (id),
    followed_id INT REFERENCES users (id),
    PRIMARY KEY (follower_id, followed_id),
    CHECK (follower_id != followed_id)
    );

-- Table channel_users
CREATE TABLE channel_users
(
    user_id    INT REFERENCES users (id),
    channel_id INT REFERENCES channels (id),
    role       conversation_role NOT NULL,
    PRIMARY KEY (user_id, channel_id)
);

-- Table conversations
CREATE TABLE conversations
(
    id          SERIAL PRIMARY KEY,
    title       TEXT,
    description TEXT,
    type        conversation_type NOT NULL,
    role        conversation_role NOT NULL,
    user_one_id INT REFERENCES users (id),
    user_two_id INT REFERENCES users (id),
    UNIQUE (user_one_id, user_two_id),
    CHECK (user_one_id < user_two_id),
    CHECK (
        (type = 'public' AND title IS NOT NULL AND description IS NOT NULL) OR
        (type = 'private' AND title IS NULL AND description IS NULL)
        ),
    CHECK (type = 'private' AND role = 'user' OR type = 'public')
);

-- Table messages
CREATE TABLE messages
(
    id              SERIAL PRIMARY KEY,
    content         TEXT,
    type            message_type NOT NULL,
    url             VARCHAR(255),
    conversation_id INT REFERENCES conversations (id),
    user_id         INT REFERENCES users (id)
);

-- Indexes
CREATE UNIQUE INDEX idx_users_email ON users (email);
CREATE INDEX idx_messages_conversation_id ON messages (conversation_id);
CREATE INDEX idx_messages_user_id ON messages (user_id);
CREATE INDEX idx_posts_channel_id ON posts (channel_id);
CREATE INDEX idx_posts_user_id ON posts (user_id);
CREATE INDEX idx_comments_post_id ON comments (post_id);
CREATE INDEX idx_comments_user_id ON comments (user_id);
