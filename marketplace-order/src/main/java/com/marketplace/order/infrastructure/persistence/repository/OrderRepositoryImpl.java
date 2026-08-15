package com.marketplace.order.infrastructure.persistence.repository;

import com.marketplace.order.domain.entity.Order;
import com.marketplace.order.domain.repository.OrderRepository;
import com.marketplace.order.domain.valueobject.OrderStatus;
import com.marketplace.order.infrastructure.persistence.entity.OrderJpaEntity;
import com.marketplace.order.infrastructure.persistence.mapper.OrderPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    @Override
    public Order save(Order order) {
        OrderJpaEntity jpa = OrderPersistenceMapper.toJpaEntity(order);
        OrderJpaEntity saved = jpaRepository.save(jpa);
        return OrderPersistenceMapper.toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream()
            .map(OrderPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        return jpaRepository.findByUserId(userId, pageable)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByUserIdAndStatus(Long userId, OrderStatus status) {
        return jpaRepository.findByUserIdAndStatus(userId, status).stream()
            .map(OrderPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Order> findByUserIdAndStatus(Long userId, OrderStatus status, Pageable pageable) {
        return jpaRepository.findByUserIdAndStatus(userId, status, pageable)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return jpaRepository.findByStatus(status).stream()
            .map(OrderPersistenceMapper::toDomain)
            .toList();
    }

    @Override
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return jpaRepository.findByStatus(status, pageable)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public List<Order> findByUserIdAndCreatedAtBetween(Long userId, Instant start, Instant end) {
        return jpaRepository.findByUserId(userId).stream()
            .map(OrderPersistenceMapper::toDomain)
            .filter(o -> o.getCreatedAt().isAfter(start) && o.getCreatedAt().isBefore(end))
            .toList();
    }

    @Override
    public Page<Order> search(String keyword, Long userId, Long vendorId, OrderStatus status, Pageable pageable) {
        return jpaRepository.search(keyword, userId, vendorId, status, pageable)
            .map(OrderPersistenceMapper::toDomain);
    }

    @Override
    public long countByUserId(Long userId) {
        return jpaRepository.countByUserId(userId);
    }

    @Override
    public long countByUserIdAndStatus(Long userId, OrderStatus status) {
        return jpaRepository.countByUserIdAndStatus(userId, status);
    }

    @Override
    public long countByStatus(OrderStatus status) {
        return jpaRepository.countByStatus(status);
    }
}