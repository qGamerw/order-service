package ru.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.entities.DishOrder;

import java.util.List;

/**
 * Репозиторий с {@link DishOrder блюдами}
 */
@Repository
public interface DishesOrderRepository extends JpaRepository<DishOrder, Long> {

    List<DishOrder> findAllByOrderId(long id);
}