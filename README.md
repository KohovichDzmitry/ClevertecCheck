# ClevectecCheck
## Настройка
В файле src/main/resources/database.properties необходимо указать данные для подключения к БД.
### Создание БД
Для создания и заполнения таблиц необходимо, чтобы БД, указанная в database.properties, существовала.
## Запуск
Для создания необходим контейнер сервлетов (я использовал Tomcat 9).

.war файл после сборки с помощью Gradle находится в папке build/libs.

Запуск при помощи Tomcat: собранный .war файл положить в папку wedapps Tomcat.

## Использование
### Доступные адреса
* /api/products(/{id}, {name}, {count}, {sorted_by_alphabet}, {sorted_by_price}) - работа с продуктами

* /api/cards(/{id}, {number}, {count}) - работа со скидочными картами

* /api/order - работа с чеком

Поддерживаются методы GET, POST, PUT, DELETE.

Для Get метода к набору ресурсов доступны параметры:
* page - номер страницы пагинации (с нуля);
* pageSize - размер страницы (по умолчанию - 20).

POST, PUT, DELETE недоступен для чека.

## Создание чека
Чек создается GET запросом по адресу /api/order с массивом параметров products:

product={product_id = ..}&{quantity = ..}

card={cardNumber}

В ответ на запрос возвращается файл чека Check.pdf.