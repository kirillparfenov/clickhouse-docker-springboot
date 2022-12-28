CREATE DATABASE curdb;
USE curdb;
CREATE TABLE IF NOT EXISTS curdb.stories
(
    id              UUID,
    title           String,
    theme_id        UUID,
    case_id         Nullable(UUID),
    created_at      UInt64
    )
    ENGINE = MergeTree()
    ORDER BY created_at;