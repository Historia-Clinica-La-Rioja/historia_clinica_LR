import {Injectable} from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, Data, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { switchMap, map } from 'rxjs/operators';

export const isAllow = (data: Data, roles$: Observable<ERole[]>): Observable<boolean | undefined> =>
	!data?.allowedRoles ? of(true) : roles$.pipe(
		map((userRoles: ERole[]) => anyMatch(userRoles, data?.allowedRoles)),
	);

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {

	private readonly NO_INSTITUTION = -1;

	constructor(
		private router: Router,
		private permissionsService: PermissionsService
	) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
		// const allowedRoles = route.data.allowedRoles;
		const needsRoot = route.data?.needsRoot;
		const institutionId = needsRoot ? this.NO_INSTITUTION : this.getInstitutionIdFrom(route);

		return isAllow(route.data, this.permissionsService.filterAssignments$(institutionId))
			.pipe(
				switchMap(isOn => {
					return isOn ? of(true) : of(this.router.createUrlTree(['/']));
				})
			);
	}

	private getInstitutionIdFrom(route: ActivatedRouteSnapshot): number {
		let institutionIdSnapshot: ActivatedRouteSnapshot = route;
		while (institutionIdSnapshot.parent.routeConfig.path !== 'institucion') {
			institutionIdSnapshot = institutionIdSnapshot.parent;
		}
		return Number(institutionIdSnapshot.paramMap.get('id'));
	}
}
