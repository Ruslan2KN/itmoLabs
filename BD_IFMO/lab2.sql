
--первый запрос
SELECT grade."КОД", statement."ДАТА"
FROM "Н_ОЦЕНКИ" grade
RIGHT JOIN "Н_ВЕДОМОСТИ" statement ON grade."КОД"=statement."ОЦЕНКА"
WHERE grade."КОД"='незач' and statement."ЧЛВК_ИД" > 153_285;

--второй запрос
SELECT people."ОТЧЕСТВО", educ."ЧЛВК_ИД", stud."ГРУППА"
FROM "Н_ЛЮДИ" people
JOIN "Н_ОБУЧЕНИЯ" educ ON people."ИД"=educ."ЧЛВК_ИД"
JOIN "Н_УЧЕНИКИ" stud ON educ."ЧЛВК_ИД" = stud."ЧЛВК_ИД"
WHERE people."ФАМИЛИЯ" < 'Афанасьев' AND educ."НЗК"= '999_080'
AND CAST(stud."ГРУППА" as INT) > 4103;

--третий запрос
WITH unique_bday AS (SELECT people."ДАТА_РОЖДЕНИЯ"
FROM "Н_ЛЮДИ" people
GROUP BY people."ДАТА_РОЖДЕНИЯ")

SELECT count(*)
FROM unique_bday;


-- четвертый запрос
SELECT stud."ГРУППА", count(stud."ЧЛВК_ИД") "количество_студентов"
FROM "Н_УЧЕНИКИ" stud
/*JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД"=plans."ИД"
JOIN "Н_ОТДЕЛЫ" dep ON dep."ИД" = plans."ОТД_ИД_ЗАКРЕПЛЕН_ЗА" */
WHERE extract(YEAR FROM stud."КОНЕЦ") >=2011 AND extract(YEAR FROM stud."НАЧАЛО") <= 2011
--AND dep."КОРОТКОЕ_ИМЯ"='КТиУ'
  AND stud."ПЛАН_ИД" IN (
    SELECT plans."ИД"
    FROM "Н_ПЛАНЫ" plans
    JOIN "Н_ОТДЕЛЫ" dep ON dep."ИД" = plans."ОТД_ИД_ЗАКРЕПЛЕН_ЗА"
    WHERE dep."КОРОТКОЕ_ИМЯ" = 'КТиУ'
    )
GROUP BY stud."ГРУППА"
HAVING count(stud."ЧЛВК_ИД")>10;

-- пятый запрос
WITH group_1101 as (SELECT avg(extract(YEAR FROM AGE(CURRENT_DATE, p."ДАТА_РОЖДЕНИЯ"))) avg_age
               FROM "Н_ЛЮДИ" p
                JOIN "Н_ОБУЧЕНИЯ" e ON p."ИД"=e."ЧЛВК_ИД"
                JOIN "Н_УЧЕНИКИ" s ON e."ЧЛВК_ИД" = s."ЧЛВК_ИД"
                WHERE s."ГРУППА" = '1101'

)

SELECT stud."ГРУППА", avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ"))) "средний_возраст"
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ОБУЧЕНИЯ" educ ON stud."ЧЛВК_ИД"=educ."ЧЛВК_ИД"
JOIN "Н_ЛЮДИ" people ON educ."ЧЛВК_ИД" = people."ИД"
GROUP BY stud."ГРУППА"
HAVING avg(extract(YEAR FROM AGE(CURRENT_DATE, people."ДАТА_РОЖДЕНИЯ")))
           < (SELECT avg_age FROM group_1101);

--шестой запрос
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
WHERE plans."КУРС"=5 and form_educ."НАИМЕНОВАНИЕ" = 'Очная');

--седьмой запрос
SELECT count(DISTINCT stud."ЧЛВК_ИД")
FROM "Н_УЧЕНИКИ" stud
JOIN "Н_ОБУЧЕНИЯ" educ ON stud."ЧЛВК_ИД" = educ."ЧЛВК_ИД"
JOIN "Н_ЛЮДИ" people ON educ."ЧЛВК_ИД" = people."ИД"
JOIN "Н_ВЕДОМОСТИ" statment ON people."ИД" = statment."ЧЛВК_ИД"
--JOIN "Н_ВЕДОМОСТИ" statment ON stud."ЧЛВК_ИД"= statment."ЧЛВК_ИД"
JOIN "Н_ПЛАНЫ" plans ON stud."ПЛАН_ИД" = plans."ИД"
JOIN "Н_ОТДЕЛЫ" dep ON dep."ИД" = plans."ОТД_ИД_ЗАКРЕПЛЕН_ЗА"

WHERE dep."КОРОТКОЕ_ИМЯ"='КТиУ' AND
      statment."ОЦЕНКА"= '3';