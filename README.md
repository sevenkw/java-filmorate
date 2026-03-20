# Проект Filmorate: Схема базы данных

## Визуализация схемы
<img width="780" height="671" alt="FIlmorate drawio" src="https://github.com/user-attachments/assets/784781b0-bdb9-4802-a337-21868140b524" />


## Описание таблиц и связей
- **Users** — данные пользователей.
- **Films** — информация о фильмах и их рейтингах (MPA).
- **Friends** — статусы дружбы между пользователями (суррогатный PK `id`).
- **Film_likes** — лайки пользователей к фильмам.
- **Film_genre** — связь фильмов с их жанрами.

## Примеры SQL-запросов
### Получение пользователя с id = 1
```sql
SELECT *
FROM Users
WHERE user_id = 1
```
### Вывод фильма со всеми его жанрами и рейтингом MPA
```sql
SELECT f.name,
m.name AS mpa_rating, 
g.genre_name
FROM Films AS f
JOIN MPA AS m ON f.MPA_id = m.id
LEFT JOIN Film_genere AS fg ON f.film_id = fg.film_id
LEFT JOIN Genre AS g ON fg.genre_id = g.genre_id
WHERE f.film_id = 5;
```
### Вывод фильмов и жанров, у которых жанр - Comedy
```sql
SELECT f.name,
 g.genre_name
FROM Films AS f
JOIN Film_genre AS fg ON f.film_id = fg.film_id
JOIN Genre AS g ON fg.genre_id = g.genre_id
WHERE g.genre_name = 'Comedy';

