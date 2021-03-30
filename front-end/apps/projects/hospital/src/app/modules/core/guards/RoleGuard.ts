import {Injectable} from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { switchMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

	private readonly NO_INSTITUTION = -1;

	constructor(
		private router: Router,
		private permissionsService: PermissionsService
	) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
		const allowedRoles = route.data.allowedRoles;
		const needsRoot = route.data?.needsRoot;
		const institutionId = needsRoot ? this.NO_INSTITUTION : this.getInstitutionIdFrom(route);

		return this.permissionsService.filterAssignments$(institutionId)
			.pipe(switchMap((userRoles: ERole[]) => {
				return (anyMatch(userRoles, allowedRoles)) ?
					of(true) :
					of(this.router.createUrlTree(['/']));
		}));
	}

	private getInstitutionIdFrom(route: ActivatedRouteSnapshot): number {
		let institutionIdSnapshot: ActivatedRouteSnapshot = route;
		while (institutionIdSnapshot.parent.routeConfig.path !== 'institucion') {
			institutionIdSnapshot = institutionIdSnapshot.parent;
		}
		return Number(institutionIdSnapshot.paramMap.get('id'));
	}
}
