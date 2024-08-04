# Java Stream API Задачки

## Введение

Данные задачи нагло скопированы и криво переведены с проекта [Святого человека](https://github.com/gavinklfong/stream-api-exercises).
Немного модифицирован код.


## Модель данных

Состоит из Customer, Order и Product. Customers могут содержать множество orders и каждый Order должен содержать некоторое количество products.

![Data Model](https://github.com/gavinklfong/stream-api-exercises/blob/main/blob/Data_Model.jpg?raw=true)

## Задачи:

Примеры решений(от автора) можно найти в тестах `space.gavinklfong.demo.streamapi.StreamApiTest`
Свои решения можно размещать в классе `space.gavinklfong.demo.streamapi.AppCommandRunner`
Ниже приведены условия задач - их можно скопировать в указанный класс, чтобы заменить то, что успел "нарешать" я.

// 1.
System.out.println(DIVIDING_LINE);
System.out.println("1. Получить список товаров с категорией = «Books» и ценой > 100.");


// 2.
System.out.println(DIVIDING_LINE);
System.out.println("2. Получите список продуктов с категорией = «Books» и ценой > 100 (используя цепочку предикатов для фильтра).");


// 3.
System.out.println(DIVIDING_LINE);
System.out.println("3. Получите список продуктов с категорией = «Книги» и ценой > 100 (используя BiPredicate для фильтра).");

// 4.
System.out.println(DIVIDING_LINE);
System.out.println("4. Получить список заказов с категорией товара = «Baby»");

// 5.
System.out.println(DIVIDING_LINE);
System.out.println("5. Получите список товаров с категорией = «Toys», а затем примените скидку 10%.");

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