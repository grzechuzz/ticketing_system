CREATE TABLE addresses (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    country_code CHAR(2) NOT NULL,
    city VARCHAR(100) NOT NULL,
    postal_code VARCHAR(12) NOT NULL,
    street VARCHAR(255) NOT NULL,
    building_number VARCHAR(10) NOT NULL,
    apartment_number VARCHAR(10),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE venues (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address_id BIGINT NOT NULL UNIQUE REFERENCES addresses(id),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE sectors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    venue_id BIGINT NOT NULL REFERENCES venues(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    is_standing BOOLEAN NOT NULL DEFAULT FALSE,
    standing_capacity INTEGER,
    rows_count INTEGER,
    seats_per_row INTEGER,
    position_x INTEGER NOT NULL DEFAULT 0,
    position_y INTEGER NOT NULL DEFAULT 0,
    width INTEGER NOT NULL DEFAULT 100,
    height INTEGER NOT NULL DEFAULT 100,
    color VARCHAR(7) DEFAULT '#3B82F6',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (venue_id, name),
    CONSTRAINT chk_sector_type CHECK (
     (is_standing = TRUE AND standing_capacity > 0 AND rows_count IS NULL) OR
     (is_standing = FALSE AND standing_capacity IS NULL AND rows_count > 0 AND seats_per_row > 0)
     )
);

CREATE INDEX idx_sectors_venue ON sectors(venue_id);

CREATE TABLE seats (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    sector_id BIGINT NOT NULL REFERENCES sectors(id) ON DELETE CASCADE,
    row_number INTEGER NOT NULL CHECK (row_number > 0),
    seat_number INTEGER NOT NULL CHECK (seat_number > 0),
    UNIQUE (sector_id, row_number, seat_number)
);

CREATE INDEX idx_seats_sector ON seats(sector_id);
CREATE INDEX idx_seats_sector_row ON seats(sector_id, row_number);