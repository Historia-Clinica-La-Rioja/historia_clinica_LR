
const WEBAPP_ROOT_URL = process.env.NODE_ENV === 'production'? '' : 'http://localhost:4200';

const openNewTab = (url) => window.open(url, '_blank');

export const openPasswordReset = (token) => openNewTab(`${WEBAPP_ROOT_URL}/auth/password-reset/${token}`);

