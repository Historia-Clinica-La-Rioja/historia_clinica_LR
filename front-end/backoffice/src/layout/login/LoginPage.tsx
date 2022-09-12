import React from 'react';
import { Login } from 'react-admin';
import { loginUrl } from '../../providers/utils/webappLink';

import LoginRedirect from '../../libs/sgx/components/login/LoginRedirect';

const MyLoginPage = () => {
	const useAppLogin = !!loginUrl;
	return !useAppLogin ? <Login /> : (
		<Login>
			<LoginRedirect loginUrl={loginUrl} />
		</Login>
	)
};

export default MyLoginPage;
