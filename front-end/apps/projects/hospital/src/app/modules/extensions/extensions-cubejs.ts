

import { environment } from '@environments/environment';
import { retrieveToken } from '@core/utils/jwt-storage';

export const cubejsOptions = {
	token: retrieveToken,
	options: {
		apiUrl: `${environment.apiBase}/dashboards/cubejs-api`
	}
};
