CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS players (
  id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  ip_address VARCHAR(255) NOT NULL,
  isActive Boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS games (
  id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
  admin_id UUID NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  player_id1 UUID,
  player_id2 UUID,
  player_id_turn UUID,
  current_cell_type VARCHAR(5) DEFAULT 'NONE' CHECK (cell1 IN ('NONE', 'X', 'O')),
  cell1 VARCHAR(5) DEFAULT 'NONE' CHECK (cell1 IN ('NONE', 'X', 'O')),
  cell2 VARCHAR(5) DEFAULT 'NONE' CHECK (cell2 IN ('NONE', 'X', 'O')),
  cell3 VARCHAR(5) DEFAULT 'NONE' CHECK (cell3 IN ('NONE', 'X', 'O')),
  cell4 VARCHAR(5) DEFAULT 'NONE' CHECK (cell4 IN ('NONE', 'X', 'O')),
  cell5 VARCHAR(5) DEFAULT 'NONE' CHECK (cell5 IN ('NONE', 'X', 'O')),
  cell6 VARCHAR(5) DEFAULT 'NONE' CHECK (cell6 IN ('NONE', 'X', 'O')),
  cell7 VARCHAR(5) DEFAULT 'NONE' CHECK (cell7 IN ('NONE', 'X', 'O')),
  cell8 VARCHAR(5) DEFAULT 'NONE' CHECK (cell8 IN ('NONE', 'X', 'O')),
  cell9 VARCHAR(5) DEFAULT 'NONE' CHECK (cell9 IN ('NONE', 'X', 'O')),
  FOREIGN KEY (admin_id) REFERENCES players(id)
);