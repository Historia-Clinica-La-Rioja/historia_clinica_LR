import springbootRestProvider from './sgxDataProvider';

const dataProvider = springbootRestProvider('/backoffice', {});

export {
	dataProvider,
};