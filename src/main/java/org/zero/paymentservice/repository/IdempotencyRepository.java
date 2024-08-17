package org.zero.paymentservice.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.zero.paymentservice.entity.Idempotency;

@Repository
public interface IdempotencyRepository extends CrudRepository<Idempotency, String> {

}
