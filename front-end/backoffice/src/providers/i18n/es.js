import spanishMessages from '@blackbox-vision/ra-language-spanish';

export default {
    ...spanishMessages,
    sgh: {
        dashboard: {
            title: 'Historia de salud integrada',
            subtitle: 'Bienvenido',
        }
    },
    error: {
        "role-level": {
            institution: {
                required: 'El rol requiere una institución'
            }
        },
        "beds": {
            "existsInternmentEpisode": "La cama tiene un episodio de internación y no puede marcarse como libre",
            "enabled-available": "Solo las camas habilitadas pueden estar disponibles",
            "available-free": "Solo las camas disponibles pueden estar libres"
        },
        "doctorsoffices": {
            "closingBeforeOpening": "La hora de apertura no puede ser posterior a la hora de cierre"
        },
        forbidden: 'No tiene los permisos necesarios',
        "sector-description-inst-unique": "Ya existe un sector con el mismo nombre en la institución"
    },
    resources: {
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
        doctorsoffices:{
          name: 'Consultorio |||| Consultorios',
          fields:{
              description: 'Nombre',
              openingTime: 'Horario de apertura',
              closingTime: 'Horario de cierre',
              clinicalSpecialtySectorId: 'Especialidad | Sector',
              institutionId: 'Institución'
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
            },
        }, 
        sectors: {
            name: 'Sector |||| Sectores',
            fields: {
                institutionId: 'Institución',
                description: 'Nombre',
                clinicalspecialtysectors: 'Especialidad | Sector'
            },
            createRelated: 'Crear Sector'
        }, 
        clinicalspecialties: {
            name: 'Especialidad |||| Especialidades',
            fields: {
                name: 'Nombre',
                description: 'Descripción',
                sctidCode: 'Código SNOMED',
            },
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
            createRelated: 'Crear Especialidad | Sector'
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
        healthcareprofessionals: {
            name: 'Profesional |||| Profesionales',
            fields: {
                personId: 'Persona',
                licenseNumber: 'Nro. Licencia',
                isMedicalDoctor: 'Es médico?',
                healthcareprofessionalspecialties: 'Profesional | Profesión'
            }
        }, 
        healthcareprofessionalspecialties: {
            name: 'Profesional | Profesión',
            fields: {
                healthcareProfessionalId: 'Profesional',
                professionalSpecialtyId: 'Profesión',
                personId: 'Persona',
                description: 'Descripción',
            },
            createRelated: 'Crear Profesional | Profesión',
        }, 
        users: {
            name: 'Usuario |||| Usuarios',
            fields: {
                username: 'Nombre de usuario',
                personId: 'Persona',
                enable: 'Habilitado',
                lastLogin: 'Último ingreso',
                institutionId: 'Institución',
                roleId: 'Rol'
            },
            fieldGroups: {
                passwordResets: 'Establecer clave de acceso'
            },
            action: {
                reset: 'Visitar link',
                newReset: 'Generar link',
            }
        },
        internmentepisodes: {
            name: 'Episodio de internación ||| Episodios de internación',
            fields: {
                entryDate: 'Fecha de entrada'
            }
        },
        people: {
            name: 'Persona ||| Personas'
        }
    },
};
