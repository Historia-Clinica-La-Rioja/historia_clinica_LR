

const sectorMessages = {
    name: 'Sector |||| Sectores',
    fields: {
        institutionId: 'Institución',
        description: 'Nombre',
        clinicalspecialtysectors: 'Especialidad | Sector',
        childSectors: 'Sectores Hijos',
        ageGroupId: 'Grupo de edad',
        sectorTypeId: 'Tipo de sector',
        sectorOrganizationId: 'Organización',
        careTypeId: 'Tipo de cuidado',
        hospitalizationTypeId: 'Permanencia',
        sectorId: 'Sector padre'
    },
    createRelated: 'Crear Sector'
}

const messages = {
    app: {
        menu: {
            staff: 'Planta',
            facilities: 'Instalaciones',
            debug: 'Inspeccionar',
            masterData: 'Datos maestros',
            booking: 'Reservas online',
            more: 'Mas',

        },
    },
    bo: {
        login: {
            redirect: {
                message: "Parece que su sesión ha finalizado."
            }
        }
    },
    sgh: {
        dashboard: {
            title: 'Historia de salud integrada',
            subtitle: 'Bienvenido',
        },
        components: {
            customtoolbar: {
                backButton: 'Volver',
            }
        },
    },
    error: {
        "role-level": {
            institution: {
                required: 'El rol requiere una institución'
            }
        },
        "beds": {
            "existsHospitalization": "La cama no puede ser editada porque tiene un episodio de internación asociado",
            "enabled-available": "Solo las camas habilitadas pueden estar disponibles",
            "available-free": "Solo las camas disponibles pueden estar libres"
        },
        "doctorsoffices": {
            "closingBeforeOpening": "La hora de apertura no puede ser posterior a la hora de cierre",
            "matchingIds": "Ese Sector no pertenece a esa institución"
        },
        "healthcareprofessional": {
            "exists": "Esta persona ya está registrada como profesional en el sistema",
        },
        "healthcare-professional": {
            "specialty-profession-exists": "La profesión y especialidad ya se encuentra asignada",
            "only-one-specialty": "Esta especialidad no puede borrarse dado que es la única que posee el profesional",
            "specialty-profession-not-exists": "La especialidad no existe",
            "affected-to-diary-agenda": "Esta especialidad y profesional están afectados a una agenda en curso"
        },
        "role": {
            "requiresprofessional": "Alguno de los roles asignados requiere que el usuario sea un profesional"
        },
        "PROFESSIONAL_REQUIRED": "Alguno de los roles asignados requiere que el usuario sea un profesional",
        "ROOT_LOST_PERMISSION": "El admin no puede perder el rol: ROOT",
        "USER_INVALID_ROLE": "El usuario creado no puede tener el siguiente rol: ROOT",
        "user": {
            "exists": "Esta persona ya tiene un usuario en el sistema",
            "hasrole": "El profesional que quiere eliminar tiene un rol asociado"
        },
        "sector": {
            "mandatoryCareType": "El tipo de cuidado es obligatorio para ese tipo de organización de sector",
            "parentOfItself": "Un sector no puede ser padre de sí mismo"
        },
        forbidden: 'No tiene los permisos necesarios',
        "sector-description-inst-unique": "Ya existe un sector con el mismo nombre en la institución",
        "care-line": {
            "clinical-specialty-exists": "La especialidad clínica ya se encuentra asociada a la línea de cuidado"
        },
        "medical-coverage": {
            "rnos-duplicated": "El Rnos ya se encuentra asociado a otra cobertura médica",
            "cuit-duplicated": "El CUIT ya se encuentra asociado a otra cobertura médica",
            "invalid-cuit": "El CUIT debe ser numérico",
            "plan-exists": "El plan ya se encuentra asociado a la cobertura médica",
        },
    },
    files: {
        cant_download: 'No se pudo descargar el archivo'
    },
    mergeMedicalCoverage: {
        merge_success: 'La cobertura se unificó correctamente',
        cant_merge: 'No se pudo unificar la cobertura médica',
        dialog_title: 'Unificar coberturas médicas',
    },
    resources: {
        wcDefinitionPath: {
            name: 'Extension |||| Extensiones',
            fields: {
                name: 'Nombre',
                path: 'Ruta'
            },
            createRelated: 'Crear wc'
        },
        beds: {
            name: 'Cama |||| Camas',
            fields: {
                roomId: 'Habitación',
                bedNumber: 'Nro. de cama',
                bedCategoryId: 'Categoría',
                enabled: 'Habilitada',
                available: 'Disponible',
                free: 'Libre',
                internmentepisodes: 'Episodios de internación activos para esta cama'
            },
            createRelated: 'Crear Cama'
        },
        rooms: {
            name: 'Habitación |||| Habitaciones',
            fields: {
                description: 'Nombre',
                type: 'Tipo',
                specialityId: 'Especialidad',
                dischargeDate: 'Fecha de alta',
                sectorId: 'Sector',
                clinicalSpecialtySectorId: 'Especialidad | Sector',
                roomNumber: 'Nro. habitación',
                beds: 'Camas'
            },
            createRelated: 'Crear Habitación'
        },
        cities: {
            name: 'Ciudad |||| Ciudades',
            fields: {
                description: 'Nombre',
                departmentId: 'Partido',
            },
        },
        departments: {
            name: 'Partido |||| Partidos',
            fields: {
                description: 'Nombre',
            },
        },
        doctorsoffices: {
            name: 'Consultorio |||| Consultorios',
            fields: {
                description: 'Nombre',
                openingTime: 'Horario de apertura',
                closingTime: 'Horario de cierre',
                sectorId: 'Sector',
                institutionId: 'Institución',
                topic: 'Tópico'
            },
            createRelated: 'Crear Consultorio',
        },
        addresses: {
            name: 'Dirección |||| Direcciones',
            fields: {
                street: 'Calle',
                number: 'Número',
                floor: 'Piso',
                apartment: 'Nro. dpto.',
                quarter: 'Cuarto',
                postcode: 'Código postal',
                cityId: 'Ciudad',
                departmentId: 'Departamento',
                provinceId: 'Provincia',
                latitude: 'Latitud',
                longitude: 'Longitud',
            },
        },
        institutions: {
            name: 'Institución |||| Instituciones',
            fields: {
                name: 'Nombre',
                website: 'Sitio web',
                phone: 'Teléfono',
                sisaCode: 'Código SISA',
                addressId: 'Dirección',
                sectors: 'Sectores',
                dependencyId: 'Dependencia',
                provinceCode: 'Código de provincia'
            },
        },
        "booking-institution": {
            name: 'Instituciones turnos online |||| Instituciones turnos online',
            fields: {
                id: 'Nombre',
            },
        },
        snvs: {
            name: 'SNVS |||| SNVS',
            fields: {
                groupEventId: 'Grupo evento',
                eventId: 'Evento',
                manualClassificationId: 'Clasificación manual',
                patientId: 'Paciente',
                snomedSctid: 'Id Snomed',
                snomedPt: 'Término Snomed',
                status: 'Estado',
                responseCode: 'Código de respuesta',
                professionalId: 'Profesional',
                institutionId: 'Institución',
                sisaRegisteredId: 'Id Sisa',
                lastUpdate: 'Última actualización'
            },
        },
        sectors: sectorMessages,
        rootsectors: sectorMessages,

        clinicalspecialties: {
            name: 'Especialidad |||| Especialidades',
            fields: {
                name: 'Nombre',
                description: 'Descripción',
                sctidCode: 'Código SNOMED',
            },
        },
        clinicalservices: {
            name: 'Servicio |||| Servicios',
            fields: {
                name: 'Nombre',
                description: 'Descripción',
                sctidCode: 'Código SNOMED'
            }
        },
        clinicalspecialtysectors: {
            name: 'Especialidad | Sector',
            fields: {
                description: 'Descripción',
                sectorId: 'Sector',
                clinicalSpecialtyId: 'Especialidad',
                rooms: 'Habitaciones',
                doctorsoffices: 'Consultorios',
            },
            createRelated: 'Crear Especialidad | Sector',
        },
        clinicalservicesectors: {
            name: 'Servicio | Sector',
            fields: {
                description: 'Descripción',
                sectorId: 'Sector',
                clinicalSpecialtyId: 'Servicio',
                rooms: 'Habitaciones',
                doctorsoffices: 'Consultorios',
            },
            createRelated: 'Crear Servicio | Sector',
        },
        clinicalspecialtymandatorymedicalpractices: {
            name: 'Especialidad | PMO',
            fields: {
                practiceRecommendations: 'Recomendaciones',
                clinicalSpecialtyId: 'Especialidad',
                mandatoryMedicalPracticeId: 'PMO',
            }
        },
        professionalspecialties: {
            name: 'Profesión |||| Profesiones',
            fields: {
                description: 'Descripción',
                descriptionProfessionRef: 'Descripción profesión padre',
                sctidCode: 'Código SNOMED',
                educationTypeId: 'Formación',
            }
        },
        professionalprofessions: {
            name: 'Profesión',
            tab: {
                title: 'Profesiones',
                subtitle: 'Profesiones'
            },
            license: 'Nro. de licencia del profesional',
            fields: {
                personId: 'Persona',
                licenseNumber: 'Nro. Licencia',
                isMedicalDoctor: 'Es médico?',
                healthcareprofessionalspecialties: 'Especialidades',
                professionalSpecialtyId: 'Profesión',
                professionalLicenseNumbers: 'Matriculas',
                clinicalSpecialtyId: 'Especialidad'
            },
            buttons: {
                linkSpecialities: 'Asociar especialidades',
                linkProfessionalLicenseNumbers: 'Asociar matricula'
            }
        },
        healthcareprofessionallicensenumbers: {
            name: 'Matricula',
            fields: {
                licenseNumber: 'Nro. de matricula',
                typeId: 'Tipo de matricula',
                description: 'Descripción',
                professionalSpecialtyId: 'Profesión',
                personId: 'Persona',
                clinicalSpecialtyId: 'Especialidad',
            }
        },
        healthcareprofessionalspecialtylicensenumbers: {
            name: 'Matricula',
            fields: {
                licenseNumber: 'Nro. de matricula',
                typeId: 'Tipo de matricula',
                description: 'Descripción',
                professionalSpecialtyId: 'Profesión',
                personId: 'Persona',
                clinicalSpecialtyId: 'Especialidad',
            }
        },
        healthcareprofessionalspecialties: {
            name: 'Profesión | Especialidad',
            fields: {
                healthcareProfessionalId: 'Profesional',
                healthcareProfessionalSpecialtyId: 'Especialidad',
                professionalSpecialtyId: 'Profesión',
                clinicalSpecialtyId: 'Especialidad',
                personId: 'Persona',
                description: 'Descripción',
            },
            buttons: {
                linkProfessionalSpecialtyLicenseNumbers: 'Asociar matricula'
            },
            title: {
                professionalSpecialtyLicenseNumbers: 'Matriculas'
            },
            createRelated: 'Crear Profesión | Especialidad',
        },
        mandatorymedicalpractices: {
            name: 'PMO',
            fields: {
                description: 'Nombre',
                mmpCode: 'Código PMO',
                snomedId: 'Id Snomed'
            }
        },
        users: {
            name: 'Usuario |||| Usuarios',
            fields: {
                username: 'Nombre de usuario',
                personId: 'Persona',
                enable: 'Habilitado',
                lastLogin: 'Último ingreso',
                institutionId: 'Institución',
                roleId: 'Rol',
                email: "E-mail"
            },
            fieldGroups: {
                passwordResets: 'Establecer clave de acceso',
                twoFactorAuthenticationReset: 'Deshabilitar doble factor de autenticación'
            },
            action: {
                reset: 'Visitar link',
                newReset: 'Generar link',
                twoFactorAuthenticationResetSuccess: 'Doble factor de autenticación deshabilitado',
            },
            createRelated: 'Crear Usuario',
            noEmail: "Sin información",
        },
        admin: {
            name: 'Admin |||| Admins',
            fields: {
                username: 'Nombre de usuario',
                enable: 'Habilitado',
                lastLogin: 'Último ingreso',
                institutionId: 'Institución',
                roleId: 'Rol',
                email: "E-mail"
            },
            noEmail: "Sin información",
        },
        internmentepisodes: {
            name: 'Episodio de internación |||| Episodios de internación',
            fields: {
                entryDate: 'Fecha de entrada'
            }
        },
        person: {
            name: 'Persona |||| Personas',
            fields: {
                firstName: 'Nombre',
                middleNames: 'Segundo nombre',
                lastName: 'Apellido',
                otherLastNames: 'Segundo apellido',
                genderId: 'Género',
                identificationTypeId: 'Tipo de documento',
                identificationNumber: 'Nº de documento',
                birthDate: 'Fecha de nacimiento'
            },
            tabs: {
                details: 'Datos personales',
                users: 'Usuario'
            },
            buttons: {
                linkProfession: 'Asociar profesión'
            }
        },
        carelines: {
            name: 'Línea de cuidado |||| Líneas de cuidado',
            fields: {
                description: 'Nombre',
                atentionType: 'Tipo de atención',
                consultation: 'Consulta',
                procedure: 'Procedimiento',
            }
        },
        clinicalspecialtycarelines: {
            name: 'Especialidades',
            fields: {
                clinicalSpecialtyId: 'Especialidad',
                careLineId: 'Línea de cuidado',
            },
            addRelated: 'Agregar especialidad',
        },
        "nomivac-immunizationdata": {
            name: 'Historial de vacunas |||| Historial de vacunas',
        },
        "nomivac-immunizationsync": {
            name: 'Nomivac |||| Nomivac',
        },
        "rest-client-measures": {
            name: 'Monitoreo servicio',
            fields: {
                uri: 'Uri',
                host: 'Host',
                path: 'Path',
                method: 'Método',
                time: 'Tiempo de respuesta(seg)',
                responseTimeInMillis: 'Tiempo de respuesta(ms)',
                responseCode: 'Código de respuesta',
                requestDate: 'Fecha de pedido',
            },
            addRelated: 'Agregar especialidad',
        },
        "documentfiles": {
            name: 'Documentos |||| Documentos',
            fields: {
                sourceTypeId: 'Tipo de atención',
                typeId: 'Tipo de documento',
                filename: 'Nombre de archivo',
                createdOn: 'Fecha de creación',
                "creationable.createdOn": 'Fecha de creación'
            },
            downloadFile: 'Descargar pdf'
        },
        "sourcetypes": {
            name: 'Tipos de atención |||| Tipos de atención',
            fields: {
                id: 'Identificador',
                description: 'Descripción',
            }
        },
        "documenttypes": {
            name: 'Tipos de documentos |||| Tipos de documentos',
            fields: {
                id: 'Identificador',
                description: 'Descripción',
            }
        },
        medicalcoverages: {
            name: 'Cobertura médica |||| Coberturas médicas',
            fields: {
                name: 'Nombre',
                type: 'Tipo',
                plan: 'Plan',
                rnos: 'Rnos',
                acronym: 'Acrónimo',
                plans: 'Planes',
                enabled: 'Habilitada',
                merge: 'Coberturas a unificar'
            },
            createRelated: 'Crear Cobertura médica'

        },
        medicalcoverageplans: {
            name: 'Plan |||| Planes',
            fields: {
                medicalCoverageId: 'Cobertura médica',
                plan: 'Plan',
            },
            addRelated: 'Agregar plan',
        },
        snomedgroups: {
            name: 'Grupo de terminología |||| Grupos de terminología',
            fields: {
                id: 'ID',
                description: 'Descripción',
                ecl: 'ECL',
                customId: 'ID custom',
                groupId: 'Grupo padre',
                institutionId: 'Institución',
                userId: 'Usuario',
                template: 'Plantilla',
                lastUpdate: 'Última actualización',
                snomedConcepts: "Conceptos de Snomed",
                groupType: "Tipo de grupo",
            },
            createRelated: 'Agregar concepto Snomed',
            noInfo: 'Sin información',
        },
        snomedrelatedgroups: {
            name: 'Snomed | Grupo de terminología',
            fields: {
                orden: 'Orden',
                lastUpdate: 'Última actualización',
                groupId: 'Grupo de terminología',
                snomedId: 'Concepto Snomed'
            },
        },
        snomedgroupconcepts: {
            name: 'Snomed | Grupo de terminología',
            fields: {
                orden: 'Orden',
                lastUpdate: 'Última actualización',
                groupId: 'Grupo de terminología',
                snomedId: 'Concepto Snomed',
                conceptSctid: 'Snomed CT ID',
                conceptPt: 'Término'
            },
        },
        snomedconcepts: {
            name: 'Concepto de Snomed |||| Conceptos de Snomed',
            fields: {
                id: 'ID',
                sctid: 'Snomed CT ID',
                pt: 'Preferred Term'
            },
        },
        "properties": {
            name: 'Propiedades del sistema |||| Propiedades del sistema',
            fields: {
                property: 'Propiedad',
                value: 'Valor',
                origin: 'Fuente de datos',
                nodeId: 'Nodo',
                updatedOn: 'Última actualización',
                description: 'Descripción',
            }
        },
        "healthinsurances": {
            name: 'Obras sociales |||| Obras sociales',
            fields: {
                name: 'Nombre',
                acronym: 'Acrónimo',
                id: 'Obra social',
                healthinsurances: 'Obra social',
            }
        },
        "healthcareprofessionalhealthinsurances": {
            name: 'Profesional | Cobertura médica |||| Profesional | Cobertura médica',
            fields: {
                acronym: 'Acrónimo',
                medicalCoverageId: 'Cobertura médica',
                healthcareProfessionalId: 'Profesional',
                licenseNumber: 'Nro. Licencia de profesional',
                person: 'Profesional',
                personId: 'Profesional',
                medicalcoverages: 'Cobertura médica',
            }
        },
        "healthinsurancepractices": {
            name: 'Obra social | PMO |||| Obra social | PMO',
            fields: {
                healthInsuranceId: 'Obra social',
                clinicalSpecialtyId: 'Especialidad',
                mandatoryMedicalPracticeId: 'PMO',
                coverageInformation: 'Información de cobertura'
            }
        },
        "mandatoryprofessionalpracticefreedays": {
            name: 'Días de atención',
            fields: {
                clinicalSpecialtyId: 'Especialidad',
                days: 'Días',
                healthcareProfessionalId: 'Profesional',
                mandatoryMedicalPracticeId: 'PMO',
            }
        },
        holidays: {
            name: 'Feriado |||| Feriados',
            fields: {
                date: 'Fecha',
                description: 'Descripción',
            }
        },
        carelineproblems: {
            name: 'Problemas',
            fields: {
                careLineId: 'Línea de cuidado',
                snomedId: 'Problema',
                conceptSctid: 'sctid',
                pt: 'pt',
            },
            addRelated: 'Agregar problema',
        },
        carelineinstitution: {
            name: 'Adhesión a Línea de cuidado',
            fields: {
                institutionId: 'Institución',
                careLineId: 'Línea de cuidado',
                newspecialty: 'Agregar Especialidad',
                newpractice: 'Agregar Práctica',
                specialtys: 'Especialidades',
                practices: 'Prácticas',
                specialty: 'Especialidad',
                practice: 'Práctica',
            }
        },
        carelineinstitutionspecialty: {
            name: 'Especialidad de adhesión a Línea de cuidado',
            fields: {
                institutionId: 'Institución',
                careLineId: 'Línea de cuidado',
                clinicalSpecialtyId: 'Especialidad',
                newspecialty: 'Nueva Especialidad',
            }
        },
        carelineinstitutionpractice: {
            name: 'Práctica de adhesión a Línea de cuidado',
            fields: {
                institutionId: 'Institución',
                careLineId: 'Línea de cuidado',
                snomedRelatedGroupId: 'Práctica',
                newpractice: 'Nueva Práctica',
            }
        },
        institutionpractices: {
            name: 'Grupo de prácticas |||| Grupos de prácticas',
            fields: {
                id: 'ID',
                description: 'Descripción',
                ecl: 'ECL',
                customId: 'ID custom',
                groupId: 'Grupo padre',
                institutionId: 'Institución',
                userId: 'Usuario',
                template: 'Plantilla',
                lastUpdate: 'Última actualización',
                snomedConcepts: "Prácticas",
                groupType: "Tipo de grupo",
            },
            createRelated: 'Agregar práctica',
            noInfo: 'Sin información',
        },
        institutionpracticesrelatedgroups: {
            name: 'Práctica | Grupo de prácticas',
            fields: {
                orden: 'Orden',
                lastUpdate: 'Última actualización',
                groupId: 'Grupo de prácticas',
                snomedId: 'Práctica'
            },
        },
        snowstormproblems: {
            name: 'Concepto Snowstorm',
            fields: {
                conceptId: 'Id Concepto',
                term: 'Descripción',
            }
        }
    }
};

export default messages;
