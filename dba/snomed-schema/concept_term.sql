INSERT INTO snomedct.concept_term (id, term, def_status, type, acceptability)
SELECT DISTINCT 
    c.id,
    d.term,
    CASE
        WHEN c.definitionstatusid = '900000000000074008' THEN 'primitivo'
        WHEN c.definitionstatusid = '900000000000073002' THEN 'definido'
    END AS def_status,
    CASE
        WHEN d.typeid = '900000000000013009' THEN 'syn'
        WHEN d.typeid = '900000000000003001' THEN 'fsn'
    END AS type,
    CASE
        WHEN l.acceptabilityid = '900000000000548007' THEN 'preferido'
        WHEN l.acceptabilityid = '900000000000549004' THEN 'aceptable'
    END AS acceptability
FROM
    snomedct.concept AS c 
    JOIN snomedct.description AS d ON c.id = d.conceptid
    JOIN snomedct.langrefset AS l ON d.id = l.referencedcomponentid
WHERE
    c.active = '1'
    AND d.active = '1'
    AND d.languagecode = 'es'
    AND l.active = '1';