create table if not exists users(
  id BIGINT primary key,
  name VARCHAR(20) unique not null,
  password VARCHAR(255) not null,
  version INT not null,
  created_at TIMESTAMP(6) not null,
  modified_at TIMESTAMP(6) not null
);
