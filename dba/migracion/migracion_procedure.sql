CREATE PROCEDURE quilmes.migrate(offset_value integer, from_schema VARCHAR(50), to_schema VARCHAR(50), prefix_username VARCHAR(50))
    language plpgsql
as
$$
DECLARE
    Query VARCHAR(5000);
    temprow RECORD;
    temprow2 RECORD;
    temprow3 RECORD;
    exists_patient BOOLEAN;
BEGIN
    IF (offset_value < 1000) THEN
        RAISE EXCEPTION 'No se puede elegir un offset menor a 1000';
    END IF;

    IF (prefix_username = '') OR (prefix_username IS NULL)THEN
        RAISE EXCEPTION 'El prefijo usado para los nombres de usuarios es obligatorio';
    END IF;

    -- Direcciones
    query := 'INSERT INTO ' || to_schema || '.address  (id, street, number, floor, apartment, quarter, city_id, postcode, latitude, longitude)
    SELECT (-'|| offset_value ||'-a.id), a.street, a.number, a.floor, a.apartment, a.quarter, a.city_id,
           a.postcode, a.latitude, a.longitude
    FROM '|| from_schema || '.address AS a';

    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Personas
    query := 'INSERT INTO ' || to_schema || '.person (id, first_name, middle_names, last_name, other_last_names, identification_type_id,
                                identification_number, gender_id, birth_date)
    SELECT (-'|| offset_value ||'-p.id), p.first_name, p.middle_names, p.last_name, p.other_last_names,
           p.identification_type_id, p.identification_number, p.gender_id, p.birth_date
    FROM '|| from_schema || '.person AS p';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.person_extended (person_id, cuil, mothers_last_name, address_id, phone_number, email, religion,
                                         name_self_determination, gender_self_determination, photo_file_path, ethnicity_id,
                                         education_level_id, occupation_id, other_gender_self_determination, phone_prefix)
    SELECT (-'|| offset_value ||'-pe.person_id), pe.cuil, pe.mothers_last_name, (-'|| offset_value ||'-pe.address_id),
           pe.phone_number, pe.email, pe.religion, pe.name_self_determination, pe.gender_self_determination,
           pe.photo_file_path, pe.ethnicity_id, pe.education_level_id, pe.occupation_id,
           pe.other_gender_self_determination, pe.phone_prefix
    FROM '|| from_schema || '.person_extended AS pe';
    EXECUTE(query);


    CREATE TEMPORARY TABLE temp_duplicate_patient_ids (id int not null) ON COMMIT DROP;

    FOR temprow IN EXECUTE
        'SELECT pa.id AS id, pa.person_id, type_id, possible_duplicate, pa.national_id,
        pa.identity_verification_status_id, pa.comments,
        UPPER(p.identification_number::varchar(11)) AS identification_number, p.gender_id, p.birth_date
        FROM '|| from_schema || '.patient AS pa
        JOIN '|| from_schema || '.person AS p ON (p.id = pa.person_id)'
    LOOP
        --RAISE NOTICE '%', temprow;
        IF ((temprow.identification_number IS NULL) OR
           (temprow.gender_id IS NULL) OR
           (temprow.birth_date IS NULL))
        THEN
            IF (temprow.type_id = (1::smallint)) --SI ESTA COMO PERMANENTE VALIDADO EN LA BASE ORIGEN, LO TENGO QUE MARCAR COMO VALIDADO PARA QUE EL NUEVO DOMINIO LO FEDERE.
            THEN
                EXECUTE format('INSERT INTO %I.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                                 comments)
                VALUES ($1,$2,$3,$4,$5,$6,$7)', to_schema) USING
                 (-offset_value-temprow.id), (-offset_value-temprow.person_id), 2, temprow.possible_duplicate, temprow.national_id,
                 temprow.identity_verification_status_id, temprow.comments;
            ELSE
                EXECUTE format('INSERT INTO %I.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                                 comments)
                VALUES ($1,$2,$3,$4,$5,$6,$7)', to_schema) USING
                 (-offset_value-temprow.id), (-offset_value-temprow.person_id), temprow.type_id, temprow.possible_duplicate, temprow.national_id,
                 temprow.identity_verification_status_id, temprow.comments;
            END IF;
        ELSE
-- RAISE NOTICE 'ENTRO2 %', temprow;
            EXECUTE ('SELECT COUNT(p1.id) > 0
                      FROM '|| to_schema ||'.person AS p1
                      JOIN '|| to_schema ||'.patient AS pt1 ON (p1.id = pt1.person_id)
                      WHERE UPPER(p1.identification_number) = UPPER('''|| temprow.identification_number ||''')
                      AND p1.gender_id = '|| temprow.gender_id ||'
                      AND p1.birth_date = '''|| temprow.birth_date ||'''') INTO exists_patient;
            IF (exists_patient)
            THEN
                EXECUTE format('INSERT INTO %I.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                                     comments)
                VALUES ($1,$2,$3,$4,$5,$6,$7)', to_schema) USING
                 (-offset_value-temprow.id), (-offset_value-temprow.person_id), 3, true, temprow.national_id,
                    temprow.identity_verification_status_id, temprow.comments;
                INSERT INTO temp_duplicate_patient_ids (id) VALUES (temprow.id);
            ELSE
                --RAISE NOTICE 'Paciente nuevo -> %', temprow;
                IF (temprow.type_id = (1::smallint)) --SI ESTA COMO PERMANENTE VALIDADO EN LA BASE ORIGEN, LO TENGO QUE MARCAR COMO VALIDADO PARA QUE EL NUEVO DOMINIO LO FEDERE.
                THEN
                    EXECUTE format('INSERT INTO %I.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                                     comments)
                    VALUES ($1,$2,$3,$4,$5,$6,$7)', to_schema) USING
                     (-offset_value-temprow.id), (-offset_value-temprow.person_id), 2, temprow.possible_duplicate, temprow.national_id,
                     temprow.identity_verification_status_id, temprow.comments;
                ELSE
                    EXECUTE format('INSERT INTO %I.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                                     comments)
                    VALUES ($1,$2,$3,$4,$5,$6,$7)', to_schema) USING
                     (-offset_value-temprow.id), (-offset_value-temprow.person_id), temprow.type_id, temprow.possible_duplicate, temprow.national_id,
                     temprow.identity_verification_status_id, temprow.comments;
                END IF;
            END IF;
        END IF;
    END LOOP;


    --------------------------------------------------------------------------------------------------------------------
    -- Usuarios con username repetidos
    query := 'INSERT INTO ' || to_schema || '.users (id, created_on, deleted_on, deleted, updated_on, last_login, enable, username, created_by,
                               updated_by, deleted_by)
    SELECT (-'|| offset_value ||'-u.id), u.created_on, u.deleted_on, u.deleted, u.updated_on, u.last_login, u.enable,
           ('''||prefix_username||'-'' || u.username), (-'|| offset_value ||'-u.created_by), (-'|| offset_value ||'-u.updated_by), (-'|| offset_value ||'-u.deleted_by)
    FROM '|| from_schema || '.users AS u
    WHERE u.created_by != -1
    AND UPPER(u.username) IN (SELECT UPPER(u1.username) FROM '|| to_schema ||'.users AS u1);';
    EXECUTE format(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Usuarios con username no repetidos
    query := 'INSERT INTO ' || to_schema || '.users (id, created_on, deleted_on, deleted, updated_on, last_login, enable, username, created_by,
                               updated_by, deleted_by)
    SELECT (-'|| offset_value ||'-u.id), u.created_on, u.deleted_on, u.deleted, u.updated_on, u.last_login, u.enable,
           u.username, (-'|| offset_value ||'-u.created_by), (-'|| offset_value ||'-u.updated_by), (-'|| offset_value ||'-u.deleted_by)
    FROM '|| from_schema || '.users AS u
    WHERE u.created_by != -1
    AND UPPER(u.username) NOT IN (SELECT UPPER(u1.username) FROM '|| to_schema ||'.users AS u1);';
    EXECUTE format(query);

    query := 'INSERT INTO ' || to_schema || '.user_person (user_id, person_id)
    SELECT (-'|| offset_value ||'-up.user_id), (-'|| offset_value ||'-up.person_id)
    FROM '|| from_schema || '.user_person AS up
	JOIN '|| from_schema || '.users AS u ON  u.id = up.user_id
    WHERE u.created_by != -1;';
    EXECUTE format(query);


    query := 'INSERT INTO ' || to_schema || '.user_password (id, created_on, deleted_on, deleted, updated_on, hash_algorithm, password, salt,
                                       created_by, updated_by, deleted_by)
    SELECT (-'|| offset_value ||'-up.id), up.created_on, up.deleted_on, up.deleted, up.updated_on, up.hash_algorithm, up.password,
           up.salt, (-'|| offset_value ||'-up.created_by), (-'|| offset_value ||'-up.updated_by),(-'|| offset_value ||'-up.deleted_by)
    FROM '|| from_schema || '.users AS u
    JOIN '|| from_schema || '.user_password AS up ON u.id = up.id
    WHERE u.created_by != -1';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Roles
    query := 'INSERT INTO ' || to_schema || '.user_role (role_id, user_id, created_on, deleted_on, deleted, updated_on, created_by, updated_by,
                                   institution_id, deleted_by)
    SELECT ur.role_id, (-'|| offset_value ||'-ur.user_id), ur.created_on, ur.deleted_on, ur.deleted, ur.updated_on,
           (-'|| offset_value ||'-ur.created_by), (-'|| offset_value ||'-ur.updated_by), (-'|| offset_value ||'-ur.institution_id),
           (-'|| offset_value ||'-ur.deleted_by)
    FROM '|| from_schema || '.user_role AS ur
    JOIN ' || from_schema || '.users AS u ON u.id = ur.user_id
    WHERE u.created_by != -1';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Keys

    -- Aca habría que ver porque hay UQ sobre el campo key, y por ahí habría que ajustar y hacer la UQ sobre los dos campos (user_id, key)
    --SELECT INTO migrate.user_key (user_id, "key")
    --VALUES ((-'|| offset_value ||'-temprow.uk_user_id), temprow.key)
    --FROM public.user_key AS uk
    --JOIN public.users AS u ON u.id = uk.user_id
    --WHERE u.created_by != -1

    --------------------------------------------------------------------------------------------------------------------
    -- Institution
   query := 'INSERT INTO ' || to_schema || '.institution (id, name, address_id, website, phone_number, email, cuit, sisa_code, timezone,
                                     dependency_id, province_code)
    SELECT (-'|| offset_value ||'-i.id), i.name, (-'|| offset_value ||'-i.address_id), i.website, i.phone_number,
           i.email, i.cuit, i.sisa_code, i.timezone, i.dependency_id, i.province_code
    FROM '|| from_schema || '.institution AS i';
    EXECUTE(query);


    --------------------------------------------------------------------------------------------------------------------
    -- Sector
    query := 'INSERT INTO ' || to_schema || '.sector (id, institution_id, description, sector_type_id, sector_organization_id, age_group_id,
                                care_type_id, hospitalization_type_id, sector_id)
    SELECT (-'|| offset_value ||'-s.id), (-'|| offset_value ||'-s.institution_id), s.description,
           s.sector_type_id, s.sector_organization_id, s.age_group_id, s.care_type_id,
           s.hospitalization_type_id, (-'|| offset_value ||'-s.sector_id)
    FROM '|| from_schema || '.sector AS s';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Clinical specialty Sector
    query := 'INSERT INTO ' || to_schema || '.clinical_specialty_sector (id, description, clinical_specialty_id, sector_id)
    SELECT (-'|| offset_value ||'-css.id), css.description, css.clinical_specialty_id, (-'|| offset_value ||'-css.sector_id)
    FROM '|| from_schema || '.clinical_specialty_sector AS css';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Rooms
    query := 'INSERT INTO ' || to_schema || '.room (id, clinical_specialty_sector_id, description, type, discharge_date, room_number, sector_id)
    SELECT (-'|| offset_value ||'-r.id), (-'|| offset_value ||'-r.clinical_specialty_sector_id), r.description, r.type,
           r.discharge_date, r.room_number, (-'|| offset_value ||'-r.sector_id)
    FROM '|| from_schema || '.room AS r';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Bed
    query := 'INSERT INTO ' || to_schema || '.bed (id, room_id, bed_category_id, bed_number, enabled, available, free)
    SELECT (-'|| offset_value ||'-b.id),(-'|| offset_value ||'-b.room_id), b.bed_category_id, b.bed_number,
           b.enabled, b.available, b.free
    FROM '|| from_schema || '.bed AS b';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Profesionales
    query := 'INSERT INTO ' || to_schema || '.healthcare_professional (id, license_number, person_id, deleted, deleted_by, deleted_on, created_on,
                                                 updated_on, created_by, updated_by)
    SELECT (-'|| offset_value ||'-hp.id), hp.license_number, (-'|| offset_value ||'-hp.person_id), hp.deleted,
           (-'|| offset_value ||'-hp.deleted_by), hp.deleted_on, hp.created_on, hp.updated_on,
           (-'|| offset_value ||'-hp.created_by), (-'|| offset_value ||'-hp.updated_by)
    FROM '|| from_schema || '.healthcare_professional AS hp';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Profesionales especialidades
    query := 'INSERT INTO ' || to_schema || '.healthcare_professional_specialty (id, healthcare_professional_id, professional_specialty_id,
                                                           clinical_specialty_id, deleted, deleted_by, deleted_on,
                                                           created_on, updated_on, created_by, updated_by)
    SELECT (-'|| offset_value ||'-hps.id), (-'|| offset_value ||'-hps.healthcare_professional_id), professional_specialty_id,
           hps.clinical_specialty_id, hps.deleted, (-'|| offset_value ||'-hps.deleted_by), hps.deleted_on,
           hps.created_on, hps.updated_on, (-'|| offset_value ||'-hps.created_by), (-'|| offset_value ||'-hps.updated_by)
    FROM '|| from_schema || '.healthcare_professional_specialty AS hps';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Prepagas
    FOR temprow IN EXECUTE 'SELECT mc.id AS id, name, (-'|| offset_value ||'-created_by) AS created_by, created_on,
               (-'|| offset_value ||'-updated_by) AS updated_by,
               updated_on, deleted, (-'|| offset_value ||'-deleted_by) AS deleted_by, deleted_on,
               CASE WHEN cuit IS NULL THEN NULL ELSE CONCAT(''-'',cuit) end AS cuit,
               phi.plan
        FROM '|| from_schema ||'.medical_coverage AS mc
        JOIN '|| from_schema ||'.private_health_insurance AS phi ON mc.id = phi.id'
    LOOP
        EXECUTE format('INSERT INTO %I.medical_coverage (id, name, created_by, created_on, updated_by, updated_on, deleted, deleted_by,
                                              deleted_on, cuit)
        VALUES ((-$1-$2),$3,$4,$5,$6,$7,$8,$9,$10,$11)', to_schema) USING
         offset_value, temprow.id, temprow.name, temprow.created_by, temprow.created_on, temprow.updated_by,
         temprow.updated_on, temprow.deleted, temprow.deleted_by, temprow.deleted_on, temprow.cuit;

        EXECUTE format('INSERT INTO %I.private_health_insurance (id, plan) VALUES ((-$1-$2), $3)', to_schema) USING offset_value,
            temprow.id, temprow.plan;

        FOR temprow2 IN EXECUTE
            'SELECT phip.id AS id, phip.plan, phip.private_health_insurance_id
            FROM '|| from_schema || '.private_health_insurance_plan AS phip
            WHERE private_health_insurance_id = '||temprow.id
        LOOP
            EXECUTE format('INSERT INTO %I.private_health_insurance_plan (id, private_health_insurance_id, plan) VALUES ((-$1-$2), (-$3-$4), $5)', to_schema)
                USING offset_value, temprow2.id, offset_value, temprow2.private_health_insurance_id, temprow2.plan;
        END LOOP;
    END LOOP;

    --- WHERE phid.private_health_insurance_plan_id = temprow2.id
    --- OR phid.private_health_insurance_plan_id IS NULL
    FOR temprow3 IN EXECUTE 'SELECT phid.id AS id, start_date, end_date,
               CASE WHEN phid.private_health_insurance_plan_id IS NULL
                        THEN NULL
                    ELSE (-'|| offset_value ||'-phid.private_health_insurance_plan_id)
               END AS private_health_insurance_plan_id
        FROM '|| from_schema || '.private_health_insurance_details AS phid'
    LOOP
        -- RAISE NOTICE "private_health_insurance_details % and %", temprow3.id, temprow3.private_health_insurance_plan_id;
        EXECUTE format('INSERT INTO %I.private_health_insurance_details (id, start_date, end_date, private_health_insurance_plan_id) ' ||
                       'VALUES ((-$1-$2), $3, $4, $5)', to_schema)
           USING offset_value, temprow3.id, temprow3.start_date, temprow3.end_date, temprow3.private_health_insurance_plan_id;
    END LOOP;

    --------------------------------------------------------------------------------------------------------------------
    -- Obras sociales
    FOR temprow IN EXECUTE 'SELECT (-'|| offset_value ||'-mc.id) AS id, name, (-'|| offset_value ||'-created_by) AS created_by, created_on,
               (-'|| offset_value ||'-updated_by) AS updated_by,
               updated_on, deleted, (-'|| offset_value ||'-deleted_by) AS deleted_by, deleted_on, cuit, rnos, acronym
        FROM '|| from_schema ||'.medical_coverage AS mc
        JOIN '|| from_schema ||'.health_insurance AS hi ON mc.id = hi.id
        WHERE UPPER(name) NOT IN (SELECT UPPER(name) FROM '|| to_schema ||'.medical_coverage AS mcc
                                  JOIN '|| to_schema ||'.health_insurance AS hii ON mcc.id = hii.id) '
    LOOP
        EXECUTE format('INSERT INTO %I.medical_coverage (id, name, created_by, created_on, updated_by, updated_on, deleted, deleted_by, deleted_on, cuit)' ||
                       'VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)', to_schema)
            USING temprow.id, temprow.name, temprow.created_by, temprow.created_on, temprow.updated_by, temprow.updated_on,
                temprow.deleted, temprow.deleted_by, temprow.deleted_on, temprow.cuit;

        EXECUTE format('INSERT INTO %I.health_insurance (id, rnos, acronym)' ||
                       'VALUES ($1, $2, $3)',to_schema) USING temprow.id, temprow.rnos, temprow.acronym;
    END LOOP;

    --------------------------------------------------------------------------------------------------------------------
    -- Cobertura médica de paciente

    --RAISE NOTICE 'patient_medical_coverage ';
    --RAISE NOTICE "patient_medical_coverage % and %", temprow.id, temprow.private_health_insurance_details_id;
    --RAISE NOTICE "patient_medical_coverage % and %", temprow.id, temprow.private_health_insurance_details_id;

    FOR temprow IN EXECUTE 'SELECT pmc.id AS id, (-'|| offset_value ||'-patient_id) AS patient_id,
               (-'|| offset_value ||'-pmc.medical_coverage_id) AS medical_coverage_id, active, vigency_date,
               affiliate_number,
               pmc.private_health_insurance_details_id AS private_health_insurance_details_id
        FROM '|| from_schema ||'.patient_medical_coverage AS pmc
        JOIN '|| from_schema ||'.medical_coverage AS mc ON pmc.medical_coverage_id = mc.id
        JOIN '|| from_schema ||'.private_health_insurance AS phi ON mc.id = phi.id'
       -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    LOOP
        EXECUTE format('INSERT INTO %I.patient_medical_coverage (id, patient_id, medical_coverage_id, active, vigency_date,
                                                  affiliate_number, private_health_insurance_details_id)' ||
                       'VALUES ((-$1-$2), $3, $4, $5, $6, $7, (-$1-$8))', to_schema)
            USING offset_value, temprow.id, temprow.patient_id, temprow.medical_coverage_id, temprow.active, temprow.vigency_date,
            temprow.affiliate_number, temprow.private_health_insurance_details_id;
    END LOOP;

   -- RAISE EXCEPTION 'LLEGO';

    FOR temprow IN EXECUTE 'SELECT pmc.id AS id, (-'|| offset_value ||'-patient_id) AS patient_id,
           pmc.medical_coverage_id AS medical_coverage_id, active, vigency_date,
           affiliate_number,
           pmc.private_health_insurance_details_id AS private_health_insurance_details_id, mc.name
        FROM '|| from_schema ||'.patient_medical_coverage AS pmc
        JOIN '|| from_schema ||'.medical_coverage AS mc ON pmc.medical_coverage_id = mc.id
        JOIN '|| from_schema ||'.health_insurance AS hi ON mc.id = hi.id'
       -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    LOOP
       -- RAISE NOTICE '%', temprow.name;
        EXECUTE format('INSERT INTO %I.patient_medical_coverage (id, patient_id, medical_coverage_id, active, vigency_date,
                                                  affiliate_number, private_health_insurance_details_id)' ||
                       'VALUES ((-$1-$2), $3,
                       (SELECT mcc.id FROM %I.medical_coverage AS mcc
                       JOIN %I.health_insurance AS hii ON mcc.id = hii.id
                       WHERE UPPER(mcc.name) = UPPER($4)
                       LIMIT  1),
                       $5, $6, $7, (-$1-$8))', to_schema, to_schema, to_schema)
            USING offset_value, temprow.id, temprow.patient_id, temprow.name, temprow.active, temprow.vigency_date,
            temprow.affiliate_number, temprow.private_health_insurance_details_id;
    END LOOP;

    --------------------------------------------------------------------------------------------------------------------
    -- Notas

    query := 'INSERT INTO ' || to_schema || '.note (id, description)
    SELECT (-'|| offset_value ||'-id), description
    FROM '|| from_schema ||'.note';
    EXECUTE(query);


    --------------------------------------------------------------------------------------------------------------------
    -- Documentos
    query := 'INSERT INTO ' || to_schema || '.document (id, source_id, other_note_id, status_id, type_id, created_by, updated_by, created_on,
                                  updated_on, physical_exam_note_id, studies_summary_note_id, evolution_note_id,
                                  clinical_impression_note_id, current_illness_note_id, indications_note_id, source_type_id,
                                  deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-source_id), (-'|| offset_value ||'-other_note_id), status_id, type_id, (-'|| offset_value ||'-created_by),
           (-'|| offset_value ||'-updated_by), created_on, updated_on, (-'|| offset_value ||'-physical_exam_note_id),
           (-'|| offset_value ||'-studies_summary_note_id), (-'|| offset_value ||'-evolution_note_id), (-'|| offset_value ||'-clinical_impression_note_id),
           (-'|| offset_value ||'-current_illness_note_id), (-'|| offset_value ||'-indications_note_id), source_type_id,
           deleted, (-'|| offset_value ||'-deleted_by), deleted_on
    FROM  '|| from_schema ||'.document';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);


    query := 'INSERT INTO ' || to_schema || '.document_file (id, source_id, type_id, file_path, file_name, created_by, created_on, updated_by,
                                       updated_on, uuid_file, source_type_id, checksum, deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-source_id), type_id, file_path, file_name, (-'|| offset_value ||'-created_by), created_on,
           (-'|| offset_value ||'-updated_by), updated_on, uuid_file, source_type_id, checksum, deleted,
           (-'|| offset_value ||'-deleted_by), deleted_on
    FROM '|| from_schema ||'.document_file';
    EXECUTE(query);


    --------------------------------------------------------------------------------------------------------------------
    -- Internaciones
    query := 'INSERT INTO ' || to_schema || '.internment_episode (id, patient_id, bed_id, clinical_specialty_id, status_id, note_id,
                                            anamnesis_doc_id, epicrisis_doc_id, entry_date, created_by, updated_by,
                                            created_on, updated_on, institution_id, probable_discharge_date, deleted,
                                            deleted_by, deleted_on, patient_medical_coverage_id)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), (-'|| offset_value ||'-bed_id), clinical_specialty_id, status_id,
           (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-anamnesis_doc_id), (-'|| offset_value ||'-epicrisis_doc_id), entry_date,
           (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on, (-'|| offset_value ||'-institution_id),
           probable_discharge_date, deleted, (-'|| offset_value ||'-deleted_by), deleted_on, (-'|| offset_value ||'-patient_medical_coverage_id)
    FROM  '|| from_schema ||'.internment_episode';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.evolution_note_document (document_id, internment_episode_id)
    SELECT (-'|| offset_value ||'-document_id), (-'|| offset_value ||'-internment_episode_id)
    FROM  '|| from_schema ||'.evolution_note_document';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.patient_discharge (internment_episode_id, administrative_discharge_date, discharge_type_id,
                                           created_by, updated_by, created_on, updated_on, medical_discharge_date, deleted,
                                           deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-internment_episode_id), administrative_discharge_date, discharge_type_id, (-'|| offset_value ||'-created_by),
           (-'|| offset_value ||'-updated_by), created_on, updated_on, medical_discharge_date, deleted, (-'|| offset_value ||'-deleted_by), deleted_on
    FROM  '|| from_schema ||'.patient_discharge';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.healthcare_professional_group (healthcare_professional_id, internment_episode_id, responsible)
    SELECT (-'|| offset_value ||'-healthcare_professional_id), (-'|| offset_value ||'-internment_episode_id), responsible
    FROM  '|| from_schema ||'.healthcare_professional_group';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Consultas ambulatorias
    query := 'INSERT INTO ' || to_schema || '.outpatient_consultation (id, patient_id, clinical_specialty_id, institution_id, start_date,
                                                 document_id, doctor_id, billable, created_by, created_on, updated_by,
                                                 updated_on, deleted, deleted_by, deleted_on, patient_medical_coverage_id)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), clinical_specialty_id, (-'|| offset_value ||'-institution_id), start_date,
           (-'|| offset_value ||'-document_id), (-'|| offset_value ||'-doctor_id), billable, (-'|| offset_value ||'-created_by), created_on,
           (-'|| offset_value ||'-updated_by), updated_on, deleted, (-'|| offset_value ||'-deleted_by), deleted_on, (-'|| offset_value ||'-patient_medical_coverage_id)
    FROM  '|| from_schema ||'.outpatient_consultation';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.outpatient_consultation_reasons (reason_id, outpatient_consultation_id)
    SELECT reason_id, (-'|| offset_value ||'-outpatient_consultation_id)
    FROM  '|| from_schema ||'.outpatient_consultation_reasons';
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Vacunación
    query := 'INSERT INTO ' || to_schema || '.vaccine_consultation (id, patient_id, clinical_specialty_id, institution_id, performed_date,
                                              doctor_id, billable, created_by, created_on, updated_by, updated_on, deleted,
                                              deleted_by, deleted_on, patient_medical_coverage_id)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), clinical_specialty_id, (-'|| offset_value ||'-institution_id), performed_date,
           (-'|| offset_value ||'-doctor_id), billable, (-'|| offset_value ||'-created_by), created_on,
           (-'|| offset_value ||'-updated_by), updated_on, deleted, (-'|| offset_value ||'-deleted_by), deleted_on,
           (-'|| offset_value ||'-patient_medical_coverage_id)
    FROM  '|| from_schema ||'.vaccine_consultation';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Enfermeria
    query := 'INSERT INTO ' || to_schema || '.nursing_consultation (id, patient_id, clinical_specialty_id, institution_id, performed_date,
                                              doctor_id, billable, created_by, created_on, updated_by, updated_on, deleted,
                                              deleted_by, deleted_on, patient_medical_coverage_id)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), clinical_specialty_id, (-'|| offset_value ||'-institution_id), performed_date,
           (-'|| offset_value ||'-doctor_id), billable, (-'|| offset_value ||'-created_by), created_on,
           (-'|| offset_value ||'-updated_by), updated_on, deleted, (-'|| offset_value ||'-deleted_by), deleted_on,
           (-'|| offset_value ||'-patient_medical_coverage_id)
    FROM  '|| from_schema ||'.nursing_consultation';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Recetas
    query := 'INSERT INTO ' || to_schema || '.medication_request (id, patient_id, medical_coverage_id, status_id, intent_id, category_id,
                                            doctor_id, has_recipe, note_id, institution_id, request_date, created_by,
                                            updated_by, created_on, updated_on, deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), (-'|| offset_value ||'-medical_coverage_id), status_id, intent_id,
           category_id, (-'|| offset_value ||'-doctor_id), has_recipe, (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-institution_id),
           request_date, (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on, deleted,
           (-'|| offset_value ||'-deleted_by), deleted_on
    FROM  '|| from_schema ||'.medication_request';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    --------------------------------------------------------------------------------------------------------------------
    -- Ordenes
    query := 'INSERT INTO ' || to_schema || '.service_request (id, patient_id, medical_coverage_id, status_id, intent_id, category_id, doctor_id,
                                         note_id, institution_id, request_date, created_by, updated_by, created_on,
                                         updated_on, deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-patient_id), (-'|| offset_value ||'-medical_coverage_id), status_id, intent_id,
           category_id, (-'|| offset_value ||'-doctor_id), (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-institution_id),
           request_date, (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on, deleted,
           (-'|| offset_value ||'-deleted_by), deleted_on
    FROM  '|| from_schema ||'.service_request';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    FOR temprow IN EXECUTE 'SELECT s.pt, s.sctid, s.parent_fsn, s.parent_id
        FROM '|| from_schema ||'.snomed AS s'
    LOOP
        IF (NOT EXISTS (SELECT 1 FROM snomed WHERE sctid = temprow.sctid  AND pt = temprow.pt)) THEN

            EXECUTE format('INSERT INTO %I.snomed (sctid, pt, parent_id, parent_fsn)' ||
                       'VALUES ($1, $2, $3, $4)', to_schema)
                USING temprow.sctid, temprow.pt, temprow.parent_id, temprow.parent_fsn;
        END IF;
    END LOOP;

    query := 'INSERT INTO ' || to_schema || '.health_condition (id, patient_id, sctid_code, status_id, verification_status_id, start_date,
                                          inactivation_date, personal, note_id, created_by, updated_by, created_on,
                                          updated_on, problem_id, main, cie10_codes, snomed_id, severity, deleted,
                                          deleted_by, deleted_on)
            SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), null, status_id, verification_status_id,
                   start_date, inactivation_date, personal, (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by),
                   (-'|| offset_value ||'-updated_by), created_on, updated_on, problem_id, main, cie10_codes, ms.id ,severity, deleted,
                   (-'|| offset_value ||'-deleted_by), deleted_on
            FROM '|| from_schema ||'.health_condition AS hc
            JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
            JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_health_condition (health_condition_id, document_id)
    SELECT (-'|| offset_value ||'-m.health_condition_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_health_condition AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.observation_lab (id, patient_id, sctid_code, status_id, category_id, "value", effective_time,
                                         note_id, created_by, updated_by, created_on, updated_on, cie10_codes, snomed_id,
                                         deleted, deleted_by, deleted_on)
            SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), null, status_id, category_id,
                   hc.value, effective_time, (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by),
                   (-'|| offset_value ||'-updated_by), created_on, updated_on, cie10_codes, ms.id, deleted,
                   (-'|| offset_value ||'-deleted_by), deleted_on
            FROM '|| from_schema ||'.observation_lab AS hc
            JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
            JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_lab (observation_lab_id, document_id)
    SELECT (-'|| offset_value ||'-m.observation_lab_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_lab AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.observation_vital_sign (id, patient_id, sctid_code, status_id, category_id, "value", effective_time,
                                                note_id, created_by, updated_by, created_on, updated_on, cie10_codes, snomed_id,
                                                deleted, deleted_by, deleted_on)
            SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), null, status_id, category_id,
                   hc.value, effective_time, (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by),
                   (-'|| offset_value ||'-updated_by), created_on, updated_on, cie10_codes, ms.id, deleted,
                   (-'|| offset_value ||'-deleted_by), deleted_on
            FROM '|| from_schema ||'.observation_vital_sign AS hc
            JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
            JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_vital_sign (observation_vital_sign_id, document_id)
    SELECT (-'|| offset_value ||'-m.observation_vital_sign_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_vital_sign AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.procedures (id, patient_id, sctid_code, status_id, note_id, performed_date, created_by, updated_by,
                                    created_on, updated_on, cie10_codes, snomed_id, deleted, deleted_by, deleted_on)
            SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), null, status_id, (-'|| offset_value ||'-note_id), performed_date,
                   (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on, cie10_codes, ms.id, deleted,
                   (-'|| offset_value ||'-deleted_by), deleted_on
            FROM '|| from_schema ||'.procedures AS hc
            JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
            JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_procedure (procedure_id, document_id)
    SELECT (-'|| offset_value ||'-m.procedure_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_procedure AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.allergy_intolerance (id, patient_id, sctid_code, status_id, verification_status_id, start_date,
                                             note_id, created_by, updated_by, created_on, updated_on, cie10_codes,
                                             snomed_id, category_id, type, criticality, deleted, deleted_by, deleted_on)
            SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), null, status_id, verification_status_id, start_date,
                   (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on,
                   cie10_codes, ms.id, category_id, type, criticality, deleted,
                   (-'|| offset_value ||'-deleted_by), deleted_on
            FROM '|| from_schema ||'.allergy_intolerance AS hc
            JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
            JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_allergy_intolerance (allergy_intolerance_id, document_id)
    SELECT (-'|| offset_value ||'-m.allergy_intolerance_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_allergy_intolerance AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.diagnostic_report (id, patient_id, status_id, health_condition_id, effective_time, link, note_id,
                                           created_by, updated_by, created_on, updated_on, cie10_codes, snomed_id, deleted,
                                           deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-hc.id), (-'|| offset_value ||'-patient_id), status_id, (-'|| offset_value ||'-health_condition_id), effective_time,
           link, (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on,
           cie10_codes, ms.id, deleted,
           (-'|| offset_value ||'-deleted_by), deleted_on
    FROM '|| from_schema ||'.diagnostic_report AS hc
    JOIN '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
    JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_diagnostic_report (diagnostic_report_id, document_id)
    SELECT (-'|| offset_value ||'-m.diagnostic_report_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_diagnostic_report AS m';
    EXECUTE(query);

    FOR temprow IN EXECUTE 'SELECT (-'|| offset_value ||'-hc.id) AS id, (-'|| offset_value ||'-patient_id) as patient_id, null, status_id,
               (-'|| offset_value ||'-institution_id) AS institution_id, administration_date,
               expiration_date, (-'|| offset_value ||'-note_id) AS note_id,
               (-'|| offset_value ||'-created_by) AS created_by, (-'|| offset_value ||'-updated_by) AS updated_by, created_on, updated_on,
               cie10_codes, ms.id AS snomed_id, deleted,
               (-'|| offset_value ||'-deleted_by) AS deleted_by, deleted_on, snomed_commercial_id, scheme_id, condition_id, dose_order, lot_number,
               billable, dose, doctor_info, institution_info
        FROM '|| from_schema ||'.inmunization AS hc
        JOIN  '|| from_schema ||'.snomed AS s ON hc.snomed_id = s.id
        JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)'
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    LOOP
        EXECUTE format('INSERT INTO %I.inmunization (id, patient_id, sctid_code, status_id, institution_id, administration_date,
                                          expiration_date, note_id, created_by, updated_by, created_on, updated_on, cie10_codes,
                                          snomed_id, deleted, deleted_by, deleted_on, snomed_commercial_id, scheme_id,
                                          condition_id, dose_order, lot_number, billable, dose, doctor_info, institution_info)' ||
                   'VALUES ($1, $2, null, $3, $4, $5, $6, $7, $8, $9, $10, $11, $12, $13, $14, $15, $16,
                   (SELECT ms.id AS snomed_commercial_id
                    FROM %I.snomed AS s
                    JOIN %I.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)
                    WHERE $17 = s.id), $18, $19, $20, $21, $22, $23, $24, $25) ', to_schema, from_schema, to_schema)
       USING temprow.id, temprow.patient_id, temprow.status_id, temprow.institution_id, temprow.administration_date,
       temprow.expiration_date, temprow.note_id, temprow.created_by, temprow.updated_by,
       temprow.created_on, temprow.updated_on, temprow.cie10_codes, temprow.snomed_id, temprow.deleted, temprow.deleted_by, temprow.deleted_on,
       temprow.snomed_commercial_id, temprow.scheme_id, temprow.condition_id, temprow.dose_order, temprow.lot_number,
       temprow.billable, temprow.dose, temprow.doctor_info, temprow.institution_info;
    END LOOP;

    query := 'INSERT INTO ' || to_schema || '.document_inmunization (inmunization_id, document_id)
    SELECT (-'|| offset_value ||'-m.inmunization_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_inmunization AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.dosage (id, sequence, "count", duration, duration_unit, frequency, period_unit, dose_quantity_id,
                                chronic, start_date, end_date, suspended_start_date, suspended_end_date, event)
    SELECT (-'|| offset_value ||'-id), sequence, count, duration, duration_unit, frequency, period_unit, dose_quantity_id,
           chronic, start_date, end_date, suspended_start_date, suspended_end_date, event
    FROM '|| from_schema ||'.dosage AS d';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.medication_statement (id, patient_id, sctid_code, status_id, note_id, created_by, updated_by,
                                              created_on, updated_on, dosage_id, health_condition_id, cie10_codes,
                                              snomed_id, deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-m.id), (-'|| offset_value ||'-patient_id), null, status_id,
           (-'|| offset_value ||'-note_id), (-'|| offset_value ||'-created_by), (-'|| offset_value ||'-updated_by), created_on, updated_on,
           (-'|| offset_value ||'-dosage_id),  (-'|| offset_value ||'-health_condition_id), cie10_codes, ms.id, deleted,
           (-'|| offset_value ||'-deleted_by), deleted_on
    FROM '|| from_schema ||'.medication_statement AS m
    JOIN '|| from_schema ||'.snomed AS s ON m.snomed_id = s.id
    JOIN '|| to_schema ||'.snomed AS ms ON (ms.sctid = s.sctid AND ms.pt = s.pt)';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.document_medicamention_statement (medication_statement_id, document_id)
    SELECT (-'|| offset_value ||'-m.medication_statement_id), (-'|| offset_value ||'-document_id)
    FROM '|| from_schema ||'.document_medicamention_statement AS m';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.doctors_office (id, institution_id, clinical_specialty_sector_id, description, opening_time,
                                   closing_time, topic)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-institution_id), (-'|| offset_value ||'-clinical_specialty_sector_id), description,
           opening_time, closing_time, topic
    FROM '|| from_schema ||'.doctors_office';
    EXECUTE(query);


    query := 'INSERT INTO ' || to_schema || '.diary (id, healthcare_professional_id, doctors_office_id, start_date, end_date, appointment_duration,
                          automatic_renewal, days_before_renew, professional_asign_shift, include_holiday, active,
                          created_by, created_on, updated_by, updated_on, deleted, deleted_by, deleted_on)
    SELECT (-'|| offset_value ||'-id), (-'|| offset_value ||'-healthcare_professional_id), (-'|| offset_value ||'-doctors_office_id), start_date, end_date,
           appointment_duration, automatic_renewal, days_before_renew, professional_asign_shift, include_holiday, active,
           (-'|| offset_value ||'-created_by), created_on,  (-'|| offset_value ||'-updated_by), updated_on, deleted,  (-'|| offset_value ||'-deleted_by), deleted_on
    FROM '|| from_schema ||'.diary';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.appointment (id, date_type_id, "hour", appointment_state_id, patient_id, is_overturn, created_by,
                                created_on, updated_by, updated_on, phone_number, patient_medical_coverage_id, deleted,
                                deleted_by, deleted_on, phone_prefix)
    SELECT (-'|| offset_value ||'-id), date_type_id, "hour", appointment_state_id, (-'|| offset_value ||'-patient_id), is_overturn,
           (-'|| offset_value ||'-created_by), created_on, (-'|| offset_value ||'-updated_by), updated_on, phone_number,
           (-'|| offset_value ||'-patient_medical_coverage_id), deleted, (-'|| offset_value ||'-deleted_by), deleted_on, phone_prefix
    FROM '|| from_schema ||'.appointment';
    -- || 'WHERE patient_id (-'|| offset_value ||'-patient_id) NOT IN (SELECT id FROM temp_duplicate_patient_ids) '
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.opening_hours (id, day_week_id, "from", "to")
    SELECT (-'|| offset_value ||'-id), day_week_id, "from", "to"
    FROM '|| from_schema ||'.opening_hours';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.diary_opening_hours (diary_id, opening_hours_id, medical_attention_type_id, overturn_count)
    SELECT (-'|| offset_value ||'-diary_id), (-'|| offset_value ||'-opening_hours_id), medical_attention_type_id, overturn_count
    FROM '|| from_schema ||'.diary_opening_hours';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.appointment_assn (diary_id, opening_hours_id, appointment_id)
    SELECT (-'|| offset_value ||'-diary_id), (-'|| offset_value ||'-opening_hours_id), (-'|| offset_value ||'-appointment_id)
    FROM '|| from_schema ||'.appointment_assn';
    EXECUTE(query);

    query := 'INSERT INTO ' || to_schema || '.historic_appointment_state (appointment_id, appointment_state_id, reason, changed_state_date,
                                               created_by, created_on, updated_by, updated_on, deleted, deleted_by,
                                               deleted_on)
    SELECT (-'|| offset_value ||'-appointment_id), appointment_state_id, reason, changed_state_date, (-'|| offset_value ||'-created_by),
           created_on, (-'|| offset_value ||'-updated_by), updated_on, deleted, (-'|| offset_value ||'-deleted_by), deleted_on
    FROM '|| from_schema ||'.historic_appointment_state;';
    EXECUTE(query);
END;
$$;

CALL quilmes.migrate(1000, 'quilmes', 'public', 'QUILMES');