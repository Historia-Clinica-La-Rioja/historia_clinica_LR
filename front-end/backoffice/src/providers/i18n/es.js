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
    },
};
