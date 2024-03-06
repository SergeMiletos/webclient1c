-- FUNCTION: public.pivotcode(character varying, character varying, character varying, character varying, character varying)

-- DROP FUNCTION IF EXISTS public.pivotcode(character varying, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION public.pivotcode(
	tablename character varying,
	rowc character varying,
	colc character varying,
	cellc character varying,
	celldatatype character varying)
    RETURNS character varying
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE PARALLEL UNSAFE
AS $BODY$
declare
    dynsql1 varchar;
    dynsql2 varchar;
    columnlist varchar;
begin
    -- 1. retrieve list of column names.
    dynsql1 = 'select string_agg(distinct ''_stock''||'||colc||'||'' '||celldatatype||''','','' order by ''_stock''||'||colc||'||'' '||celldatatype||''') from '||tablename||';';
    execute dynsql1 into columnlist;
    -- 2. set up the crosstab query
    dynsql2 = 'select psl.product_code, psl.product_name, psl.uom, newtable.* from crosstab (
 ''select '||rowc||','||colc||','||cellc||' from '||tablename||' group by 1,2 order by 1,2'',
 ''select distinct '||colc||' from '||tablename||' order by 1''
 )
 as newtable (
 '||rowc||' varchar,'||columnlist||'
 ) join products_stock_levels psl on newtable.id = psl.id;';
    return dynsql2;
end
$BODY$;

ALTER FUNCTION public.pivotcode(character varying, character varying, character varying, character varying, character varying)
    OWNER TO postgres;

GRANT EXECUTE ON FUNCTION public.pivotcode(character varying, character varying, character varying, character varying, character varying) TO PUBLIC;

GRANT EXECUTE ON FUNCTION public.pivotcode(character varying, character varying, character varying, character varying, character varying) TO hibernateuser WITH GRANT OPTION;

GRANT EXECUTE ON FUNCTION public.pivotcode(character varying, character varying, character varying, character varying, character varying) TO postgres;

