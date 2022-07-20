import { AuthProvider, HttpError } from 'react-admin';
import SGXPermissions from '../auth/SGXPermissions';
import roleAssignments from './role-assignments';
import { sgxFetchApi, sgxFetchApiWithToken, jsonPayload, withHeader } from './fetch';
import { LoggedUserDto, PermissionsDto, PublicInfoDto } from './model';
import { clearUserAuth, saveUserAuth, retrieveUserAuth } from './userAuthStorage';

const getPermissions = (): Promise<SGXPermissions> =>
        Promise.all([
            roleAssignments(() => sgxFetchApiWithToken<PermissionsDto>('account/permissions')), 
            sgxFetchApiWithToken<PublicInfoDto>('public/info'),
        ])
        .then(([roleAssignments, { features }]) => {
                return new SGXPermissions(roleAssignments, features)
        });

const authProvider: AuthProvider = {
    // authentication
    login: ({ username, password, raToken }) => {
        const options = jsonPayload('POST', { username, password });
        const optionsWithReCaptcha = withHeader(options, 'recaptcha', raToken);
        return sgxFetchApi<void>('auth', optionsWithReCaptcha)
            .then(() => {
                saveUserAuth();
            });
    },
    checkError: (error: HttpError) => {
        const status = error.status;
        if (status === 401 ) {
            clearUserAuth();
            return Promise.reject();
        }
        console.log('authProvider checkError', error);
        return Promise.resolve()
    },
    checkAuth: () => retrieveUserAuth() ? Promise.resolve() : Promise.reject(),
    logout: () => {
        clearUserAuth();
        return sgxFetchApi<void>('auth/refresh', { method: 'DELETE'});
    },
    getIdentity:
        () => sgxFetchApiWithToken<LoggedUserDto>('account/info')
            .then(({id, email}) => ({id, fullName: email})),
    // authorization
    getPermissions,
};

export default authProvider;
