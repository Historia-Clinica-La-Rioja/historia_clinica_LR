import springbootRestProvider from './sgxDataProvider';
import authProvider from './authProvider';
import i18nProviderBuilder from './i18nProviderBuilder';

const dataProvider = springbootRestProvider('backoffice', {});

export {
	authProvider,
	dataProvider,
	i18nProviderBuilder,
};