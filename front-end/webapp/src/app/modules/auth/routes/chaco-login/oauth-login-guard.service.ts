import { Injectable } from '@angular/core';
import { CanActivate, UrlTree, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { AuthService } from '@api-rest/services/auth.service';

@Injectable()
export class OauthLoginGuardService implements CanActivate {
    constructor(
        private readonly authService: AuthService,
    ) {
    }

    canActivate(route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> {
        return this.authService.getOauthConfig().pipe(
            map(oauthConfig => {
                if (oauthConfig.enabled && oauthConfig.loginUrl) {
                    window.location.href = oauthConfig.loginUrl;
                    return false;
                } else { return true }
            }));
    }
}
