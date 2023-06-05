cube(`TablaDiabetes`, {
    sql: `with rc as (
        SELECT srg.snomed_id  
        FROM snomed_related_group srg 
        WHERE srg.group_id = 36),
        gh as (
        select ovs.patient_id, ovs.value, date(ovs.effective_time) as creation_date
        from observation_vital_sign ovs 
        where ovs.id = (select ovs2.id 
                            from observation_vital_sign ovs2 
                            where ovs2.patient_id = ovs.patient_id 
                            and ovs2.snomed_id = 1481
                            order by ovs2.effective_time desc
                            limit 1)),
        g as (
        select ovs.patient_id, ovs.value, date(ovs.effective_time) as creation_date
        from observation_vital_sign ovs 
        where ovs.id = (select ovs2.id 
                            from observation_vital_sign ovs2 
                            where ovs2.patient_id = ovs.patient_id 
                            and ovs2.snomed_id = 1480
                            order by ovs2.effective_time desc
                            limit 1)),
        lof as (
        select p.patient_id, p.performed_date
        from procedures p
        where p.id = (select p2.id
                        from procedures p2 
                        where p2.patient_id = p.patient_id 
                        and p2.snomed_id = 174915 
                        order by p2.performed_date desc 
                        limit 1)),
        la as (
        select hc.patient_id, hc.snomed_id, hc.created_on 
        from health_condition hc
        where hc.id = (select hc2.id
                        from health_condition hc2 
                        where hc2.patient_id = hc.patient_id 
                        and hc2.snomed_id in (select snomed_id from rc)
                        and hc2.snomed_id = hc.snomed_id
                        order by hc2.created_on desc
                        limit 1))  
        select distinct p2.first_name, p2.last_name, it.description || ' ' || p2.identification_number as identification_number, s.pt as diagnosis, to_char(hc.start_date, 'DD/MM/YYYY') as problem_start_date, hcs.display as severity, 
        a.street || ' ' || a."number" as address, c.description as city_name, gh.value || '% (' || to_char(gh.creation_date, 'DD/MM/YYYY') || ')' as glycosilated_hemoglobin_value,
        g.value || ' mg/dl (' || to_char(g.creation_date, 'DD/MM/YYYY') || ')' as glycemia_value, to_char(lof.performed_date, 'DD/MM/YYYY') as last_ocular_fondus_date, to_char(la.created_on, 'DD/MM/YYYY') as last_attention_date, d.institution_id
        from document d 
        join document_health_condition dhc on (dhc.document_id = d.id)
        join health_condition hc on (hc.id = dhc.health_condition_id)
        JOIN snomed s ON (s.id = hc.snomed_id) 
        left join health_condition_severity hcs on (hcs.code = hc.severity)
        join patient p on (p.id = hc.patient_id)
        join person p2 on (p2.id = p.person_id)
        join identification_type it on (p2.identification_type_id = it.id)
        left join person_extended pe on (pe.person_id = p2.id)
        left join address a on (a.id = pe.address_id)
        left join city c on (c.id = a.city_id)
        left join gh on (gh.patient_id = p.id)
        left join g on (g.patient_id = p.id)
        left join lof on (lof.patient_id = p.id)
        join la on (la.patient_id = hc.patient_id and la.snomed_id = hc.snomed_id)
        where hc.id = (SELECT hc2.id  
                    from document d2
                    join document_health_condition dhc2 on (dhc2.document_id = d2.id)
                    join health_condition hc2 on (hc2.id = dhc2.health_condition_id) 
                    WHERE hc.snomed_id IN (
                        SELECT rc.snomed_id  
                        FROM rc)
                    AND hc2.created_on <= current_date - interval '120' day
                    AND hc2.problem_id in ('55607006', '-55607006')
                    AND d2.status_id IN ('445665009', '445667001') 
                    AND d2.type_id IN (4, 9) 
                    AND hc2.start_date IS NOT null
                    and hc2.patient_id = hc.patient_id
                    and hc2.snomed_id = hc.snomed_id
                    ${SECURITY_CONTEXT.roles.unsafeValue()?.filter(role => role.id === 16 || role.id === 8).length === 0 ? '' +  `
                    and d.created_by = ${SECURITY_CONTEXT.userId.unsafeValue()}` : ''}
                    order by hc2.start_date
                    limit 1)`,
    measures: {
        pacientes_diabeticos_sin_atencion: {
            sql: `*`,
            type: `count`,
            title: `Pacientes Diabeticos sin atención por mas de 120 dias`
        }
    },
    dimensions: {
        primer_nombre: {
            sql: `first_name`,
            type: `string`,
            title: `Nombre`
        },
        apellido: {
            sql: `last_name`,
            type: `string`,
            title:`Apellido`
        },
        numero_identificacion: {
            sql: `identification_number`,
            type: `string`,
            title: `Número de identificación`
        },
        diagnostico: {
            sql: `diagnosis`,
            type: `string`,
            title: `Diagnóstico`
        },
        fecha_deteccion_diagnostico: {
            sql: `problem_start_date`,
            type: `string`,
            title: `Fecha detección`
        },
        ultima_fecha_atencion: {
            sql: `last_attention_date`,
            type: `string`,
            title: `Última fecha atención`
        },
        severidad: {
            sql: `severity`,
            type: `string`,
            title: `Severidad`
        },
        direccion: {
            sql: `address`,
            type: `string`,
            title: `Domicilio`
        },
        nombre_ciudad: {
            sql: `city_name`,
            type: `string`,
            title: `Ciudad`
        },
        valor_hemoglobina_glicosada: {
            sql: `glycosilated_hemoglobin_value`,
            type: `string`,
            title: `Última Hemoglobina Glicosada`
        },
        valor_glucemia: {
            sql: `glycemia_value`,
            type: `string`,
            title: `Última Glucemia`
        },
        fecha_fondo_de_ojo: {
            sql: `last_ocular_fondus_date`,
            type: `string`,
            title: `Fecha fondo de ojo`
        },
        institucion: {
            sql: `institution_id`,
            type: `number`,
            title: `Intitución`
        }
    },
    title:` `,
    dataSource: `default`
});