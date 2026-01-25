
INSERT INTO addresses (country_code, city, postal_code, street, building_number) VALUES
('PL', 'Kraków', '30-492', 'Sportowa', '1'), ('PL', 'Warszawa', '02-496', 'Kulturalna', '12'), ('PL', 'Opole', '29-222', 'Bajeczna', '102');

INSERT INTO venues (name, address_id) VALUES ('Dragon Arena', 1), ('Biblioteka Główna', 2);

INSERT INTO organizers (name, email, phone_number, address_id, vat_number, registration_number, iban, country_code) VALUES
('TestORG', 'kontakt@testorganizer.pl', '+48123456789', 3, 'PL3930123458', '0000123456', 'PL61109010140000071219812874', 'PL');

INSERT INTO sectors (venue_id, name, is_standing, standing_capacity, rows_count, seats_per_row, position_x, position_y, width, height, color) VALUES
(1, 'A', false, NULL, 10, 10, 100, 50, 200, 150, '#FF5733'),
(1, 'GA', true, 500, NULL, NULL, 100, 250, 200, 100, '#33FF57'),
(2, 'Hala Główna', true, 8000, NULL, NULL, 50, 50, 300, 200, '#3357FF');

INSERT INTO seats (sector_id, row_number, seat_number)
SELECT 1, r, s
FROM generate_series(1, 10) AS r,
     generate_series(1, 10) AS s;

INSERT INTO events (organizer_id, venue_id, name, description, event_start, event_end, sales_start, sales_end, status, cover_image_url) VALUES
(1, 1, 'Skolim | Kraków 2027', 'Wyglądasz idealnie, by wpaść na ten koncert! Skolim znów w Krakowie z potężną dawką energii, obecność obowiązkowa!',
'2027-01-09 20:00:00+01', '2027-01-09 23:30:00+01',
'2026-01-18 20:00:00+01', '2027-01-09 19:00:00+01',
'APPROVED', '/images/skolim.png'),
(1, 2, 'Targi Książki - Warszawa 2027', 'Największe święto literatury w stolicy! Setki wydawnictw, spotkania z autorami, panele dyskusyjne.',
'2027-01-10 11:00:00+01', '2027-01-10 22:00:00+01',
'2026-01-18 20:00:00+01', '2027-01-10 10:00:00+01',
'APPROVED', '/images/targi_ksiazki.png');

INSERT INTO event_sectors (event_id, sector_id, capacity_snapshot, available_capacity) VALUES
(1, 1, NULL, NULL),
(1, 2, 500, 500),
(2, 3, 8000, 8000);

INSERT INTO event_sector_ticket_types (event_sector_id, ticket_type_id, price_net, vat_rate) VALUES
(1, 1, 120.00, 1.23),
(1, 2, 80.00, 1.23),
(1, 3, 250.00, 1.23);

INSERT INTO event_sector_ticket_types (event_sector_id, ticket_type_id, price_net, vat_rate) VALUES
(2, 1, 80.00, 1.23),
(2, 2, 50.00, 1.23);

INSERT INTO event_sector_ticket_types (event_sector_id, ticket_type_id, price_net, vat_rate) VALUES
(3, 1, 25.00, 1.08),
(3, 2, 15.00, 1.08),
(3, 4, 0.00, 1.08);
