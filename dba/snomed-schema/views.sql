-- Fármaco comercial y fármaco genérico
CREATE MATERIALIZED VIEW snomedct.v_commercial_medication AS
SELECT DISTINCT ct2.id AS commercial_sctid, ct2.term AS commercial_pt, ct3.id AS generic_sctid, ct3.term AS generic_pt
FROM snomedct.relationship r 
JOIN snomedct.concept_term ct ON (r.sourceid = ct.id)
JOIN snomedct.concept_term ct2 ON (r.destinationid  = ct2.id)
JOIN snomedct.relationship r2 ON (ct2.id = r2.sourceid)
JOIN snomedct.concept_term ct3 ON (r2.destinationid = ct3.id)
JOIN (
	SELECT DISTINCT s.sctid 
	FROM public.snomed s 
	JOIN public.snomed_related_group srg ON (srg.snomed_id = s.id)
	JOIN public.snomed_group sg ON (sg.id = srg.group_id)
	WHERE sg.description = 'MEDICINE_WITH_UNIT_OF_PRESENTATION'
	AND sg.institution_id = -1
) AS mwuop ON (mwuop.sctid = ct3.id)
WHERE r.active = '1'
AND r2.active = '1' 
AND r.typeid = '774160008'
AND r2.typeid = '116680003'
AND ct."type" = 'syn'
AND ct2."type" = 'syn'
AND ct3."type" = 'syn'
AND ct3.acceptability = 'preferido';

-- Unidades de presentación
CREATE MATERIALIZED VIEW snomedct.v_medication_presentation_unit AS
SELECT r.destinationid AS sctid, rcv.value AS presentation_unit_quantity
FROM snomedct.relationship r
JOIN snomedct.concept_term cpt ON (r.sourceid = cpt.id) 
JOIN snomedct.relationship_concrete_value rcv ON (cpt.id = rcv.sourceid)
WHERE r.active = '1'
AND r.typeid = '774160008'
AND rcv.typeid = '1142142004'
AND cpt."type" = 'syn'
AND rcv.active = '1'
UNION
SELECT DISTINCT vcm.generic_sctid AS sctid, rcv.value AS presentation_unit_quantity
FROM snomedct.relationship r
JOIN snomedct.concept_term cpt ON (r.sourceid = cpt.id) 
JOIN snomedct.relationship_concrete_value rcv ON (cpt.id = rcv.sourceid)
JOIN snomedct.v_commercial_medication vcm ON (vcm.commercial_sctid  = r.destinationid)
WHERE r.active = '1'
AND r.typeid = '774160008'
AND rcv.typeid = '1142142004'
AND cpt."type" = 'syn'
AND rcv.active = '1';
