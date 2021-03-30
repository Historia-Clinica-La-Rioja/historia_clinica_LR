import { Route } from '@angular/router';
import { MockComponent } from '@presentation/components/mock/mock.component';

export const mockRouters = (mocks: any[]): Route[] => {
	return mocks.map(routerConf => ({
		path: routerConf.path,
		component: MockComponent,
		pathMatch: 'full',
		data: {
			loads: routerConf.loads,
			actions: routerConf.actions,
		},
	}));
};

