CREATE TYPE order_status AS ENUM ('PENDING', 'AWAITING_PAYMENT', 'COMPLETED', 'CANCELLED', 'EXPIRED');
CREATE TYPE ticket_status AS ENUM ('ACTIVE', 'USED', 'CANCELLED', 'EXPIRED');

CREATE TABLE orders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    event_id BIGINT NOT NULL REFERENCES events(id),
    total_price_net NUMERIC(10,2) NOT NULL DEFAULT 0,
    total_price_gross NUMERIC(10,2) NOT NULL DEFAULT 0,
    status order_status NOT NULL DEFAULT 'PENDING',
    reserved_until TIMESTAMPTZ NOT NULL DEFAULT (now() + INTERVAL '20 minutes'),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    completed_at TIMESTAMPTZ
);

CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_event ON orders(event_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE UNIQUE INDEX uq_active_cart ON orders(user_id, event_id) WHERE status IN ('PENDING', 'AWAITING_PAYMENT');

CREATE TABLE order_items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    event_id BIGINT NOT NULL REFERENCES events(id),
    event_sector_ticket_type_id BIGINT NOT NULL REFERENCES event_sector_ticket_types(id),
    seat_id BIGINT REFERENCES seats(id),
    quantity INTEGER NOT NULL DEFAULT 1 CHECK (quantity > 0),
    unit_price_net NUMERIC(10,2) NOT NULL,
    unit_price_gross NUMERIC(10,2) NOT NULL,
    vat_rate NUMERIC(5,2) NOT NULL,
    event_name_snapshot VARCHAR(255) NOT NULL,
    event_date_snapshot TIMESTAMPTZ NOT NULL,
    venue_name_snapshot VARCHAR(255) NOT NULL,
    venue_address_snapshot VARCHAR(500) NOT NULL,
    sector_name_snapshot VARCHAR(100) NOT NULL,
    row_snapshot INTEGER,
    seat_number_snapshot INTEGER,
    ticket_type_name_snapshot VARCHAR(100) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT chk_seated_qty CHECK (seat_id IS NULL OR quantity = 1)
);

CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_event ON order_items(event_id);
CREATE UNIQUE INDEX uq_order_item_event_seat ON order_items(event_id, seat_id) WHERE seat_id IS NOT NULL;


CREATE TABLE tickets (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_item_id BIGINT NOT NULL REFERENCES order_items(id) ON DELETE CASCADE,
    code UUID NOT NULL UNIQUE DEFAULT gen_random_uuid(),
    status ticket_status NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    used_at TIMESTAMPTZ
);

CREATE INDEX idx_tickets_code ON tickets(code);
CREATE INDEX idx_tickets_order_item ON tickets(order_item_id);

CREATE TABLE ticket_holders (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ticket_id BIGINT NOT NULL UNIQUE REFERENCES tickets(id) ON DELETE CASCADE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    birth_date DATE,
    document_number VARCHAR(50),
    created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);