import { sgxFetchApi } from '../../libs/sgx/utils/sgxFetch';

const getRecaptchaPublicConfig = () => {
    return sgxFetchApi('/public/recaptcha', { method: 'GET' });
}

export { getRecaptchaPublicConfig };
