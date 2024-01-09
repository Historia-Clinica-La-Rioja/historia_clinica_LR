import { Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AppFeature } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { isValueInStringEnum } from '@core/utils/enum.utils';
import { AuditAccessRegisterComponent } from '@historia-clinica/dialogs/audit-access-register/audit-access-register.component';
import { INTERNMENT_SECTOR, SECTOR_AMBULATORIO, SECTOR_GUARDIA } from '@historia-clinica/modules/guardia/constants/masterdata';
import { Observable, map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuditAccessService {

  private NEW_TAB_ROUTE = '/'
  private  isValueInMyStrEnum = isValueInStringEnum(SCOPE_REQUEST_HC)
  private isNewTabRoute = () => this.router.url === this.NEW_TAB_ROUTE
  private isScopeAllowed = (valueTocheck: string) => this.isValueInMyStrEnum(valueTocheck)
  private getScopeSector = (scope: SCOPE_AUDIT_ACCESS): Number => hashMapScopeSectors.get(scope)

  constructor(
    private readonly dialog: MatDialog,
    private readonly router: Router,
    private readonly featureFlagService: FeatureFlagService,
  ) { }


  verifyConditionToAccessToHC(): Observable<boolean> {
    return this.featureFlagService.isActive(AppFeature.HABILITAR_AUDITORIA_DE_ACCESO_EN_HC)
  }


  getResultAccessDialogAudit(): Observable<boolean> {
    const scope: Number = this.router.url !== this.NEW_TAB_ROUTE && this.isScopeAllowed(this.getScopeFromCurrentUrl())
    ? this.getScopeSector(this.getScopeFromCurrentUrl()) : null

    const dialog = this.dialog.open(AuditAccessRegisterComponent,
      {
        width: '30%',
        height:'510px',
        autoFocus: false,
        disableClose: true,
        data: {
          scopeRequest: scope,
        }
      });
    return dialog.afterClosed().pipe(
      map(resultActionDialog => resultActionDialog),
    );
  }

  redirectHomeIfNewTab(): boolean {
    if (this.isNewTabRoute()) {
      this.router.navigate(['/home'])
      return false
    }
    return false
  }

  private getScopeFromCurrentUrl(): SCOPE_REQUEST_HC {
    const SLASH_CHAR= '/'
    const urlParameters: string[] = this.router.url.split(SLASH_CHAR)
    return urlParameters[urlParameters.length -1] as SCOPE_REQUEST_HC
  }


}

export enum SCOPE_REQUEST_HC {
  INTERNACION = 'internaciones',
  AMBULATORIA = 'ambulatoria',
  GUARDIA = 'guardia',
}

export const hashMapScopeSectors = new Map<SCOPE_REQUEST_HC, number>();
hashMapScopeSectors.set(SCOPE_REQUEST_HC.AMBULATORIA, SECTOR_AMBULATORIO )
hashMapScopeSectors.set(SCOPE_REQUEST_HC.INTERNACION,  INTERNMENT_SECTOR)
hashMapScopeSectors.set(SCOPE_REQUEST_HC.GUARDIA, SECTOR_GUARDIA )

export type SCOPE_AUDIT_ACCESS = SCOPE_REQUEST_HC.INTERNACION | SCOPE_REQUEST_HC.AMBULATORIA | SCOPE_REQUEST_HC.GUARDIA
