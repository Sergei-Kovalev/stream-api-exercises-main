package space.gavinklfong.demo.streamapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import space.gavinklfong.demo.streamapi.models.Customer;
import space.gavinklfong.demo.streamapi.models.Order;
import space.gavinklfong.demo.streamapi.models.Product;
import space.gavinklfong.demo.streamapi.repos.CustomerRepo;
import space.gavinklfong.demo.streamapi.repos.OrderRepo;
import space.gavinklfong.demo.streamapi.repos.ProductRepo;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppCommandRunner implements CommandLineRunner {
	private final static String DIVIDING_LINE = "..................................................................";

	private final CustomerRepo customerRepos;
	private final OrderRepo orderRepos;
	private final ProductRepo productRepos;

	@Transactional
	@Override
	public void run(String... args) {
		List<Customer> customers = customerRepos.findAll();

		List<Order> orders = orderRepos.findAll();

		List<Product> products = productRepos.findAll();

		// 1.
		System.out.println(DIVIDING_LINE);
		System.out.println("1. Получить список товаров с категорией = «Books» и ценой > 100.");
		products.stream()
				.filter(p -> p.getCategory().equals("Books") && p.getPrice() > 100)
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 2.
		System.out.println(DIVIDING_LINE);
		System.out.println("2. Получите список продуктов с категорией = «Books» и ценой > 100 (используя цепочку предикатов для фильтра).");
		Predicate<Product> predicate1 = product -> product.getCategory().equals("Books");
		Predicate<Product> predicate2 = product -> product.getPrice() > 100;

		products.stream()
				.filter(predicate1.and(predicate2))
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 3.
		System.out.println(DIVIDING_LINE);
		System.out.println("3. Получите список продуктов с категорией = «Книги» и ценой > 100 (используя BiPredicate для фильтра).");
		BiPredicate<Product, String> biPredicateForCategory = (product, category) -> category.equals(product.getCategory());
		BiPredicate<Product, Double> biPredicateForPrice = (product, price) -> product.getPrice() > price;

		products.stream()
				.filter(product -> biPredicateForCategory.test(product, "Books") && biPredicateForPrice.test(product, 100.0))
				.collect(Collectors.toList())
				.forEach(System.out::println);


		// 4.
		System.out.println(DIVIDING_LINE);
		System.out.println("4. Получить список заказов с категорией товара = «Baby»");

		long startTime = System.currentTimeMillis();
		products.stream()
				.filter(product -> product.getCategory().equals("Baby"))
				.flatMap(product -> product.getOrders().stream())
				.sorted(Comparator.comparing(Order::getId))
				.distinct()
				.collect(Collectors.toList())
				.forEach(System.out::println);
		long endTime = System.currentTimeMillis();

		System.out.println(DIVIDING_LINE);

		long startTime2 = System.currentTimeMillis();
		orders.stream()
				.filter(o ->
						o.getProducts()
								.stream()
								.anyMatch(p -> p.getCategory().equalsIgnoreCase("Baby"))
				)
				.collect(Collectors.toList())
				.forEach(System.out::println);
		long endTime2 = System.currentTimeMillis();

		log.info("\n1й способ занял: {}ms" +
				"\n2й способ занял: {}ms", endTime - startTime, endTime2 - startTime2);


		// 5.
		System.out.println(DIVIDING_LINE);
		System.out.println("5. Получите список товаров с категорией = «Toys», а затем примените скидку 10%.");

		products.stream()
				.filter(product -> product.getCategory().equals("Toys"))
				.peek(product -> product.setPrice(Math.round(product.getPrice() * 0.9 * 100.0) / 100.0))
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 6.
		System.out.println(DIVIDING_LINE);
		System.out.println("6. Получите список продуктов, заказанных клиентом уровня 2 в период с 1 февраля 2021 г. по 1 апреля 2021 г.");

		products.stream()
				.filter(product -> product.getOrders().stream()
						.anyMatch(order -> order.getCustomer().getTier().equals(2)
								&& order.getOrderDate().isAfter(LocalDate.of(2021, 2, 1))
								&& order.getOrderDate().isBefore(LocalDate.of(2021, 4, 1))
						))
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 7.
		System.out.println(DIVIDING_LINE);
		System.out.println("7. Получите 3 самых дешевых товара категории «Books»");

		products.stream()
				.filter(product -> product.getCategory().equals("Books"))
				.sorted(Comparator.comparingDouble(Product::getPrice))
				.limit(3)
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 8.
		System.out.println(DIVIDING_LINE);
		System.out.println("8. Получите 3 последних размещенных заказа");

		orders.stream()
				.sorted(Comparator.comparing(Order::getOrderDate).reversed())
				.limit(3)
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 9.
		System.out.println(DIVIDING_LINE);
		System.out.println("9. Получите список товаров, которые были заказаны 15 марта 2021 г.");

		orders.stream()
				.filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
				.flatMap(order -> order.getProducts().stream())
				.distinct()
				.sorted(Comparator.comparingLong(Product::getId))
				.collect(Collectors.toList())
				.forEach(System.out::println);

		// 10.
		System.out.println(DIVIDING_LINE);
		System.out.println("10. Подсчитайте общую сумму всех заказов, размещенных в феврале 2021 г.");

		double sumOfFebruaryOrders = orders.stream()
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 1, 31))
						&& order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
				.flatMap(order -> order.getProducts().stream())
				.mapToDouble(Product::getPrice)
				.sum();
		System.out.println(sumOfFebruaryOrders);

		// 11.
		System.out.println(DIVIDING_LINE);
		System.out.println("11. Подсчитайте общую сумму всех заказов, размещенных в феврале 2021 г. (используя reduce() с помощью BiFunction).");

		BiFunction<Double, Product, Double> accumulator = (acc, product) -> acc + product.getPrice();

		Double sumOfFO = orders.stream()
				.filter(order -> order.getOrderDate().isAfter(LocalDate.of(2021, 1, 31))
						&& order.getOrderDate().isBefore(LocalDate.of(2021, 3, 1)))
				.flatMap(order -> order.getProducts().stream())
				.reduce(0.0, accumulator, Double::sum);
		System.out.println(sumOfFO);

		// 12.
		System.out.println(DIVIDING_LINE);
		System.out.println("12. Рассчитайте среднюю цену всех заказов, размещенных 15 марта 2021 г.");

		double avgPrice = orders.stream()
				.filter(order -> order.getOrderDate().isEqual(LocalDate.of(2021, 3, 15)))
				.flatMapToDouble(
						order -> order.getProducts().stream()
								.mapToDouble(Product::getPrice))
				.average()
				.orElse(0.0);

		System.out.println(avgPrice);

		// 13.
		System.out.println(DIVIDING_LINE);
		System.out.println("13. Получите сводную статистику по всем продуктам категории «Книги».");

		DoubleSummaryStatistics statistics = products.stream()
				.filter(product -> product.getCategory().equals("Books"))
				.mapToDouble(Product::getPrice)
				.summaryStatistics();
		System.out.println(statistics);

		// 14.
		System.out.println(DIVIDING_LINE);
		System.out.println("14. Получите Map -> id заказа : количество продуктов в заказе.");

		orders.stream()
				.collect(Collectors.toMap(Order::getId, order -> order.getProducts().size()))
				.entrySet()
				.forEach(System.out::println);

		// 15.
		System.out.println(DIVIDING_LINE);
		System.out.println("15. Получить Map -> клиент : список заказов");

		orders.stream()
				.collect(Collectors.groupingBy(Order::getCustomer))
				.entrySet()
				.forEach(System.out::println);

		// 16.
		System.out.println(DIVIDING_LINE);
		System.out.println("16. Получите Map -> customer_id : список order_id(ов)");

		orders.stream()
				.collect(Collectors.groupingBy(
						order -> order.getCustomer().getId(),
//						HashMap::new,
						Collectors.mapping(Order::getId, Collectors.toList())
				))
				.entrySet()
				.forEach(System.out::println);

		// 17.
		System.out.println(DIVIDING_LINE);
		System.out.println("17. Получите Map -> заказ : его общая стоимость.");

		orders.stream()
				.collect(Collectors.toMap(Function.identity(), o -> o.getProducts().stream()
						.mapToDouble(Product::getPrice).sum()))
				.entrySet()
				.forEach(System.out::println);

		// 18.
		System.out.println(DIVIDING_LINE);
		System.out.println("18. Получите Map -> заказ : его общая стоимость (с помощью reduce())");

		orders.stream()
				.collect(Collectors.toMap(Function.identity(), order -> order.getProducts().stream()
						.reduce(0.0, (acc, p) -> acc + p.getPrice(), Double::sum)))
				.entrySet().forEach(System.out::println);

		// 19.
		System.out.println(DIVIDING_LINE);
		System.out.println("19. Получите Map -> с названиями продуктов по категориям");

		products.stream()
				.collect(Collectors.groupingBy(
						Product::getCategory, Collectors.mapping(
								Product::getName, Collectors.toList())))
				.entrySet().forEach(System.out::println);

		// 20.
		System.out.println(DIVIDING_LINE);
		System.out.println("20. Получите самый дорогой товар в каждой категории");

		products.stream()
                .collect(Collectors.groupingBy(
                        Product::getCategory, Collectors.maxBy(Comparator.comparingDouble(Product::getPrice))))
                .forEach((key, value) -> value.ifPresent(product -> System.out.printf("Category: %s, Price: %s\n", key, product.getPrice())));

		// 21.
		System.out.println(DIVIDING_LINE);
		System.out.println("21. Получите самый дорогой товар (выбрать только названия) в каждой категории.");

		products.stream()
				.collect(Collectors.groupingBy(Product::getCategory, Collectors.maxBy(Comparator.comparingDouble(Product::getPrice))))
				.entrySet().stream()
				.collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().orElseThrow().getName()))
				.entrySet().forEach(System.out::println);
	}
}
