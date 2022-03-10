import { AuthProvider } from 'react-admin';
import SGXPermissions from '../auth/SGXPermissions';
import roleAssignments from './role-assignments'
import { sgxFetchApi, sgxFetchApiWithToken, jsonPayload, withHeader } from './fetch';
import { LoggedUserDto, OauthConfigDto, PermissionsDto, PublicInfoDto } from './model';
import { clearTokens, retrieveToken, saveTokens } from './tokenStorage';

const getPermissions = (): Promise<SGXPermissions> =>
        Promise.all([
            roleAssignments(() => sgxFetchApiWithToken<PermissionsDto>('account/permissions')), 
            sgxFetchApiWithToken<PublicInfoDto>('public/info'),
        ])
            .then(([roleAssignments, { features }]) => {
                return new SGXPermissions(roleAssignments, features)
            })

function logoutFromOAuthServer(oAuthConfig: OauthConfigDto, refreshToken: string) {
    const options = jsonPayload('POST', {
        client_id: oAuthConfig.clientId,
        refresh_token: refreshToken
    });
    const optionsWithAccept = withHeader(options, 'Accept', 'application/json');
    const optionsWithContentType = withHeader(optionsWithAccept, 'Content-Type', 'application/x-www-form-urlencoded');

    fetch(oAuthConfig.logoutUrl, optionsWithContentType).then(r => {
    });
}

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
        let refreshToken = localStorage.getItem("refreshtoken");
        sgxFetchApi<OauthConfigDto>('oauth/config').then(
            oAuthConfig => {
                if (oAuthConfig.enabled && refreshToken) {
                    logoutFromOAuthServer(oAuthConfig, refreshToken);
                }

            }
        );
        clearTokens();
        return Promise.resolve();
    },
    getIdentity:
        () => sgxFetchApiWithToken<LoggedUserDto>('account/info')
            .then(({id, email}) => ({id, fullName: email})),
    getPermissions,
};

export default authProvider;
