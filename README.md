## Cтек
- Java
- Maven
- Spring (MVC, Data Jpa, Security)
- Minio
- База данных
	- PostgreSQL
- Redis
- Frontend
  - Thymeleaf
  - Bootstrap
  - JavaScript
- Docker
  - docker-compose для инфраструктуры
- Testing
	- JUnit
  	- Testcontainers
	- Mockito 
	- AssertJ
- Прочие библиотеки
	- [Spring-dotenv](https://github.com/paulschwarz/spring-dotenv)

<br><br>

## Краткое описание
Приложение представляет собой инструмент для организации и запоминания информации с помощью карточек (Flashcards). 
Пользователи могут создавать свои карточки и организовывать опросы на основе созданных карточек, что помогает проверить свои знания и усвоение информации.

<br>

## Покрытие тестами
![image](https://github.com/ryzendee/flashcards_mvc/assets/108254290/8396d890-e6cf-42ca-b21c-8fa1232098f2)
<br>

## Структура
**Security**
<br><br>
Приложение использует механизм Spring-security для аутентификации пользователей по их учетным данным (username, password).
Используются сессии, которые хранятся в Redis.
Есть возможность сделать logout.
<br><br>

**Управление папками (CardFolders)**
<br><br>
Все карточки пользователей хранятся в CardFolder (name, description, imageUrl).
Для удобства на странице используется пагинация. 
<br>
Пользователь может:
- создавать, изменять и удалять папки
- просматривать карточки (будет соверешен переход на страницу с Flashcards)
- запускать опрос по карточкам из данной папки
<br><br>

**Управление карточками (Flashcards)**
<br><br>
Карточки используются в опросах. Их основа состоит из двух полей: имя и определение.
<br>
Пользователь может:
- создавать, изменять, удалять карточки

Опрос по карточкам реализован с помощью JS-скрипта. Во время опроса пользователю показывается имя карточки без определения. 
Далее при нажатии кнопки пользователь может посмотреть определение и проверить себя, после чего карточка помечается либо как изученная (больше не появляется в опросе), либо не изученная (появится в опросе еще раз)
<br><br>

**Загрузка фотографий с использованием Minio**
<br><br>
При добавлении или изменении карточки (Flashcard) или папки (CardFolder), пользователь может загрузить фотографию, которая затем будет хранится в Minio. При отсутствии будет использовано фото по-умолчанию, которое хранится локально (в статических ресурсах)
<br><br>





