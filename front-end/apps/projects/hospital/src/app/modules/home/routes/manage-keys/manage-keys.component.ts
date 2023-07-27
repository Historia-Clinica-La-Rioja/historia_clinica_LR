import { Component } from '@angular/core';
import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { RoleAssignmentDto } from '@api-rest/api-model';
import { Observable, map } from 'rxjs';
import { PUBLIC_API_ROLES } from '../../home-routing.module';

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
