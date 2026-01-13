CREATE TABLE organizers (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone_number VARCHAR(20) NOT NULL,
    address_id BIGINT NOT NULL REFERENCES addresses(id),
    vat_number VARCHAR(32) UNIQUE,
    registration_number VARCHAR(40),
    iban VARCHAR(34) UNIQUE,
    country_code VARCHAR(2) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE organizer_users (
    organizer_id BIGINT NOT NULL REFERENCES organizers(id) ON DELETE CASCADE,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    PRIMARY KEY (organizer_id, user_id)
);

CREATE INDEX idx_organizer_users_user ON organizer_users(user_id);
