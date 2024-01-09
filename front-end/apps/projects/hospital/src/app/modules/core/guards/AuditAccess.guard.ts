import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuditAccessService } from '@api-rest/services/audit-access.service';
import { Observable, map, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuditAccessGuard implements CanActivate {


  constructor(
    private auditAccessService: AuditAccessService,
  ){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

      const accessToHC = true
      return this.auditAccessService.verifyConditionToAccessToHC().pipe(
        switchMap(accessGranted => accessGranted ?  this.auditAccessService.getResultAccessDialogAudit() : of(accessToHC)),
        map( result => !result ?  this.auditAccessService.redirectHomeIfNewTab() : result)
      )
  }

}