import spanishMessages from '@blackbox-vision/ra-language-spanish';

export default {
    ...spanishMessages,
    sgh: {
        dashboard: {
            title: 'Hospitales',
            subtitle: 'Bienvenido',
        }
    },
    resources: {

        beds: {
            name: 'Cama |||| Camas',
            fields: {
                roomId: 'Habitación',
            },
        },
        rooms: {
          name: 'Habitación |||| Habitaciones',
          fields: {
              description: 'Nombre',
              type: 'Tipo',
              specialityId: 'Especialidad',
              dischargeDate: 'Fecha de alta',
              sectorId: 'Sector'
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
                apartment: 'Nro. Dpto.',
                quarter: 'Cuarto',
                postcode: 'Código Postal',
            },
        }, 
        institutions: {
            name: 'Institución |||| Instituciones',
            fields: {
                name: 'Nombre',
                website: 'Sitio Web',
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
    },
};
