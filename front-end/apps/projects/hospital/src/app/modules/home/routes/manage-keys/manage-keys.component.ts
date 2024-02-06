import { Component } from '@angular/core';
import { Observable, map } from 'rxjs';
import { RoleAssignmentDto } from '@api-rest/api-model';

import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { PUBLIC_API_ROLES } from '../../constants/menu';


export const hasAccessToManageKeys = (allRoles: RoleAssignmentDto[]) => {
	return allRoles.filter((ra) => PUBLIC_API_ROLES.find(r => ra.role === r)).length > 0;
};

@Component({
	selector: 'app-manage-keys',
	templateUrl: './manage-keys.component.html',
	styleUrls: ['./manage-keys.component.scss']
})
export class ManageKeysComponent {
	keyManage$: Observable<any>;

	constructor(
		loggedUserService: LoggedUserService,
	) {
		this.keyManage$ = loggedUserService.assignments$.pipe(
			map((allRoles: RoleAssignmentDto[]) => ({isAllow: hasAccessToManageKeys(allRoles)})),
		);
	 }

}
