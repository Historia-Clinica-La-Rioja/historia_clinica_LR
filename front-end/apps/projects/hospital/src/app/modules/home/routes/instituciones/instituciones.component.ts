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
import { Slot, SlotedInfo, WCExtensionsService } from '@extensions/services/wc-extensions.service';

@Component({
	selector: 'app-instituciones',
	templateUrl: './instituciones.component.html',
	styleUrls: ['./instituciones.component.scss']
})
export class InstitucionesComponent {
	institutions: { id: number, name: string }[] = null;
	patientPortalEnabled: boolean;
	webappInstitutionsAccess: boolean;
	backofficeAccess: boolean;
	patientPortalAccess: boolean;
	previousLogin: Date;
	enabledPreviousLogin = false;
	extensions$: Observable<SlotedInfo[]>;

	constructor(
		loggedUserService: LoggedUserService,
		institutionService: InstitutionService,
		private featureFlagService: FeatureFlagService,
		private router: Router,
		private accountService: AccountService,
		private wcExtensionsService: WCExtensionsService,
	) {
		loggedUserService.assignments$.subscribe((allRoles: RoleAssignmentDto[]) => {
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
		this.extensions$ = this.wcExtensionsService.getComponentsFromSlot(Slot.SYSTEM_HOME_PAGE);
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

	ingresar(institutionDto: { id: number }, backoffice): void {
		if (backoffice) {
			this.router.navigate([AppRoutes.Backoffice]);
		} else {
			this.router.navigate([AppRoutes.Institucion, institutionDto.id]);
		}
	}

	ingresarPortalPaciente(): void {
		this.router.navigate([AppRoutes.PortalPaciente]);
	}

	hasAccessToBackoffice(allRoles: RoleAssignmentDto[]) {
		return allRoles
			.filter((ra) => ra.role === ERole.ROOT ||
				ra.role === ERole.ADMINISTRADOR ||
				ra.role === ERole.ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE).length > 0;
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
