

const rolesTable = {
    1: 'ROOT',
    2: 'ADMINISTRADOR',
    3: 'ESPECIALISTA_MEDICO',
    4: 'PROFESIONAL_DE_SALUD',
    5: 'ADMINISTRATIVO',
    6: 'ENFERMERO',
    7: 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE',
    18: 'PERFIL_EPIDEMIO_MESO'
}

const authProvider = {

    getRole: (id) => {
        return rolesTable[id];
    }
};

export default authProvider;
