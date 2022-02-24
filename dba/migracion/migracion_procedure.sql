CREATE PROCEDURE migrate(offset_value int)
LANGUAGE plpgsql
AS $$
DECLARE
  temprow RECORD;
BEGIN

    -- Direcciones
    INSERT INTO migrate.address (id, street, number, floor, apartment, quarter, city_id, postcode, latitude, longitude)
    SELECT (-offset_value-a.id), a.street, a.number, a.floor, a.apartment, a.quarter, a.city_id,
            a.postcode, a.latitude, a.longitude
    FROM public.address AS a;

    --------------------------------------------------------------------------------------------------------------------
    -- Personas
    INSERT INTO migrate.person (id, first_name, middle_names, last_name, other_last_names, identification_type_id,
                            identification_number, gender_id, birth_date)
    SELECT (-offset_value-p.id), p.first_name, p.middle_names, p.last_name, p.other_last_names,
            p.identification_type_id, p.identification_number, p.gender_id, p.birth_date
    FROM public.person AS p;


    INSERT INTO migrate.person_extended (person_id, cuil, mothers_last_name, address_id, phone_number, email, religion,
                                     name_self_determination, gender_self_determination, photo_file_path, ethnicity_id,
                                     education_level_id, occupation_id, other_gender_self_determination, phone_prefix)
    SELECT (-offset_value-pe.person_id), pe.cuil, pe.mothers_last_name, (-offset_value-pe.address_id),
            pe.phone_number, pe.email, pe.religion, pe.name_self_determination, pe.gender_self_determination,
            pe.photo_file_path, pe.ethnicity_id, pe.education_level_id, pe.occupation_id,
            pe.other_gender_self_determination, pe.phone_prefix
    FROM person_extended AS pe;

    INSERT INTO migrate.patient (id, person_id, type_id, possible_duplicate, national_id, identity_verification_status_id,
                         comments)
    SELECT (-offset_value-pa.id), (-offset_value-pa.person_id), pa.type_id, pa.possible_duplicate,
            pa.national_id, pa.identity_verification_status_id, pa.comments
    FROM public.patient AS pa;
    --------------------------------------------------------------------------------------------------------------------
    -- Usuarios
    INSERT INTO migrate.users (id, created_on, deleted_on, deleted, updated_on, last_login, enable, username, created_by,
                           updated_by, deleted_by)
    SELECT (-offset_value-u.id), u.created_on, u.deleted_on, u.deleted, u.updated_on, u.last_login, u.enable,
           ('MIGRACION' || u.username), (-offset_value-u.created_by), (-offset_value-u.updated_by), (-offset_value-u.deleted_by)
    FROM public.users AS u
    WHERE u.created_by != -1;

    INSERT INTO migrate.user_password (id, created_on, deleted_on, deleted, updated_on, hash_algorithm, password, salt,
                               created_by, updated_by, deleted_by)
    SELECT (-offset_value-up.id), up.created_on, up.deleted_on, up.deleted, up.updated_on, up.hash_algorithm, up.password,
           up.salt, (-offset_value-up.created_by), (-offset_value-up.updated_by),(-offset_value-up.deleted_by)
    FROM public.users AS u
    JOIN public.user_password AS up ON u.id = up.id
    WHERE u.created_by != -1;

    --------------------------------------------------------------------------------------------------------------------
    -- Roles
    INSERT INTO migrate.user_role (role_id, user_id, created_on, deleted_on, deleted, updated_on, created_by, updated_by,
                           institution_id, deleted_by)
    SELECT ur.role_id, (-offset_value-ur.user_id), ur.created_on, ur.deleted_on, ur.deleted, ur.updated_on,
           (-offset_value-ur.created_by), (-offset_value-ur.updated_by), (-offset_value-ur.institution_id),
           (-offset_value-ur.deleted_by)
    FROM public.user_role AS ur
    JOIN public.users AS u ON u.id = ur.user_id
    WHERE u.created_by != -1;

    --------------------------------------------------------------------------------------------------------------------
    -- Keys

    -- Aca habría que ver porque hay UQ sobre el campo key, y por ahí habría que ajustar y hacer la UQ sobre los dos campos (user_id, key)
    --SELECT INTO migrate.user_key (user_id, "key")
    --VALUES ((-offset_value-temprow.uk_user_id), temprow.key)
    --FROM public.user_key AS uk
    --JOIN public.users AS u ON u.id = uk.user_id
    --WHERE u.created_by != -1

    --------------------------------------------------------------------------------------------------------------------
    -- Institution
    INSERT INTO migrate.institution (id, name, address_id, website, phone_number, email, cuit, sisa_code, timezone,
                             dependency_id, province_code)
    SELECT (-offset_value-i.id), i.name, (-offset_value-i.address_id), i.website, i.phone_number,
        i.email, i.cuit, i.sisa_code, i.timezone, i.dependency_id, i.province_code
    FROM public.institution AS i;

    --------------------------------------------------------------------------------------------------------------------
    -- Sector
    INSERT INTO migrate.sector (id, institution_id, description, sector_type_id, sector_organization_id, age_group_id,
                        care_type_id, hospitalization_type_id, sector_id)
    SELECT (-offset_value-s.id), (-offset_value-s.institution_id), s.description,
            s.sector_type_id, s.sector_organization_id, s.age_group_id, s.care_type_id,
            s.hospitalization_type_id, (-offset_value-s.sector_id)
    FROM public.sector AS s;

    --------------------------------------------------------------------------------------------------------------------
    -- Clinical specialty Sector
    INSERT INTO migrate.clinical_specialty_sector (id, description, clinical_specialty_id, sector_id)
    SELECT (-offset_value-css.id), css.description, css.clinical_specialty_id, (-offset_value-css.sector_id)
    FROM public.clinical_specialty_sector AS css;

    --------------------------------------------------------------------------------------------------------------------
    -- Rooms
    INSERT INTO migrate.room (id, clinical_specialty_sector_id, description, type, discharge_date, room_number, sector_id)
    SELECT (-offset_value-r.id), (-offset_value-r.clinical_specialty_sector_id), r.description, r.type,
            r.discharge_date, r.room_number, (-offset_value-r.sector_id)
    FROM public.room AS r;

    --------------------------------------------------------------------------------------------------------------------
    -- Bed
    INSERT INTO migrate.bed (id, room_id, bed_category_id, bed_number, enabled, available, free)
    SELECT (-offset_value-b.id),(-offset_value-b.room_id), b.bed_category_id, b.bed_number,
            b.enabled, b.available, b.free
    FROM public.bed AS b;

    --------------------------------------------------------------------------------------------------------------------
    -- Profesionales
    INSERT INTO migrate.healthcare_professional (id, license_number, person_id, deleted, deleted_by, deleted_on, created_on,
                                         updated_on, created_by, updated_by)
    SELECT (-offset_value-hp.id), hp.license_number, (-offset_value-hp.person_id), hp.deleted,
            (-offset_value-hp.deleted_by), hp.deleted_on, hp.created_on, hp.updated_on,
            (-offset_value-hp.created_by), (-offset_value-hp.updated_by)
    FROM public.healthcare_professional AS hp;

    --------------------------------------------------------------------------------------------------------------------
    -- Profesionales especialidades
    INSERT INTO migrate.healthcare_professional_specialty (id, healthcare_professional_id, professional_specialty_id,
                                                   clinical_specialty_id, deleted, deleted_by, deleted_on,
                                                   created_on, updated_on, created_by, updated_by)
    SELECT (-offset_value-hps.id), (-offset_value-hps.healthcare_professional_id), professional_specialty_id,
            hps.clinical_specialty_id, hps.deleted, (-offset_value-hps.deleted_by), hps.deleted_on,
            hps.created_on, hps.updated_on, (-offset_value-hps.created_by), (-offset_value-hps.updated_by)
    FROM  public.healthcare_professional_specialty AS hps;
END; $$;