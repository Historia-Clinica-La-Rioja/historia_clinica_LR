-- Fármaco comercial y fármaco genérico
CREATE OR REPLACE VIEW snomedct.v_commercial_medication AS
SELECT DISTINCT ct2.id as commercial_sctid, ct2.term as commercial_pt,
       ct3.id AS generic_sctid, ct3.term AS generic_pt
FROM snomedct.relationship r 
JOIN snomedct.concept_term ct ON (r.sourceid = ct.id)
JOIN snomedct.concept_term ct2 ON (r.destinationid  = ct2.id)
join snomedct.relationship r2 on (ct2.id = r2.sourceid)
join snomedct.concept_term ct3 on (r2.destinationid = ct3.id)
WHERE r.active = '1'
AND r2.active = '1' 
AND r.typeid = '774160008'
AND r2.typeid = '116680003'
AND ct."type" = 'syn'
AND ct2."type" = 'syn'
AND ct3."type" = 'syn'
AND ct3.acceptability = 'preferido'