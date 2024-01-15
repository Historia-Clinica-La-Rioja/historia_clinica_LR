import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AuditAccessService } from '@api-rest/services/audit-access.service';
import { Observable, map, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuditAccessGuard implements CanActivate {

  getPositionValueParameterFromUrl = (urlFragmentsArray: string[], parameter: PARAMETERS_CLINIC_HISTORY_URL): number => urlFragmentsArray.indexOf(parameter) + 1

  constructor(
    private readonly auditAccessService: AuditAccessService,
  ){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

      const accessToHC = true
      const contextUrlParameters = this.getContextFromClinicHistoryUrl(state.url)
      return this.auditAccessService.verifyConditionToAccessToHC(contextUrlParameters).pipe(
        switchMap(accessGranted => accessGranted ?  of(accessToHC) : this.auditAccessService.getResultAccessDialogAudit(contextUrlParameters)),
        map( result => !result ?  this.auditAccessService.redirectHomeIfNewTab() : result)
      )
  }

  getContextFromClinicHistoryUrl(urlFragment: string) : ContextClinicHistoryUrl {
    const clinicHistoryUrlFragments = urlFragment.split('/')
    const idPatientPosition = this.getPositionValueParameterFromUrl(clinicHistoryUrlFragments, PARAMETERS_CLINIC_HISTORY_URL.PACIENTE)
    const idInstitutionPosition = this.getPositionValueParameterFromUrl(clinicHistoryUrlFragments, PARAMETERS_CLINIC_HISTORY_URL.INSTITUCION)
    return {
      idPatient: Number(clinicHistoryUrlFragments[idPatientPosition]),
      idInstitution: Number(clinicHistoryUrlFragments[idInstitutionPosition])
    }
  }
}

enum PARAMETERS_CLINIC_HISTORY_URL{
  PACIENTE = 'paciente',
  INSTITUCION = 'institucion',
}


export interface ContextClinicHistoryUrl {
  idPatient: number,
  idInstitution: number
}

