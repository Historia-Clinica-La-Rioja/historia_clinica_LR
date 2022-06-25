import springbootRestProvider from './sgxDataProvider';
import authProvider from './authProvider';

const dataProvider = springbootRestProvider('backoffice', {});

export {
	authProvider,
	dataProvider,
};