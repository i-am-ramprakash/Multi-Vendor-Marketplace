package com.marketplace.admin.infrastructure.persistence.repository;

import com.marketplace.admin.domain.entity.*;
import com.marketplace.admin.domain.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DashboardRepositoryImpl implements DashboardRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserJpaRepository userJpaRepository;
    private final VendorJpaRepository vendorJpaRepository;
    private final ProductJpaRepository productJpaRepository;
    private final OrderJpaRepository orderJpaRepository;
    private final CommissionRecordJpaRepository commissionRecordJpaRepository;

    @Override
    public Long countUsers() {
        return userJpaRepository.countAllUsers();
    }

    @Override
    public Long countVendors() {
        return vendorJpaRepository.countAllVendors();
    }

    @Override
    public Long countVendorsByStatus(String status) {
        return vendorJpaRepository.countByStatus(status);
    }

    @Override
    public Long countProducts() {
        return productJpaRepository.countAllProducts();
    }

    @Override
    public Long countProductsByStatus(String status) {
        return productJpaRepository.countByStatus(status);
    }

    @Override
    public Long countOrders() {
        return orderJpaRepository.countAllOrders();
    }

    @Override
    public Long countOrdersByStatus(String status) {
        return orderJpaRepository.countByStatus(status);
    }

    @Override
    public BigDecimal sumRevenue() {
        return orderJpaRepository.sumTotalRevenue();
    }

    @Override
    public BigDecimal sumCommissionRevenue() {
        return commissionRecordJpaRepository.sumTotalCommission();
    }

    @Override
    public BigDecimal averageOrderValue() {
        return orderJpaRepository.getAverageOrderValue();
    }

    @Override
    public BigDecimal sumRevenueByDateRange(LocalDate from, LocalDate to) {
        return orderJpaRepository.sumRevenueByDateRange(
                from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public BigDecimal sumCommissionByDateRange(LocalDate from, LocalDate to) {
        return commissionRecordJpaRepository.sumCommissionByDateRange(
                from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public Long countOrdersByDateRange(LocalDate from, LocalDate to) {
        return orderJpaRepository.countByCreatedAtBetween(
                from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public Long countUsersByDateRange(LocalDate from, LocalDate to) {
        return userJpaRepository.countByCreatedAtBetween(
                from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public List<TopVendor> findTopVendorsByRevenue(int limit, int offset) {
        String sql = """
                SELECT v.id as vendor_id, v.store_name, u.first_name, u.last_name,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission_paid,
                       COALESCE(AVG(v.commission_rate), 0) as average_rating,
                       v.created_at as joined_at
                FROM vendors v
                JOIN users u ON v.user_id = u.id
                LEFT JOIN products p ON v.id = p.vendor_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON v.id = oi.vendor_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE v.status = 'APPROVED'
                GROUP BY v.id, v.store_name, u.first_name, u.last_name, v.created_at
                ORDER BY total_revenue DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopVendor.builder()
                .vendorId(rs.getLong("vendor_id"))
                .storeName(rs.getString("store_name"))
                .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .commissionPaid(rs.getBigDecimal("commission_paid"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .joinedAt(rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null)
                .build(), limit, offset);
    }

    @Override
    public List<TopVendor> findTopVendorsByOrders(int limit, int offset) {
        String sql = """
                SELECT v.id as vendor_id, v.store_name, u.first_name, u.last_name,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission_paid,
                       COALESCE(AVG(v.commission_rate), 0) as average_rating,
                       v.created_at as joined_at
                FROM vendors v
                JOIN users u ON v.user_id = u.id
                LEFT JOIN products p ON v.id = p.vendor_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON v.id = oi.vendor_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE v.status = 'APPROVED'
                GROUP BY v.id, v.store_name, u.first_name, u.last_name, v.created_at
                ORDER BY total_orders DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopVendor.builder()
                .vendorId(rs.getLong("vendor_id"))
                .storeName(rs.getString("store_name"))
                .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .commissionPaid(rs.getBigDecimal("commission_paid"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .joinedAt(rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null)
                .build(), limit, offset);
    }

    @Override
    public List<TopVendor> findTopVendorsByRating(int limit, int offset) {
        String sql = """
                SELECT v.id as vendor_id, v.store_name, u.first_name, u.last_name,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission_paid,
                       COALESCE(AVG(v.average_rating), 0) as average_rating,
                       v.created_at as joined_at
                FROM vendors v
                JOIN users u ON v.user_id = u.id
                LEFT JOIN products p ON v.id = p.vendor_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON v.id = oi.vendor_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE v.status = 'APPROVED'
                GROUP BY v.id, v.store_name, u.first_name, u.last_name, v.created_at
                ORDER BY average_rating DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopVendor.builder()
                .vendorId(rs.getLong("vendor_id"))
                .storeName(rs.getString("store_name"))
                .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .commissionPaid(rs.getBigDecimal("commission_paid"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .joinedAt(rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null)
                .build(), limit, offset);
    }

    @Override
    public List<TopVendor> findTopVendors(LocalDate from, LocalDate to, String sortBy, int limit, int offset) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "orders" -> "total_orders DESC";
            case "rating" -> "average_rating DESC";
            default -> "total_revenue DESC";
        };

        String sql = """
                SELECT v.id as vendor_id, v.store_name, u.first_name, u.last_name,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission_paid,
                       COALESCE(AVG(v.average_rating), 0) as average_rating,
                       v.created_at as joined_at
                FROM vendors v
                JOIN users u ON v.user_id = u.id
                LEFT JOIN products p ON v.id = p.vendor_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON v.id = oi.vendor_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                    AND o.created_at BETWEEN ? AND ?
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE v.status = 'APPROVED'
                GROUP BY v.id, v.store_name, u.first_name, u.last_name, v.created_at
                ORDER BY """ + orderBy + """
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopVendor.builder()
                .vendorId(rs.getLong("vendor_id"))
                .storeName(rs.getString("store_name"))
                .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .commissionPaid(rs.getBigDecimal("commission_paid"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .joinedAt(rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null)
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX), limit, offset);
    }

    @Override
    public List<TopProduct> findTopProductsBySales(int limit, int offset) {
        String sql = """
                SELECT p.id as product_id, p.name as product_name, c.name as category_name,
                       v.store_name as vendor_name, COALESCE(SUM(oi.quantity), 0) as total_sold,
                       COALESCE(SUM(oi.quantity * oi.unit_price), 0) as total_revenue,
                       COALESCE(AVG(p.base_price), 0) as average_price,
                       p.review_count as total_reviews,
                       COALESCE(AVG(p.average_rating), 0) as average_rating
                FROM products p
                JOIN categories c ON p.category_id = c.id
                JOIN vendors v ON p.vendor_id = v.id
                LEFT JOIN order_items oi ON p.id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                WHERE p.status = 'APPROVED'
                GROUP BY p.id, p.name, c.name, v.store_name
                ORDER BY total_sold DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopProduct.builder()
                .productId(rs.getLong("product_id"))
                .productName(rs.getString("product_name"))
                .categoryName(rs.getString("category_name"))
                .vendorName(rs.getString("vendor_name"))
                .totalSold(rs.getLong("total_sold"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .averagePrice(rs.getBigDecimal("average_price"))
                .totalReviews(rs.getInt("total_reviews"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .build(), limit, offset);
    }

    @Override
    public List<TopProduct> findTopProductsByRevenue(int limit, int offset) {
        String sql = """
                SELECT p.id as product_id, p.name as product_name, c.name as category_name,
                       v.store_name as vendor_name, COALESCE(SUM(oi.quantity), 0) as total_sold,
                       COALESCE(SUM(oi.quantity * oi.unit_price), 0) as total_revenue,
                       COALESCE(AVG(p.base_price), 0) as average_price,
                       p.review_count as total_reviews,
                       COALESCE(AVG(p.average_rating), 0) as average_rating
                FROM products p
                JOIN categories c ON p.category_id = c.id
                JOIN vendors v ON p.vendor_id = v.id
                LEFT JOIN order_items oi ON p.id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                WHERE p.status = 'APPROVED'
                GROUP BY p.id, p.name, c.name, v.store_name
                ORDER BY total_revenue DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopProduct.builder()
                .productId(rs.getLong("product_id"))
                .productName(rs.getString("product_name"))
                .categoryName(rs.getString("category_name"))
                .vendorName(rs.getString("vendor_name"))
                .totalSold(rs.getLong("total_sold"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .averagePrice(rs.getBigDecimal("average_price"))
                .totalReviews(rs.getInt("total_reviews"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .build(), limit, offset);
    }

    @Override
    public List<TopProduct> findTopProductsByRating(int limit, int offset) {
        String sql = """
                SELECT p.id as product_id, p.name as product_name, c.name as category_name,
                       v.store_name as vendor_name, COALESCE(SUM(oi.quantity), 0) as total_sold,
                       COALESCE(SUM(oi.quantity * oi.unit_price), 0) as total_revenue,
                       COALESCE(AVG(p.base_price), 0) as average_price,
                       p.review_count as total_reviews,
                       COALESCE(AVG(p.average_rating), 0) as average_rating
                FROM products p
                JOIN categories c ON p.category_id = c.id
                JOIN vendors v ON p.vendor_id = v.id
                LEFT JOIN order_items oi ON p.id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                WHERE p.status = 'APPROVED'
                GROUP BY p.id, p.name, c.name, v.store_name
                ORDER BY average_rating DESC
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopProduct.builder()
                .productId(rs.getLong("product_id"))
                .productName(rs.getString("product_name"))
                .categoryName(rs.getString("category_name"))
                .vendorName(rs.getString("vendor_name"))
                .totalSold(rs.getLong("total_sold"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .averagePrice(rs.getBigDecimal("average_price"))
                .totalReviews(rs.getInt("total_reviews"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .build(), limit, offset);
    }

    @Override
    public List<TopProduct> findTopProducts(LocalDate from, LocalDate to, Long categoryId, String sortBy, int limit, int offset) {
        String orderBy = switch (sortBy.toLowerCase()) {
            case "revenue" -> "total_revenue DESC";
            case "rating" -> "average_rating DESC";
            default -> "total_sold DESC";
        };

        String sql = """
                SELECT p.id as product_id, p.name as product_name, c.name as category_name,
                       v.store_name as vendor_name, COALESCE(SUM(oi.quantity), 0) as total_sold,
                       COALESCE(SUM(oi.quantity * oi.unit_price), 0) as total_revenue,
                       COALESCE(AVG(p.base_price), 0) as average_price,
                       COUNT(DISTINCT oi.id) as total_reviews,
                       COALESCE(AVG(p.average_rating), 0) as average_rating
                FROM products p
                JOIN categories c ON p.category_id = c.id
                JOIN vendors v ON p.vendor_id = v.id
                LEFT JOIN order_items oi ON p.id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                    AND o.created_at BETWEEN ? AND ?
                WHERE p.status = 'APPROVED' AND (? IS NULL OR p.category_id = ?)
                GROUP BY p.id, p.name, c.name, v.store_name
                ORDER BY """ + orderBy + """
                LIMIT ? OFFSET ?
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> TopProduct.builder()
                .productId(rs.getLong("product_id"))
                .productName(rs.getString("product_name"))
                .categoryName(rs.getString("category_name"))
                .vendorName(rs.getString("vendor_name"))
                .totalSold(rs.getLong("total_sold"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .averagePrice(rs.getBigDecimal("average_price"))
                .totalReviews(rs.getInt("total_reviews"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(offset + rowNum + 1)
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX), categoryId, categoryId, limit, offset);
    }

    @Override
    public List<DailyRevenue> findDailyRevenue(LocalDate from, LocalDate to) {
        String sql = """
                SELECT DATE(o.created_at) as date,
                       COALESCE(SUM(o.total), 0) as revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission,
                       COUNT(o.id) as order_count,
                       CASE WHEN COUNT(o.id) > 0
                            THEN SUM(o.total) / COUNT(o.id)
                            ELSE 0 END as average_order_value
                FROM orders o
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE o.status = 'DELIVERED' AND o.created_at BETWEEN ? AND ?
                GROUP BY DATE(o.created_at)
                ORDER BY date ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> DailyRevenue.builder()
                .date(rs.getDate("date").toLocalDate())
                .revenue(rs.getBigDecimal("revenue"))
                .commission(rs.getBigDecimal("commission"))
                .orderCount(rs.getLong("order_count"))
                .averageOrderValue(rs.getBigDecimal("average_order_value"))
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public List<DailyRevenue> findWeeklyRevenue(LocalDate from, LocalDate to) {
        String sql = """
                SELECT DATE(DATE_ADD(o.created_at, INTERVAL(-WEEKDAY(o.created_at)) DAY)) as date,
                       COALESCE(SUM(o.total), 0) as revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission,
                       COUNT(o.id) as order_count,
                       CASE WHEN COUNT(o.id) > 0
                            THEN SUM(o.total) / COUNT(o.id)
                            ELSE 0 END as average_order_value
                FROM orders o
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE o.status = 'DELIVERED' AND o.created_at BETWEEN ? AND ?
                GROUP BY DATE(DATE_ADD(o.created_at, INTERVAL(-WEEKDAY(o.created_at)) DAY))
                ORDER BY date ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> DailyRevenue.builder()
                .date(rs.getDate("date").toLocalDate())
                .revenue(rs.getBigDecimal("revenue"))
                .commission(rs.getBigDecimal("commission"))
                .orderCount(rs.getLong("order_count"))
                .averageOrderValue(rs.getBigDecimal("average_order_value"))
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public List<DailyRevenue> findMonthlyRevenue(int months) {
        LocalDate from = LocalDate.now().minusMonths(months);
        LocalDate to = LocalDate.now();

        String sql = """
                SELECT DATE_FORMAT(o.created_at, '%Y-%m-01') as date,
                       COALESCE(SUM(o.total), 0) as revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission,
                       COUNT(o.id) as order_count,
                       CASE WHEN COUNT(o.id) > 0
                            THEN SUM(o.total) / COUNT(o.id)
                            ELSE 0 END as average_order_value
                FROM orders o
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE o.status = 'DELIVERED' AND o.created_at BETWEEN ? AND ?
                GROUP BY DATE_FORMAT(o.created_at, '%Y-%m-01')
                ORDER BY date ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> DailyRevenue.builder()
                .date(rs.getDate("date").toLocalDate())
                .revenue(rs.getBigDecimal("revenue"))
                .commission(rs.getBigDecimal("commission"))
                .orderCount(rs.getLong("order_count"))
                .averageOrderValue(rs.getBigDecimal("average_order_value"))
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public List<CategoryRevenue> findCategoryRevenue(LocalDate from, LocalDate to) {
        String sql = """
                SELECT c.id as category_id, c.name as category_name,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       CASE WHEN COUNT(o.id) > 0
                            THEN SUM(o.total) / COUNT(o.id)
                            ELSE 0 END as average_order_value
                FROM categories c
                LEFT JOIN products p ON c.id = p.category_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON p.id = oi.product_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                    AND o.created_at BETWEEN ? AND ?
                GROUP BY c.id, c.name
                ORDER BY total_revenue DESC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> CategoryRevenue.builder()
                .categoryId(rs.getLong("category_id"))
                .categoryName(rs.getString("category_name"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .averageOrderValue(rs.getBigDecimal("average_order_value"))
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX));
    }

    @Override
    public List<OrderStatusBreakdown> findOrderStatusBreakdown(LocalDate from, LocalDate to) {
        String sql = """
                SELECT status, COUNT(*) as count
                FROM orders
                WHERE created_at BETWEEN ? AND ?
                GROUP BY status
                ORDER BY count DESC
                """;
        List<OrderStatusBreakdown> results = jdbcTemplate.query(sql, (rs, rowNum) -> OrderStatusBreakdown.builder()
                .status(rs.getString("status"))
                .count(rs.getLong("count"))
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX));

        long totalCount = results.stream().mapToLong(OrderStatusBreakdown::getCount).sum();
        results.forEach(r -> r.setPercentage(totalCount > 0 ? (double) r.getCount() / totalCount * 100 : 0.0));

        return results;
    }

    @Override
    public List<UserRegistrationTrend> findUserRegistrationTrend(int months) {
        String sql = """
                SELECT DATE_FORMAT(created_at, '%Y-%m') as period, COUNT(*) as count
                FROM users
                WHERE created_at >= DATE_SUB(NOW(), INTERVAL ? MONTH)
                GROUP BY DATE_FORMAT(created_at, '%Y-%m')
                ORDER BY period ASC
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> UserRegistrationTrend.builder()
                .period(rs.getString("period"))
                .count(rs.getLong("count"))
                .growthRate(0.0)
                .build(), months);
    }

    @Override
    public java.util.Optional<TopVendor> findVendorPerformance(Long vendorId, LocalDate from, LocalDate to) {
        String sql = """
                SELECT v.id as vendor_id, v.store_name, u.first_name, u.last_name,
                       COUNT(DISTINCT p.id) as total_products,
                       COUNT(DISTINCT o.id) as total_orders,
                       COALESCE(SUM(o.total), 0) as total_revenue,
                       COALESCE(SUM(cr.commission_amount), 0) as commission_paid,
                       COALESCE(AVG(v.average_rating), 0) as average_rating,
                       v.created_at as joined_at
                FROM vendors v
                JOIN users u ON v.user_id = u.id
                LEFT JOIN products p ON v.id = p.vendor_id AND p.status = 'APPROVED'
                LEFT JOIN order_items oi ON v.id = oi.vendor_id
                LEFT JOIN orders o ON oi.order_id = o.id AND o.status = 'DELIVERED'
                    AND o.created_at BETWEEN ? AND ?
                LEFT JOIN commission_records cr ON o.id = cr.order_id AND cr.is_settled = TRUE
                WHERE v.id = ?
                GROUP BY v.id, v.store_name, u.first_name, u.last_name, v.created_at
                """;
        List<TopVendor> results = jdbcTemplate.query(sql, (rs, rowNum) -> TopVendor.builder()
                .vendorId(rs.getLong("vendor_id"))
                .storeName(rs.getString("store_name"))
                .ownerName(rs.getString("first_name") + " " + rs.getString("last_name"))
                .totalProducts(rs.getLong("total_products"))
                .totalOrders(rs.getLong("total_orders"))
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .commissionPaid(rs.getBigDecimal("commission_paid"))
                .averageRating(rs.getDouble("average_rating"))
                .rank(1)
                .joinedAt(rs.getTimestamp("joined_at") != null ? rs.getTimestamp("joined_at").toLocalDateTime() : null)
                .build(), from.atStartOfDay(), to.atTime(LocalTime.MAX), vendorId);
        return results.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(results.get(0));
    }
}