DROP FUNCTION IF EXISTS public.generateWindowsDates;

CREATE FUNCTION public.generateWindowsDates("_dateFrom" DATE, "_dateTo" DATE)
RETURNS TABLE
  (
    "day"      		DATE,
    dateFrom		DATE,
    dateTo		DATE
  )
AS $$
  BEGIN
    RETURN QUERY
		SELECT t.day::date, t.day::date  - 1,  t.day::date + 1
		FROM generate_series("_dateFrom",  "_dateTo", interval  '1 day') AS t(day);
  END; $$

LANGUAGE 'plpgsql';