-- V1__init.sql (Corrected Version 2)

-- Table to store information about logistics partners/carriers
CREATE TABLE carriers (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    base_url VARCHAR(255),
    api_key VARCHAR(255),
    active BOOLEAN DEFAULT true
);

-- Main table for tracking individual shipments
CREATE TABLE shipments (
    id BIGSERIAL PRIMARY KEY,
    tracking_number VARCHAR(255) NOT NULL,
    carrier_id BIGINT REFERENCES carriers(id), -- Changed to BIGINT
    origin VARCHAR(255),
    destination VARCHAR(255),
    eta TIMESTAMP WITH TIME ZONE,
    priority VARCHAR(50),
    status VARCHAR(100),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Logs every status change detected for a shipment
CREATE TABLE events (
    id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT NOT NULL REFERENCES shipments(id), -- Changed to BIGINT
    external_status VARCHAR(255),
    internal_status VARCHAR(100),
    raw_payload_json TEXT,
    detected_at TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Records when an alert email is sent for a disruption event
CREATE TABLE alerts (
    id BIGSERIAL PRIMARY KEY,
    shipment_id BIGINT NOT NULL REFERENCES shipments(id), -- Changed to BIGINT
    event_id BIGINT REFERENCES events(id), -- Changed to BIGINT
    severity VARCHAR(50),
    email_sent_at TIMESTAMP WITH TIME ZONE,
    acknowledged BOOLEAN DEFAULT false
);

-- Stores alternative shipping quotes generated during a disruption
CREATE TABLE solutions (
    id BIGSERIAL PRIMARY KEY,
    event_id BIGINT NOT NULL REFERENCES events(id), -- Changed to BIGINT
    alt_carrier_id BIGINT REFERENCES carriers(id), -- Changed to BIGINT
    quoted_price NUMERIC(10, 2),
    quoted_eta TIMESTAMP WITH TIME ZONE,
    quote_payload_json TEXT,
    chosen BOOLEAN DEFAULT false
);