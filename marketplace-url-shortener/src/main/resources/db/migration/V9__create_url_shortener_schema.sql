-- Short URLs table
CREATE TABLE short_urls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_code VARCHAR(20) NOT NULL UNIQUE,
    original_url TEXT NOT NULL,
    title VARCHAR(500),
    description TEXT,
    type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    expiration_type VARCHAR(20) NOT NULL DEFAULT 'NONE',
    expires_at TIMESTAMP NULL,
    reference_id BIGINT,
    reference_type VARCHAR(50),
    created_by BIGINT,
    is_custom_alias BOOLEAN NOT NULL DEFAULT FALSE,
    click_count BIGINT NOT NULL DEFAULT 0,
    unique_click_count BIGINT NOT NULL DEFAULT 0,
    last_clicked_at TIMESTAMP NULL,
    password VARCHAR(255),
    requires_password BOOLEAN NOT NULL DEFAULT FALSE,
    tags TEXT,
    metadata TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- URL clicks table
CREATE TABLE url_clicks (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url_id BIGINT NOT NULL,
    short_code VARCHAR(20) NOT NULL,
    ip_address VARCHAR(45),
    user_agent TEXT,
    referer TEXT,
    country VARCHAR(100),
    city VARCHAR(100),
    device VARCHAR(50),
    browser VARCHAR(50),
    os VARCHAR(50),
    is_unique BOOLEAN NOT NULL DEFAULT TRUE,
    user_id BIGINT,
    clicked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (short_url_id) REFERENCES short_urls(id) ON DELETE CASCADE
);

-- URL analytics table
CREATE TABLE url_analytics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    short_url_id BIGINT NOT NULL UNIQUE,
    short_code VARCHAR(20) NOT NULL UNIQUE,
    total_clicks BIGINT NOT NULL DEFAULT 0,
    unique_visitors BIGINT NOT NULL DEFAULT 0,
    clicks_today BIGINT NOT NULL DEFAULT 0,
    clicks_this_week BIGINT NOT NULL DEFAULT 0,
    clicks_this_month BIGINT NOT NULL DEFAULT 0,
    top_country VARCHAR(100),
    top_city VARCHAR(100),
    top_device VARCHAR(50),
    top_browser VARCHAR(50),
    top_referer TEXT,
    last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (short_url_id) REFERENCES short_urls(id) ON DELETE CASCADE
);

-- URL rate limits table
CREATE TABLE url_rate_limits (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    identifier VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    request_count INT NOT NULL DEFAULT 0,
    max_requests INT NOT NULL DEFAULT 100,
    window_start TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    window_end TIMESTAMP NOT NULL,
    is_blocked BOOLEAN NOT NULL DEFAULT FALSE,
    blocked_until TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_identifier_type (identifier, type)
);

-- Indexes
CREATE INDEX idx_short_urls_short_code ON short_urls(short_code);
CREATE INDEX idx_short_urls_created_by ON short_urls(created_by);
CREATE INDEX idx_short_urls_type ON short_urls(type);
CREATE INDEX idx_short_urls_status ON short_urls(status);
CREATE INDEX idx_short_urls_reference ON short_urls(reference_id, reference_type);
CREATE INDEX idx_short_urls_created_at ON short_urls(created_at);

CREATE INDEX idx_url_clicks_short_url_id ON url_clicks(short_url_id);
CREATE INDEX idx_url_clicks_short_code ON url_clicks(short_code);
CREATE INDEX idx_url_clicks_clicked_at ON url_clicks(clicked_at);
CREATE INDEX idx_url_clicks_ip_address ON url_clicks(ip_address);

CREATE INDEX idx_url_analytics_short_url_id ON url_analytics(short_url_id);
CREATE INDEX idx_url_analytics_short_code ON url_analytics(short_code);

CREATE INDEX idx_url_rate_limits_identifier ON url_rate_limits(identifier, type);