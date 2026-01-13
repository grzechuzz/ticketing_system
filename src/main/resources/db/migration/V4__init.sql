CREATE TYPE event_status AS ENUM ('DRAFT', 'PENDING', 'APPROVED', 'REJECTED', 'CANCELLED', 'COMPLETED');

CREATE TABLE events (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    organizer_id BIGINT NOT NULL REFERENCES organizers(id),
    venue_id BIGINT NOT NULL REFERENCES venues(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    event_start TIMESTAMPTZ NOT NULL,
    event_end TIMESTAMPTZ NOT NULL,
    sales_start TIMESTAMPTZ NOT NULL,
    sales_end TIMESTAMPTZ NOT NULL,
    max_tickets_per_customer INTEGER,
    min_age INTEGER,
    holder_data_required BOOLEAN NOT NULL DEFAULT FALSE,
    status event_status NOT NULL DEFAULT 'DRAFT',
    rejection_reason TEXT,
    cover_image_url VARCHAR(500),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_event_dates CHECK (event_end >= event_start AND sales_end >= sales_start AND sales_end <= event_start)
);

CREATE INDEX idx_events_organizer ON events(organizer_id);
CREATE INDEX idx_events_status ON events(status);
CREATE INDEX idx_events_sales ON events(sales_start, sales_end);

CREATE TABLE event_sectors (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    sector_id BIGINT NOT NULL REFERENCES sectors(id),
    capacity_snapshot INTEGER NOT NULL CHECK (capacity_snapshot > 0),
    available_capacity INTEGER NOT NULL CHECK (available_capacity >= 0),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (event_id, sector_id),
    CONSTRAINT chk_capacity CHECK (available_capacity <= capacity_snapshot)
);

CREATE INDEX idx_event_sectors_event ON event_sectors(event_id);

CREATE TABLE ticket_types (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

INSERT INTO ticket_types (name, description) VALUES
    ('NORMAL', 'Standard ticket'),
    ('REDUCED', 'Reduced price (students, seniors)'),
    ('VIP', 'VIP access'),
    ('CHILD', 'Child ticket');

CREATE TABLE event_sector_ticket_types (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_sector_id BIGINT NOT NULL REFERENCES event_sectors(id) ON DELETE CASCADE,
    ticket_type_id BIGINT NOT NULL REFERENCES ticket_types(id),
    price_net NUMERIC(10,2) NOT NULL CHECK (price_net >= 0),
    vat_rate NUMERIC(5,2) NOT NULL DEFAULT 1.23 CHECK (vat_rate >= 1),
    price_gross NUMERIC(10,2) GENERATED ALWAYS AS (price_net * vat_rate) STORED,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    UNIQUE (event_sector_id, ticket_type_id)
);

CREATE INDEX idx_estt_event_sector ON event_sector_ticket_types(event_sector_id);