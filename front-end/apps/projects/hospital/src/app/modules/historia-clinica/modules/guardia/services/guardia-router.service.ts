import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ERole } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { PatientType } from '@historia-clinica/constants/summaries';
import { Episode } from '../routes/home/home.component';

@Injectable({
	providedIn: 'root'
})
export class GuardiaRouterService {

	private hasEmergencyCareRelatedRole = false;

	private readonly routePrefix = 'institucion/' + this.contextService.institutionId;
	constructor(
		private router: Router,
		private readonly contextService: ContextService,
		private readonly permissionsService: PermissionsService,
	) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasEmergencyCareRelatedRole = anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD]);
		});
	}


	goToEpisode(episodeId: number, patient: { typeId: number, id: number }) {
		const isEmergencyCareTemporalPatient = patient.typeId === PatientType.EMERGENCY_CARE_TEMPORARY;
		if (this.hasEmergencyCareRelatedRole  /* && episodeId !== EstadosEpisodio.CON_ALTA_ADMINISTRATIVA */) {
			const url = `${this.routePrefix}/ambulatoria/paciente/${patient.id}`;
			this.router.navigateByUrl(url, { state: { toEmergencyCareTab: true } });
			return;
		}
		if (!isEmergencyCareTemporalPatient) {
			const url = `${this.routePrefix}/pacientes/profile/${patient.id}`;
			this.router.navigateByUrl(url);
			return;
		}
		const url = `${this.routePrefix}/pacientes/temporal-guardia/profile/${patient.id}`;
		this.router.navigateByUrl(url);

	}
}
