import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Data, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { switchMap } from 'rxjs/operators';

export const isActive = (data: Data, featureFlagService: FeatureFlagService): Observable<boolean | undefined> =>
	!data?.featureFlag ? of(true) : featureFlagService.isActive(data.featureFlag);

@Injectable({providedIn: 'root'})
export class FeatureFlagGuard implements CanActivate {

	constructor(
		private router: Router,
		private featureFlagService: FeatureFlagService,
	) { }

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
		return isActive(route.data, this.featureFlagService)
			.pipe(
				switchMap(isOn => {
					return isOn ? of(true) : of(this.router.createUrlTree(['/']));
				})
			);
	}

}
