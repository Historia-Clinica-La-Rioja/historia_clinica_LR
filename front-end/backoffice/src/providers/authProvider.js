
import apiRest from './utils/sgxApiRest';

const rolesTable = {
    1: 'ROOT',
    2: 'ADMINISTRADOR',
    3: 'ESPECIALISTA_MEDICO',
    4: 'PROFESIONAL_DE_SALUD',
    5: 'ADMINISTRATIVO',
    6: 'ENFERMERO',
    7: 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE'
}

const authProvider = {

    login: ({ username, password, raToken }) => {
        return apiRest
            .login(username, password, raToken);
    },
    logout: () => {
        apiRest.logout();
        return Promise.resolve();
    },
    checkAuth: () => {
        return apiRest.permission$
            .then(permissions => {
                if (!permissions.hasAnyAuthority({role: 'ROOT'}, {role: 'ADMINISTRADOR'},
                    {role: 'ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE'})) {
                    return Promise.reject({ redirectTo: '/home' });
                }
            });
    },
    checkError: (error) => {
        const status = error.status;
        if (status === 401) {
            return Promise.reject();
        }
        return Promise.resolve();
    },

    getPermissions: () => {
        return apiRest.permission$;
    },

    getRole: (id) => {
        return rolesTable[id];
    }
};

export default authProvider;
