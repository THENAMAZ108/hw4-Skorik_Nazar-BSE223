# ИДЗ-4 «Система заказов по покупку билетов»
Исполнитель: студент группы БПИ223 Скорик Назар
## Команда для сборки:
![Alt text](images/photo_2024-06-10_00-11-37.jpg)
## Запуск проекта осуществляется через Docker Desktop:
![Alt text](images/photo_2024-06-10_00-12-12.jpg)
## Структура проекта:
![Alt text](images/photo_2024-06-10_01-37-00.jpg)
## Описание работы программы:
Для регистрации пользователя нужно перейти по адресу <http://localhost:8080/api/auth/register>:

В тело запроса нужно передать nickname, email и password:

**{"nickname": "nazar",**

**"email": "nazar@mail.hse.com",**

**"password": "Sssssssssss@111"}**

При успешной регистрации выведется сообщение:

**{"response": "200 OK: Регистрация успешно завершена."}**

При неправильном формате email или password будут выведены соответствующие сообщения и http статусы:

**{"response": "400 BAD\_REQUEST: Неверный формат email."}**

**{"response": "400 BAD\_REQUEST: Пароль должен состоять из не менее восьми символов, включая буквы обоих регистров, цифры и специальные символы."}**

При попытке зарегистрироваться с того же email будут выведет соответствующее сообщение и http статус:

**{"response": "400 BAD\_REQUEST: Пользователь с таким email уже зарегистрирован."}**

Для входа нужно перейти по адресу <http://localhost:8080/api/auth/authenticate>:

При успешном входе выведется сообщение и токен для доступа к защищенной конечной точке для просмотра данных профиля:

**{"token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuYXphckBtYWlsLmhzZS5jb20iLCJpYXQiOjE3MTc5Njk5MTAsImV4cCI6MTcxNzk3MDAzMH0.0BgbDJaPbPGWVLqnhbIPOEGKlkhHTA2OAxotKGwKRO4",**

**"response": "200 OK: Успешная аутентификация. Время истечения срока действия сеанса: 2 минуты."}**

При неудачной попытке:

**{"token": null,**

**"response": "401 UNAUTHORIZED: Неправильный email или пароль."}**

Для получения данных профиля нужно перейти по <http://localhost:8080/api/userinfo>:

При предоставлении валидного jwt токена в хедере Authorization появится сообщение:

**{"nickname": "nazar",**

**"email": "nazar@mail.hse.com",**

**"created": "2024-06-09T21:43:23.777+00:00",**

**"response": "Информация о пользователе успешно получена."}**

Если токен invalid или expired:

**“Token invalid or expired.”**

## Технологии:
- Spring Boot;
- Java Persistence API (JPA) для работы с базами данных;
- Spring Security для аутентификации и авторизации;
- JDBC драйвер для PostgreSQL;
- jjwt-api, jjwt-impl, jjwt-jackson: Библиотеки для работы с JSON Web Tokens (JWT);
- Lombok для минимизации шаблонного кода.
change for pull request
