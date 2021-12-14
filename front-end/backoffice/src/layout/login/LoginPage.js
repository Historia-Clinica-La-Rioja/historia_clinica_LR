import React from 'react';
import { Login } from 'react-admin';
import LoginForm from './loginForm';
import { loginUrl } from '../../providers/utils/webappLink';


import LoginRedirect from '../../libs/sgx/components/login/LoginRedirect';

const MyLoginPage = () => (
		<Login>
			<LoginRedirect loginUrl={loginUrl}>
				<LoginForm />
			</LoginRedirect>
		</Login>
);
export default MyLoginPage;
