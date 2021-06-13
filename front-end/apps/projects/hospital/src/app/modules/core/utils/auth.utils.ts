import { HttpRequest } from '@angular/common/http';
import jwt_decode from 'jwt-decode';

const getTokenExpirationDate = (token: string): Date => {
	const decodedToken = jwt_decode<any>(token);
	return decodedToken?.exp ? new Date(decodedToken.exp * 1000) : undefined;
};

const isTokenExpired = (token: string): boolean => {
	if (!token) { return true; }
	const tokenExpirationDate = getTokenExpirationDate(token);
	if (tokenExpirationDate === null) { return false; }
	const actualDate = new Date().valueOf() / 1000;
	return tokenExpirationDate.valueOf() <= actualDate;
};

export const canRefreshToken = (token: string): boolean => {
	return !isTokenExpired(token);
};

export const addToken = (req: HttpRequest<any>, token: string): HttpRequest<any> => {
	return token ? req.clone({headers: req.headers.append('Authorization', `Bearer ${token}`)}) : req;
};
