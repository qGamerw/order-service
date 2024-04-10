package ru.sber.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sber.entities.Order;
import ru.sber.entities.enums.EStatusOrders;
import ru.sber.models.Coordinates;
import ru.sber.models.LimitDishesOrder;
import ru.sber.models.LimitOrder;
import ru.sber.models.LimitOrderRestaurant;
import ru.sber.repositories.DishesOrderRepository;
import ru.sber.repositories.OrderRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Контроллер для взаимодействия {@link Order заказами}
 */
@Service
@Slf4j
public class OrderServiceImp implements OrderService {
    private final OrderRepository orderRepository;
    private final DishesOrderRepository dishesOrderRepository;
    private final KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate;

    @Autowired
    public OrderServiceImp(
            OrderRepository orderRepository,
            DishesOrderRepository dishesOrderRepository,
            KafkaTemplate<String, LimitOrderRestaurant> kafkaLimitOrderRestaurantTemplate) {
        this.orderRepository = orderRepository;
        this.dishesOrderRepository = dishesOrderRepository;
        this.kafkaLimitOrderRestaurantTemplate = kafkaLimitOrderRestaurantTemplate;
    }

    @Override
    public ResponseEntity<String> updateOrderStatus(long id, LimitOrderRestaurant order) {
        Optional<LimitOrder> limitOrder = getOrderById(id);

        if (limitOrder.isPresent()) {
            sendMessageBroker(order, limitOrder);
        } else {
            return new ResponseEntity<>("Заказ не найден.", HttpStatus.NOT_FOUND);
        }

        Order LimitOrder = orderRepository.findById(id).get();
        LimitOrder.setStatusOrders(EStatusOrders.valueOf(order.getStatus()));

        switch (order.getStatus()) {
            case "COOKING" -> {
                LimitOrder.setStartCookingTime(LocalDateTime.now());
                LimitOrder.setBranchOfficeId(order.getBranchId());
                LimitOrder.setBranchAddress(order.getBranchAddress());
                LimitOrder.setEmployeeRestaurantId(order.getEmployeeRestaurantId());
            }
            case "COOKED" -> LimitOrder.setEndCookingTime(LocalDateTime.now());
            case "DELIVERY" -> LimitOrder.setDeliveryTime(LocalDateTime.now());
        }

        orderRepository.save(LimitOrder);
        return new ResponseEntity<>("Статус заказа успешно обновлен.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateOrderCourierId(String idCourier, long idOrder) {
        Optional<Order> order = orderRepository.findById(idOrder);

        if (order.isPresent()) {
            order.get().setCourierId(idCourier);
            orderRepository.save(order.get());
            return new ResponseEntity<>("Заказ успешно присвоен курьеру.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Заказ не найден.", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<LimitOrderRestaurant> getListOrder() {
        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.REVIEW,
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersIn(eStatusOrdersList)
                .stream()
                .map(getLimitOrderRestoranFunction())
                .toList();
    }

    @Override
    public ResponseEntity<String> paymentOfOrderById(long idOrder) {
        Optional<Order> order = orderRepository.findById(idOrder);

        if (order.isPresent()) {
            order.get().setStatusOrders(EStatusOrders.REVIEW);
            orderRepository.save(order.get());

            return new ResponseEntity<>("Заказ успешно оплачен.", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Заказ не найден.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> cancellationOfOrderById(Long id, String massage) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            order.get().setStatusOrders(EStatusOrders.CANCELLED);
            order.get().setRefusalReason(massage);
            orderRepository.save(order.get());

            return new ResponseEntity<>("Заказ успешно отменен.", HttpStatus.ACCEPTED);
        }

        return new ResponseEntity<>("Заказ не найден.", HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<String> cancellationOfOrderByListId(String listId, String massage) {
        if (massage == null || massage.isEmpty()) {
            return new ResponseEntity<>("Не написана причина отказа.", HttpStatus.NOT_FOUND);
        }

        Arrays.stream(listId.split(","))
                .map(Long::parseLong)
                .forEach(item ->
                {
                    Optional<Order> order = orderRepository.findById(item);

                    if (order.isPresent()) {
                        order.get().setStatusOrders(EStatusOrders.CANCELLED);
                        order.get().setRefusalReason(massage);
                        orderRepository.save(order.get());
                    }
                });

        return new ResponseEntity<>("Заказы успешно отменен.", HttpStatus.ACCEPTED);
    }

    @Override
    public List<LimitOrder> findAllActiveOrder() {
        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersInAndCourierIdNull(eStatusOrdersList)
                .stream()
                .map(getLimitOrderFunction())
                .toList();
    }

    @Override
    public Page<LimitOrder> findAllActiveOrdersByPage(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);

        List<EStatusOrders> eStatusOrdersList = Arrays.asList(
                EStatusOrders.COOKING,
                EStatusOrders.COOKED);

        return orderRepository.findByStatusOrdersInAndCourierIdNull(eStatusOrdersList, pageable)
                .map(getLimitOrderFunction());
    }

    @Override
    public Optional<LimitOrder> getOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);

        if (order.isPresent()) {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(id)
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();

            return Optional.of(new LimitOrder(order.get(), dishesOrders));
        }

        return Optional.empty();
    }

    @Override
    public ResponseEntity<?> getOrderByIdWithCoordinates(long id) {
        Optional<LimitOrder> limitOrder = getOrderById(id);

        if (limitOrder.isPresent()) {
            LimitOrder legitLimitOrder = limitOrder.get();

            try {
                legitLimitOrder.setBranchAddressCoordinates(requestCoordinates(legitLimitOrder.getBranchAddress()));
                legitLimitOrder.setAddressCoordinates(requestCoordinates(legitLimitOrder.getAddress()));
            } catch (IOException e) {
                log.error("Ошибка получения заказа с координатами: Ошибка чтения координат.");
                return new ResponseEntity<>(
                        "Ошибка получения заказа с координатами: Ошибка чтения координат.",
                        HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(legitLimitOrder, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Не удалось найли координаты.", HttpStatus.NOT_FOUND);
    }

    public Coordinates requestCoordinates(String address) throws IOException {
        String apiKey = "8ec18778-cb70-437f-87fc-7c17e8e0bb71";

        ResponseEntity<?> response = new RestTemplate().exchange(
                "https://geocode-maps.yandex.ru/1.x/?apikey=" + apiKey + "&geocode=" + address + "&format=json",
                HttpMethod.GET,
                null,
                Object.class);
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(objectMapper.writeValueAsString(response.getBody()));
        log.info("Определение по адресу: {}", rootNode);

        JsonNode posNode = rootNode
                .path("response")
                .path("GeoObjectCollection")
                .path("featureMember")
                .get(0)
                .path("GeoObject")
                .path("Point")
                .path("pos");
        String pos = posNode.asText();
        log.info("Ответ: {}.", pos);

        String[] numbers = pos.split(" ");

        return new Coordinates(new BigDecimal(numbers[0]), new BigDecimal(numbers[1]));
    }

    @Override
    public Page<LimitOrder> findOrdersByCourierId(String id, int page, int pageSize) {
        return orderRepository
                .findOrderByCourierId(id, PageRequest.of(page, pageSize))
                .map(getLimitOrderFunction());
    }

    @Override
    public List<LimitOrder> findOrdersCourierIsDelivering(String id) {
        return orderRepository
                .findOrderByCourierIdAndStatusOrdersNot(id, EStatusOrders.COMPLETED)
                .stream()
                .map(getLimitOrderFunction())
                .toList();
    }

    @Override
    public List<LimitOrderRestaurant> getListOrderByNotify(String ordersId) {
        List<Long> dishIds = Arrays.stream(ordersId.split(","))
                .map(Long::parseLong)
                .toList();

        return orderRepository.findAllById(dishIds).stream()
                .map(getLimitOrderRestoranFunction())
                .toList();
    }

    private void sendMessageBroker(LimitOrderRestaurant order, Optional<LimitOrder> limitOrder) {
        switch (order.getStatus()) {
            case "REVIEW" -> kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
            case "COOKING", "COOKED" -> {
                limitOrder.ifPresent(value -> {
                    order.setBranchAddress(value.getBranchAddress());
                    order.setClientId(value.getClientId());
                });
                kafkaLimitOrderRestaurantTemplate.send("courier_status", order);
                kafkaLimitOrderRestaurantTemplate.send("client_status", order);
            }
            case "CANCELLED" -> {
                limitOrder.ifPresent(value -> order.setClientId(value.getClientId()));
                kafkaLimitOrderRestaurantTemplate.send("restaurant_status", order);
                kafkaLimitOrderRestaurantTemplate.send("client_status", order);
            }
            case "DELIVERY", "COMPLETED" -> {
                limitOrder.ifPresent(value -> order.setClientId(value.getClientId()));
                kafkaLimitOrderRestaurantTemplate.send("client_status", order);
            }
        }
    }

    /**
     * Преобразует класс Order {@link Order} в {@link LimitOrder}
     *
     * @return урезанный заказ
     */
    private Function<Order, LimitOrder> getLimitOrderFunction() {
        return order -> {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(order.getId())
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();
            return new LimitOrder(order, dishesOrders);
        };
    }

    /**
     * Преобразует класс Order {@link Order} в {@link LimitOrderRestaurant}
     *
     * @return LimitOrderRestoran
     */
    private Function<Order, LimitOrderRestaurant> getLimitOrderRestoranFunction() {
        return order -> {
            List<LimitDishesOrder> dishesOrders = dishesOrderRepository.findAllByOrderId(order.getId())
                    .stream()
                    .map(LimitDishesOrder::new)
                    .toList();
            return new LimitOrderRestaurant(order, dishesOrders);
        };
    }
}