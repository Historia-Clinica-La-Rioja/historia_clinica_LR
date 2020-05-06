import spanishMessages from '@blackbox-vision/ra-language-spanish';

export default {
    ...spanishMessages,
    sgh: {
        dashboard: {
            title: 'Hospitales',
            subtitle: 'Bienvenido',
        }
    },
    error: {
        "role-level": {
            institution: {
                required: 'El rol requiere una institución'
            }
        }
    },
    resources: {

        beds: {
            name: 'Cama |||| Camas',
            fields: {
                roomId: 'Habitación',
                bedNumber: 'Nro. de cama',
                bedCategoryId: 'Categoría',
            },
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
              roomNumber: 'Nro. habitación'
          },
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
            },
        }, 
        sectors: {
            name: 'Sector |||| Sectores',
            fields: {
                institutionId: 'Institución',
                description: 'Nombre',
            }
        }, 
        clinicalspecialties: {
            name: 'Especialidad |||| Especialidades',
            fields: {
                name: 'Nombre',
                description: 'Descripción',
                sctidCode: 'Código SNOMED',
            }
        }, 
        clinicalspecialtysectors: {
            name: 'Especialidad | Sector',
            fields: {
                description: 'Descripción',
                sectorId: 'Sector',
                clinicalSpecialtyId: 'Especialidad',
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
        healthcareprofessionals: {
            name: 'Profesional |||| Profesionales',
            fields: {
                personId: 'Persona',
                licenseNumber: 'Nro. Licencia',
                isMedicalDoctor: 'Es médico?',
            }
        }, 
        healthcareprofessionalspecialties: {
            name: 'Profesional | Profesión',
            fields: {
                healthcareProfessionalId: 'Profesional',
                professionalSpecialtyId: 'Profesión',
                personId: 'Persona',
                description: 'Descripción',
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
    },
};
