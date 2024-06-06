import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

import { AppRoutes } from '../../../../app-routing.module';
import { LoggedUserService } from '../../../auth/services/logged-user.service';

import {LoggedUserDto, RoleAssignmentDto} from '@api-rest/api-model';
import { ERole, AppFeature } from '@api-rest/api-model';

import { InstitutionService } from '@api-rest/services/institution.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { AccountService } from '@api-rest/services/account.service';
import { dateTimeDtotoLocalDate } from '@api-rest/mapper/date-dto.mapper';
import { WCExtensionsService } from '@extensions/services/wc-extensions.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { WCParams } from '@extensions/components/ui-external-component/ui-external-component.component';
import { HierarchicalUnitService } from '@historia-clinica/services/hierarchical-unit.service';
import { itemHasAnyRole } from '@core/services/permissions.service';
import { BACKOFFICE_ROLES } from '@core/components/backoffice/backoffice.component';

@Component({
	selector: 'app-instituciones',
	templateUrl: './instituciones.component.html',
	styleUrls: ['./instituciones.component.scss']
})
export class InstitucionesComponent {
	institutions: { id: number, name: string }[] = [];
	patientPortalEnabled: boolean;
	webappInstitutionsAccess: boolean;
	backofficeAccess: boolean;
	patientPortalAccess: boolean;
	previousLogin: Date;
	enabledPreviousLogin = false;
	extensions$: Observable<WCParams[]>;
	userRoles: RoleAssignmentDto[];

	constructor(
		loggedUserService: LoggedUserService,
		institutionService: InstitutionService,
		private featureFlagService: FeatureFlagService,
		private router: Router,
		private accountService: AccountService,
		private wcExtensionsService: WCExtensionsService,
		private readonly snackBarService: SnackBarService,
		private readonly hierarchicalUnitService: HierarchicalUnitService
	) {
		loggedUserService.assignments$.subscribe((allRoles: RoleAssignmentDto[]) => {
			this.userRoles = allRoles;
			const institutionIds = allRoles
				.filter((ra) => ra.institutionId !== -1)
				.map(r => r.institutionId);
			this.webappInstitutionsAccess = this.hasAccessToWebappInstitutions(allRoles);
			this.backofficeAccess = this.hasAccessToBackoffice(allRoles);
			this.patientPortalAccess = this.hasAccessToPatientPortal(allRoles);

			if (institutionIds[0] !== undefined) {
				institutionService.getInstitutions(institutionIds).subscribe(institutions => {
					/*const uniqueIds = uniqueItems(institutionIds);

					const webappAccess = this.hasAccessToWebapp(allRoles);

					const onlyBackoffice = !webappAccess && this.backoffice;
					const hasSingleIdWebapp = !this.backoffice && webappAccess && uniqueIds.length === 1;

					if (hasSingleIdWebapp || onlyBackoffice)
						this.ingresar({id: uniqueIds[0]}, this.backoffice)*/
					this.institutions = institutions;
				});
			}
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_MODULO_PORTAL_PACIENTE).subscribe(isOn => this.patientPortalEnabled = isOn);
		this.extensions$ = this.wcExtensionsService.getSystemHomeComponents();
	}

	ngOnInit(): void {

		this.accountService.getInfo()
			.subscribe((userInfo: LoggedUserDto) => {
					if(userInfo?.previousLogin){
						this.enabledPreviousLogin = true;
						this.previousLogin = dateTimeDtotoLocalDate(userInfo.previousLogin);
					}
				});
	}

	private hasAccessToInstitution(institutionId: number): boolean{
		let hasAccess = true;
		this.featureFlagService.isActive(AppFeature.HABILITAR_PRESCRIPCION_RECETA).subscribe(isOn => {
			if (!isOn){
				this.userRoles.map(userRole => {
					if (userRole.institutionId === institutionId && userRole.role === 'PRESCRIPTOR'){
						hasAccess = false;
					}
				})
			}
		})
		return hasAccess;
	}

	ingresar(institutionDto: { id: number }, backoffice): void {
		if (backoffice) {
			this.router.navigate([AppRoutes.Backoffice]);
		} else {
			if (this.hasAccessToInstitution(institutionDto.id)){
				this.hierarchicalUnitService.resetForm();
				this.router.navigate([AppRoutes.Institucion, institutionDto.id]);
			} else {
				this.snackBarService.showError("No es posible acceder a Receta Digital todavia");
			}
		}
	}

	ingresarPortalPaciente(): void {
		this.router.navigate([AppRoutes.PortalPaciente]);
	}

	hasAccessToBackoffice(allRoles: RoleAssignmentDto[]) {
		return itemHasAnyRole(BACKOFFICE_ROLES, allRoles.map(r => r.role));
	}

	hasAccessToWebappInstitutions(allRoles: RoleAssignmentDto[]) {
		return allRoles
			.filter((ra) => ra.role !== ERole.ROOT &&
				ra.role !== ERole.ADMINISTRADOR).length > 0;
	}

	hasAccessToPatientPortal(allRoles: RoleAssignmentDto[]): boolean {
		return !(allRoles.some(ra => ra.role === ERole.ROOT));
	}
}
