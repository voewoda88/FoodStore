# FoodStore

### Описание проекта

Food Store предоставляет пользователю ассортимент продуктов, которые он может заказать на дом. API предоставляет следующие возможности:

* Аутентификация, регистрация и авторизация пользователя
* Просмотр продуктов
* Оформление заказа
* Панель админа
* Добавление админом категорий и продуктов
* Просмотр пользователей

### Графический материал

* [Диаграммы активностей](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/activity/activityDiagrams.md)
* [Диаграмма развертывания](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/deployment/deployment.md)
* [Диаграммы последовательности](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/sequence/sequence.md)
* [Диаграммы состояний](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/state/state.md)
* [Диаграмма компонентов](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/component/component.md)
* [Диаграмма вариантов использования](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/documentation/diagrams/usecases/usecases.md)

### Исходный код

Исходный код проекта можно посмотреть [здесь](https://github.com/voewoda88/FoodStore/tree/master/spring-reg-auth-master).

### Аутентификая

Для доступа к многим страницам сайта понадобится атентификация. Запустив проект, вы можете зарегистировать нового пользователя, после чего будет выполнен вход. Регистрации админинстратора нет, он добавляется посредством изменения кода, либо через запрос SQL.

Для регистрации админинстратора нужно изменить следующий код в [UserService](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/java/com/boots/service/UserService.java):

```java
user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
```

на

```java
user.setRoles(Collections.singleton(new Role(2L, "ROLE_ADMIN")));
```

После регистрации админинстратора, не забудьте изменить код на начальный.

### Установка

Чтобы развернуть API локально нужно выполнить следующие шаги:

1. Убедитесь, что у вас установлен IntelliJ IDEA. Если нет, то скачайте его с [официального сайта](https://www.jetbrains.com/idea/download/?section=windows).
2. Клонируйте репозиторий:
```
git clone https://github.com/voewoda88/FoodStore.git
```
3. Запустив проект, перейдите в файл [apllication.properties](https://github.com/voewoda88/FoodStore/blob/master/spring-reg-auth-master/src/main/resources/application.properties) и настройте соединение с БД изменив следующие поля:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres 
spring.datasource.password=root
```
4. Выполните скрипт создания БД.
5. Запустите API через IDE или консоль maven:
```
mvn run
```

API будет доступен по адресу http://localhost:8080.

### Взиамодействие

Приветствуется вклад в развитие данного проекта. Если у вас есть какие-либо предложения, идеи, или же вы нашли ошибку, пожалуйста, создайте Pull Request и опишите там свои мысли.
