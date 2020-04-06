
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
        return apiRest.hasToken() ? Promise.resolve() : Promise.reject();
    },
    checkError: (error) => {
        const status = error.status;
        if (status === 401) {
            apiRest.auth();
            return Promise.reject();
        }
        return Promise.resolve();
    },
    getPermissions: params => Promise.resolve(),
};

export default authProvider;
