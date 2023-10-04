package ru.sber.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sber.entities.DishesOrder;

import java.util.List;

/**
 * Репозиторий с {@link DishesOrder блюдами}
 */
@Repository
public interface DishesOrderRepository extends JpaRepository<DishesOrder, Long> {

    List<DishesOrder> findAllByOrderId(long id);
}