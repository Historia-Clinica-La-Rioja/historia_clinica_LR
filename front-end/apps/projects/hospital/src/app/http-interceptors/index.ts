/* "Barrel" of Http Interceptors */
import { HTTP_INTERCEPTORS } from '@angular/common/http';

import { AuthInterceptor } from './auth-interceptor';
import { ErrorInterceptor } from './error-interceptor';
import { PendingRequestsInterceptor } from './pending-requests-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
	{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
	{ provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
	{ provide: HTTP_INTERCEPTORS, useClass: PendingRequestsInterceptor, multi: true, },
];
