import springbootRestProvider from './sgxDataProvider';
import authProvider from './authProvider';

const dataProvider = springbootRestProvider('/backoffice', {});

export {
	dataProvider,
	authProvider,
};