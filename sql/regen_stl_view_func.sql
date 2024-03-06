-- FUNCTION: public.regen_stl_view()

-- DROP FUNCTION IF EXISTS public.regen_stl_view();

CREATE OR REPLACE FUNCTION public.regen_stl_view(
	)
    RETURNS integer
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
DECLARE 
	__querytext text;
BEGIN
CREATE EXTENSION IF NOT EXISTS tablefunc;
drop table if exists stocks_aggr_table cascade;
create table stocks_aggr_table as
   (
   select id, product_name, stk->>'stockCode' as stockcode, stk->>'quantity' as quantity
	  from public.products_stock_levels,
	  lateral jsonb_array_elements(stock_quantity_values) stk
	);
grant all on stocks_aggr_table to hibernateuser;

__querytext := (select * from pivotcode('stocks_aggr_table','id','stockcode','max(quantity)','varchar'));
drop view if exists stock_levels_view;
execute 'CREATE VIEW stock_levels_view AS ' || __querytext;

grant all on stock_levels_view to hibernateuser;

-- RAISE NOTICE 'CREATE VIEW my_view AS [%]', __querytext;
return 1;
END;
$BODY$;

ALTER FUNCTION public.regen_stl_view()
    OWNER TO postgres;

GRANT EXECUTE ON FUNCTION public.regen_stl_view() TO hibernateuser WITH GRANT OPTION;

GRANT EXECUTE ON FUNCTION public.regen_stl_view() TO postgres;

REVOKE ALL ON FUNCTION public.regen_stl_view() FROM PUBLIC;
