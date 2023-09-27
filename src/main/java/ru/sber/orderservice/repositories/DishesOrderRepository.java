package ru.sber.orderservice.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.orderservice.entities.DishesOrder;

/**
 * Репозиторий с {@link DishesOrder блюдами}
 */
@Repository
public interface DishesOrderRepository extends JpaRepository<DishesOrder, Long> {
}
