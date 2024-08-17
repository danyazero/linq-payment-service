package org.zero.paymentservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zero.paymentservice.entity.History;
import org.zero.paymentservice.model.OrderStatus;

import java.util.Optional;

public interface HistoryRepository extends JpaRepository<History, Integer> {

    @Query(value = """
select H.status
from "history" H where H.transaction = 1
order by H.id DESC limit 1
""", nativeQuery = true)
    Optional<OrderStatus> getPaymentStatus(Integer paymentId);
}