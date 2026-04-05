MERGE INTO mpa (id, name) KEY(id) VALUES (1, 'G');
MERGE INTO mpa (id, name) KEY(id) VALUES (2, 'PG');
MERGE INTO mpa (id, name) KEY(id) VALUES (3, 'PG-13');
MERGE INTO mpa (id, name) KEY(id) VALUES (4, 'R');
MERGE INTO mpa (id, name) KEY(id) VALUES (5, 'NC-17');

MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (1, 'Комедия');
MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (2, 'Драма');
MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (3, 'Мультфильм');
MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (4, 'Триллер');
MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (5, 'Документальный');
MERGE INTO genre (genre_id, genre_name) KEY(genre_id) VALUES (6, 'Боевик');

MERGE INTO friendship_status (status_id, status_name) KEY(status_id) VALUES (1, 'UNCONFIRMED');
MERGE INTO friendship_status (status_id, status_name) KEY(status_id) VALUES (2, 'CONFIRMED');
