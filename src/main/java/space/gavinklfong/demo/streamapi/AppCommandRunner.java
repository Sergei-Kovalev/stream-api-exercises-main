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
import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
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
	public void run(String... args) throws Exception {
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

		// 7.
		System.out.println(DIVIDING_LINE);
		System.out.println("7. Получите 3 самых дешевых товара категории «Books»");

		// 8.
		System.out.println(DIVIDING_LINE);
		System.out.println("8. Получите 3 последних размещенных заказа");

		// 9.
		System.out.println(DIVIDING_LINE);
		System.out.println("9. Получите список товаров, которые были заказаны 15 марта 2021 г.");

		// 10.
		System.out.println(DIVIDING_LINE);
		System.out.println("10. Подсчитайте общую сумму всех заказов, размещенных в феврале 2021 г.");

		// 11.
		System.out.println(DIVIDING_LINE);
		System.out.println("11. Подсчитайте общую сумму всех заказов, размещенных в феврале 2021 г. (используя сокращение с помощью BiFunction).");

		// 12.
		System.out.println(DIVIDING_LINE);
		System.out.println("12. Рассчитайте среднюю цену всех заказов, размещенных 15 марта 2021 г.");

		// 13.
		System.out.println(DIVIDING_LINE);
		System.out.println("13. Получите сводную статистику по всем продуктам категории «Книги».");

		// 14.
		System.out.println(DIVIDING_LINE);
		System.out.println("14. Получите Map -> id заказа : количество продуктов в заказе.");

		// 15.
		System.out.println(DIVIDING_LINE);
		System.out.println("15. Получить Map -> клиент : список заказов");

		// 16.
		System.out.println(DIVIDING_LINE);
		System.out.println("16. Получите Map -> customer_id : список order_id(ов)");

		// 17.
		System.out.println(DIVIDING_LINE);
		System.out.println("17. Получите Map -> заказ : его общая стоимость.");

		// 18.
		System.out.println(DIVIDING_LINE);
		System.out.println("18. Получите Map -> заказ : его общая стоимость (с помощью reduce())");

		// 19.
		System.out.println(DIVIDING_LINE);
		System.out.println("19. Получите Map -> с названиями продуктов по категориям");

		// 20.
		System.out.println(DIVIDING_LINE);
		System.out.println("20. Получите самый дорогой товар в каждой категории");

		// 21.
		System.out.println(DIVIDING_LINE);
		System.out.println("21. Получите самый дорогой товар (выбрать только названия) в каждой категории.");
	}

}
