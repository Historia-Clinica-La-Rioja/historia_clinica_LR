
import apiRest from './utils/sgxApiRest';

const authProvider = {
    login: ({ username, password }) => {
        return apiRest
            .auth(username, password);
    },
    logout: () => {
        apiRest.logout();
        return Promise.resolve();
    },
    checkAuth: () => {
        return apiRest.permission$
            .then(permissions => {
                if (!permissions.hasAnyAssignment({role: 'ROOT', institutionId: -1}, {role: 'ADMINISTRADOR', institutionId: -1})) {
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
};

export default authProvider;
