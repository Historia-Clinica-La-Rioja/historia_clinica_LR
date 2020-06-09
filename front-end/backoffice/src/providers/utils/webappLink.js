
import appInfoProvider from '../appInfoProvider'

export const openPasswordReset = (token) => window.open(`/auth/password-reset/${token}`, '_blank');

export const oAuth = (url) => {
    window.open(appInfoProvider.getInfo().oauthConfig.loginUrl, '_self');
}
