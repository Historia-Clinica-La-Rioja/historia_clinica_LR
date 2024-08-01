import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { isValueInStringEnum } from '@core/utils/enum.utils';
import { AuditAccessRegisterComponent } from '@historia-clinica/dialogs/audit-access-register/audit-access-register.component';
import { INTERNMENT_SECTOR, SECTOR_AMBULATORIO, SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';
import { Observable, map } from 'rxjs';
import { ClinicHistoryAccessService } from './clinic-history-access.service';
import { ContextClinicHistoryUrl } from '@historia-clinica/modules/ambulatoria/guards/AuditAccess.guard';

@Injectable({
  providedIn: 'root'
})
export class AuditAccessService {

  private sourceUrl: string = this.router.url
  private NEW_TAB_ROUTE = '/'
  private ADMINISTRATIVE_DISCHARGE = 'alta-administrativa'
  private  isValueInMyStrEnum = isValueInStringEnum(SCOPE_REQUEST_HC)
  private isNewTabRoute = (url:string) => url === this.NEW_TAB_ROUTE
  private isScopeAllowed = (valueTocheck: string) => this.isValueInMyStrEnum(valueTocheck)
  private getScopeSector = (scope: SCOPE_AUDIT_ACCESS): Number => hashMapScopeSectors.get(scope)

  constructor(
    private readonly dialog: MatDialog,
    private readonly router: Router,
    private readonly clinicHistoryAccessService: ClinicHistoryAccessService,
  ) {
  }


  verifyConditionToAccessToHC(context: ContextClinicHistoryUrl): Observable<boolean> {
    return this.clinicHistoryAccessService.isValid(context.idPatient, context.idInstitution)
  }


  getResultAccessDialogAudit(context: ContextClinicHistoryUrl): Observable<boolean> {
    const scope: Number = !this.isNewTabRoute(this.sourceUrl) && this.isScopeAllowed(this.getScopeFromCurrentUrl())
    ? this.getScopeSector(this.getScopeFromCurrentUrl() as SCOPE_AUDIT_ACCESS) : null

    const dialog = this.dialog.open(AuditAccessRegisterComponent,
      {
        width: '30%',
        minHeight:'510px',
        autoFocus: false,
        disableClose: true,
        data: {
          scopeRequest: scope,
          patientId: context.idPatient,
          institutionId: context.idInstitution
        }
      });
    return dialog.afterClosed().pipe(
      map(resultActionDialog => resultActionDialog),
    );
  }

  handleRedirectHomeCases(currentRouteUrl: string): boolean {
    if (this.isNewTabRoute(currentRouteUrl) || currentRouteUrl.split('/').includes(this.ADMINISTRATIVE_DISCHARGE)) {
      this.router.navigate(['/home'])
      return false
    }
    return false
  }

  private getScopeFromCurrentUrl(): string {
    const SLASH_CHAR= '/'
    const urlParameters: string[] = this.sourceUrl.split(SLASH_CHAR)
    return urlParameters[urlParameters.length -1]
  }


}

enum SCOPE_REQUEST_HC {
  INTERNACION = 'internaciones',
  AMBULATORIA = 'ambulatoria',
  GUARDIA = 'guardia',
}

const hashMapScopeSectors = new Map<SCOPE_REQUEST_HC, number>();
hashMapScopeSectors.set(SCOPE_REQUEST_HC.AMBULATORIA, SECTOR_AMBULATORIO )
hashMapScopeSectors.set(SCOPE_REQUEST_HC.INTERNACION,  INTERNMENT_SECTOR)
hashMapScopeSectors.set(SCOPE_REQUEST_HC.GUARDIA, SECTOR_GUARDIA )

type SCOPE_AUDIT_ACCESS = SCOPE_REQUEST_HC.INTERNACION | SCOPE_REQUEST_HC.AMBULATORIA | SCOPE_REQUEST_HC.GUARDIA
