import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from "@angular/router";
import { Observable, of } from 'rxjs';
import { ERole } from '@api-rest/api-model';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { switchMap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
	constructor(
		private router: Router,
		private permissionsService: PermissionsService
	) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
		const allowedRoles = route.data.allowedRoles;
		return this.permissionsService.contextAssignments$().pipe(switchMap((userRoles: ERole[]) => {
				return (anyMatch(userRoles, allowedRoles)) ?
					of(true) :
					of(this.router.createUrlTree(['/']));
		}));
	}
}
