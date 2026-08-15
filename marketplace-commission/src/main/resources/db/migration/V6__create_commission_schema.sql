-- Commission rules table
CREATE TABLE commission_rules (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    type VARCHAR(20) NOT NULL,
    rate DECIMAL(10, 4) NOT NULL,
    fixed_amount DECIMAL(12, 2),
    min_order_amount DECIMAL(12, 2),
    max_commission_amount DECIMAL(12, 2),
    category_id BIGINT,
    vendor_id BIGINT,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    priority INT NOT NULL DEFAULT 0,
    effective_from TIMESTAMP NULL,
    effective_to TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Commission records table
CREATE TABLE commission_records (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    order_item_id BIGINT NOT NULL,
    vendor_id BIGINT NOT NULL,
    commission_rule_id BIGINT NOT NULL,
    order_amount DECIMAL(12, 2) NOT NULL,
    commission_amount DECIMAL(12, 2) NOT NULL,
    vendor_payout DECIMAL(12, 2) NOT NULL,
    commission_rate DECIMAL(10, 4) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    is_settled BOOLEAN NOT NULL DEFAULT FALSE,
    settled_at TIMESTAMP NULL,
    settlement_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Settlements table
CREATE TABLE settlements (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    settlement_number VARCHAR(50) NOT NULL UNIQUE,
    vendor_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    total_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    commission_amount DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    net_payout DECIMAL(12, 2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    record_count INT NOT NULL DEFAULT 0,
    period_start TIMESTAMP NOT NULL,
    period_end TIMESTAMP NOT NULL,
    processed_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    payment_method VARCHAR(50),
    payment_reference VARCHAR(100),
    notes TEXT,
    failure_reason TEXT,
    commission_record_ids TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0
);

-- Commission audit logs table
CREATE TABLE commission_audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vendor_id BIGINT,
    order_id BIGINT,
    commission_record_id BIGINT,
    settlement_id BIGINT,
    action VARCHAR(50) NOT NULL,
    details TEXT,
    performed_by BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX idx_commission_rules_vendor_id ON commission_rules(vendor_id);
CREATE INDEX idx_commission_rules_category_id ON commission_rules(category_id);
CREATE INDEX idx_commission_rules_type ON commission_rules(type);
CREATE INDEX idx_commission_rules_is_active ON commission_rules(is_active);
CREATE INDEX idx_commission_rules_is_default ON commission_rules(is_default);

CREATE INDEX idx_commission_records_order_id ON commission_records(order_id);
CREATE INDEX idx_commission_records_vendor_id ON commission_records(vendor_id);
CREATE INDEX idx_commission_records_settlement_id ON commission_records(settlement_id);
CREATE INDEX idx_commission_records_is_settled ON commission_records(is_settled);
CREATE INDEX idx_commission_records_created_at ON commission_records(created_at);

CREATE INDEX idx_settlements_vendor_id ON settlements(vendor_id);
CREATE INDEX idx_settlements_status ON settlements(status);
CREATE INDEX idx_settlements_settlement_number ON settlements(settlement_number);
CREATE INDEX idx_settlements_vendor_status ON settlements(vendor_id, status);
CREATE INDEX idx_settlements_created_at ON settlements(created_at);

CREATE INDEX idx_commission_audit_logs_vendor_id ON commission_audit_logs(vendor_id);
CREATE INDEX idx_commission_audit_logs_order_id ON commission_audit_logs(order_id);
CREATE INDEX idx_commission_audit_logs_settlement_id ON commission_audit_logs(settlement_id);
CREATE INDEX idx_commission_audit_logs_action ON commission_audit_logs(action);