import React from 'react';
import { Login } from 'react-admin';
import { loginUrl } from '../../providers/utils/webappLink';

import LoginRedirect from '../../libs/sgx/components/login/LoginRedirect';

const MyLoginPage = () => {
	const hideRedirect = !loginUrl;
	return hideRedirect ? <Login /> : (
		<Login>
			<LoginRedirect loginUrl={loginUrl} />
		</Login>
	)
};

export default MyLoginPage;
