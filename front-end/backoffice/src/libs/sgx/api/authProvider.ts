import { AuthProvider } from 'react-admin';
import SGXPermissions from '../auth/SGXPermissions';
import roleAssignments from './role-assignments'
import { sgxFetchApi, sgxFetchApiWithToken, jsonPayload, withHeader } from './fetch';
import { LoggedUserDto, PermissionsDto, PublicInfoDto } from './model';
import { clearTokens, retrieveToken, saveTokens } from './tokenStorage';

const getPermissions = (): Promise<SGXPermissions> =>
        Promise.all([
            roleAssignments(() => sgxFetchApiWithToken<PermissionsDto>('account/permissions')), 
            sgxFetchApiWithToken<PublicInfoDto>('public/info'),
        ])
            .then(([roleAssignments, { features }]) => {
                return new SGXPermissions(roleAssignments, features)
            })

const authProvider: AuthProvider = {
    login: ({ username, password, raToken }) => {
        const options = jsonPayload('POST', { username, password });
        const optionsWithReCaptcha = withHeader(options, 'recaptcha', raToken);
        return sgxFetchApi<{ token: string, refreshToken: string }>('auth', optionsWithReCaptcha)
            .then(({ token, refreshToken }) => {
                saveTokens(token, refreshToken);
            });
    },
    checkError: (error) => {
        const status = error.status;
        if (status === 401 ) {
            clearTokens();
            return Promise.reject();
        }
        console.log('authProvider checkError', error);
        return Promise.resolve()
    },
    checkAuth: () => retrieveToken() ? Promise.resolve() : Promise.reject(),
    logout: () => {
        clearTokens();
        return Promise.resolve();
    },
    getIdentity: 
        () => sgxFetchApiWithToken<LoggedUserDto>('account/info')
            .then(({id, email}) => ({id, fullName: email})),
    getPermissions,
};

export default authProvider;
