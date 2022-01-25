
const HSI_HOME_PATH = '/home';
const isLocalDevMode = process.env.NODE_ENV !== 'production';
const WEBAPP_ROOT_URL = isLocalDevMode ? 'http://localhost:4200' : '';

const openNewTab = (url) => window.open(url, '_blank');
const navigateUrl = (url) => window.location.href = url;

const openPasswordReset = (token) => openNewTab(`${WEBAPP_ROOT_URL}/auth/password-reset/${token}`);
const goToInstitutionsHome = () => navigateUrl(`${WEBAPP_ROOT_URL}${HSI_HOME_PATH}`);
const loginUrl = isLocalDevMode ? '' : HSI_HOME_PATH;

export { openPasswordReset, goToInstitutionsHome, loginUrl };
