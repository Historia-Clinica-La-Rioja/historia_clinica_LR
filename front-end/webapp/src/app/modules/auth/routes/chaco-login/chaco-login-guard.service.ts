import { Injectable } from '@angular/core';
import { CanActivate, UrlTree, RouterStateSnapshot, ActivatedRouteSnapshot } from '@angular/router';
import { PublicService } from '@api-rest/services/public.service';
import { Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { AuthService } from '@api-rest/services/auth.service';

@Injectable()
export class ChacoLoginGuardService implements CanActivate {
    constructor(
        private readonly publicService: PublicService,
        private readonly authService: AuthService) {
    }

    canActivate(route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): Observable<boolean | UrlTree> {
        return this.publicService.getInfo().pipe(
            switchMap(publicInfo => {
                if (publicInfo.flavor === 'chaco') {
                    return this.authService.getRedirectUrl().pipe(map(url => {
                        window.location.href = url;
                        return false;
                    }));
                } else { return of(true) }
            }));
    }

}
