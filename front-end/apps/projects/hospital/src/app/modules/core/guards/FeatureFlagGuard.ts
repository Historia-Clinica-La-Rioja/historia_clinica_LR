import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable, of } from 'rxjs';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { switchMap } from 'rxjs/operators';

@Injectable({providedIn: 'root'})
export class FeatureFlagGuard implements CanActivate {
	constructor(
		private router: Router,
		private featureFlagService: FeatureFlagService) {
	}

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean | UrlTree> {
		const featureFlag = route.data.featureFlag;

		return this.featureFlagService.isActive(featureFlag)
			.pipe(
				switchMap(isOn => {
					return isOn ? of(true) : of(this.router.createUrlTree(['/']));
				})
			);
	}

}
