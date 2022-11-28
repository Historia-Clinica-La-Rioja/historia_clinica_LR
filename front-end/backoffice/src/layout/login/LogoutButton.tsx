import React from 'react';
import { Logout } from 'react-admin';
import { loginUrl } from '../../providers/utils/webappLink';

const LogoutButton = (props: any) => {
	const useAppLogin = !!loginUrl;
	return !useAppLogin ? <Logout id="logout" {...props}/> : <></>;
};

export default LogoutButton;
