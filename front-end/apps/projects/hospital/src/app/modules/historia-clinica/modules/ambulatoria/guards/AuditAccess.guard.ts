import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { AppFeature } from '@api-rest/api-model';
import { AuditAccessService } from '@api-rest/services/audit-access.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { Observable, map, of, switchMap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuditAccessGuard implements CanActivate {

  getPositionValueParameterFromUrl = (urlFragmentsArray: string[], parameter: PARAMETERS_CLINIC_HISTORY_URL): number => urlFragmentsArray.indexOf(parameter) + 1

  constructor(
    private readonly auditAccessService: AuditAccessService,
    private readonly featureFlagService: FeatureFlagService,
    private readonly router: Router,
  ){}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {

      return this.featureFlagService.isActive(AppFeature.HABILITAR_AUDITORIA_DE_ACCESO_EN_HC).pipe(
        switchMap(isActive => isActive ? this.checkAccessToHc(state) : of(true))
      )
  }

  private checkAccessToHc(stateUrl: RouterStateSnapshot): Observable<boolean> {
    const accessToHC = true
    const contextUrlParameters = this.getContextFromClinicHistoryUrl(stateUrl.url)
    return this.auditAccessService.verifyConditionToAccessToHC(contextUrlParameters).pipe(
      switchMap(accessGranted => accessGranted ?  of(accessToHC) : this.auditAccessService.getResultAccessDialogAudit(contextUrlParameters)),
      map( result => !result ?  this.auditAccessService.handleRedirectHomeCases(this.router.url) : result)
    )
  }

  private getContextFromClinicHistoryUrl(urlFragment: string) : ContextClinicHistoryUrl {
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

