-- пятый запрос
EXPLAIN (ANALYZE , BUFFERS )
WITH group_1101 as (SELECT avg(extract(YEAR FROM AGE(CURRENT_DATE, p."ДАТА_РОЖДЕНИЯ"))) avg_age
               FROM "Н_ЛЮДИ" p
                JOIN "Н_УЧЕНИКИ" s ON p."ИД" = s."ЧЛВК_ИД"
                WHERE s."ГРУППА" = '1101' AND s."ЧЛВК_ИД" IN (SELECT "ЧЛВК_ИД" FROM "Н_ОБУЧЕНИЯ")

)

SELECT stud."ГРУППА", avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ"))) "средний_возраст"
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ЛЮДИ" people ON stud."ЧЛВК_ИД" = people."ИД"
WHERE stud."ЧЛВК_ИД" IN (SELECT "ЧЛВК_ИД" FROM "Н_ОБУЧЕНИЯ")
GROUP BY stud."ГРУППА"
HAVING avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ")))
           < (SELECT avg_age FROM group_1101);


-- 5 без проверки что ЧЛВК_ИД содержится в промежуточной табле
EXPLAIN (ANALYZE , BUFFERS )
WITH group_1101 as (SELECT avg(extract(YEAR FROM AGE(CURRENT_DATE, p."ДАТА_РОЖДЕНИЯ"))) avg_age
               FROM "Н_ЛЮДИ" p
                JOIN "Н_УЧЕНИКИ" s ON p."ИД" = s."ЧЛВК_ИД"
                WHERE s."ГРУППА" = '1101'

)

SELECT stud."ГРУППА", avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ"))) "средний_возраст"
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ЛЮДИ" people ON stud."ЧЛВК_ИД" = people."ИД"
GROUP BY stud."ГРУППА"
HAVING avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ")))
           < (SELECT avg_age FROM group_1101);

--шестой запрос
EXPLAIN (ANALYZE, BUFFERS )
SELECT stud."ГРУППА", stud."ЧЛВК_ИД", concat( people."ФАМИЛИЯ",' ', people."ИМЯ", ' ', people."ОТЧЕСТВО") "ФИО",
       CONCAT(stud."П_ПРКОК_ИД", ' ',stud."СОСТОЯНИЕ") "Номер и состояние приказа"
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ОБУЧЕНИЯ" educ ON stud."ЧЛВК_ИД" = educ."ЧЛВК_ИД"
JOIN "Н_ЛЮДИ" people ON educ."ЧЛВК_ИД" = people."ИД"
--JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД" = plans."ИД"
--JOIN "Н_ФОРМЫ_ОБУЧЕНИЯ" form_educ ON plans."ФО_ИД" = form_educ."ИД"
WHERE  stud."НАЧАЛО"::DATE = make_date(2012,9,1)
/*AND plans."КУРС" = 5 AND form_educ."НАИМЕНОВАНИЕ"='Очная';*/
 AND stud."ПЛАН_ИД" IN (
SELECT plans."ИД"
FROM "Н_ПЛАНЫ" plans
JOIN "Н_ФОРМЫ_ОБУЧЕНИЯ" form_educ ON plans."ФО_ИД"= form_educ."ИД"
WHERE plans."КУРС"=1 and form_educ."НАИМЕНОВАНИЕ" = 'Очная');

--шестой запрос без IN подзапрос и JOIN из студентов сразу на ЛЮДЕЙ, проверка даты без доп функции
EXPLAIN (ANALYZE , BUFFERS )
SELECT stud."ГРУППА", stud."ЧЛВК_ИД", concat( people."ФАМИЛИЯ",' ', people."ИМЯ", ' ', people."ОТЧЕСТВО") "ФИО",
       CONCAT(stud."П_ПРКОК_ИД", ' ',stud."СОСТОЯНИЕ") "Номер и состояние приказа"
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ЛЮДИ" people ON stud."ЧЛВК_ИД" = people."ИД"
JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД" = plans."ИД"
JOIN "Н_ФОРМЫ_ОБУЧЕНИЯ" form_educ ON plans."ФО_ИД" = form_educ."ИД"
WHERE  stud."НАЧАЛО" < '20120902' and stud."НАЧАЛО" >= '20120901'
AND plans."КУРС" = 1 AND form_educ."НАИМЕНОВАНИЕ"='Очная';



--седьмой запрос
EXPLAIN (ANALYZE, BUFFERS )
SELECT count(DISTINCT stud."ЧЛВК_ИД")
FROM "Н_УЧЕНИКИ" stud
--JOIN "Н_ОБУЧЕНИЯ" educ ON stud."ЧЛВК_ИД" = educ."ЧЛВК_ИД"
-- JOIN "Н_ЛЮДИ" people ON educ."ЧЛВК_ИД" = people."ИД"
-- JOIN "Н_ВЕДОМОСТИ" statment ON people."ИД" = statment."ЧЛВК_ИД"
JOIN "Н_ВЕДОМОСТИ" statment ON stud."ЧЛВК_ИД"= statment."ЧЛВК_ИД"
JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД" = plans."ИД"
JOIN "Н_ОТДЕЛЫ" dep ON dep."ИД" = plans."ОТД_ИД_ЗАКРЕПЛЕН_ЗА"

WHERE dep."КОРОТКОЕ_ИМЯ"='КТ' AND
      statment."ОЦЕНКА"= '3';

--седьмой запрос Сформировать запрос для получения числа на ФКТИУ троечников. Проверяем с помощью подзапроса, что есть 3 у этого члвка.
EXPLAIN (ANALYZE , BUFFERS )
SELECT count(DISTINCT stud."ЧЛВК_ИД")
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД" = plans."ИД"
JOIN "Н_ОТДЕЛЫ" dep ON dep."ИД" = plans."ОТД_ИД_ЗАКРЕПЛЕН_ЗА"

WHERE dep."КОРОТКОЕ_ИМЯ"='КТ' AND
      stud."ЧЛВК_ИД" IN (
          SELECT "ЧЛВК_ИД"
          FROM "Н_ВЕДОМОСТИ"
          WHERE "ОЦЕНКА" = '3'
          );
-- В итоге време заметно меньше, и затраченных ресов так же мнеьше