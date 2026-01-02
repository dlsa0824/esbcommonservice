CREATE PROCEDURE company.sp_get_employee_by_employee_number(
    i_employee_number IN VARCHAR,
    o_refcursor OUT REFCURSOR)
LANGUAGE plpgsql
AS
$$
BEGIN
    OPEN o_refcursor FOR
    SELECT
        employee_number,
        first_name,
        last_name,
        email,
        phone,
        hire_date,
        salary,
        department,
        position_title,
        is_active,
        created_at
    FROM
        company.employees
    WHERE
        employee_number = i_employee_number;
END;
$$;