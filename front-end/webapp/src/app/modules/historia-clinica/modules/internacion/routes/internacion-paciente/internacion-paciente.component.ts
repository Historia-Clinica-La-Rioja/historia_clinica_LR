import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';

import { FeatureFlagService } from "@core/services/feature-flag.service";
import { PermissionsService } from '@core/services/permissions.service';

import { InternmentEpisodeSummary } from '@presentation/components/internment-episode-summary/internment-episode-summary.component';
import { MapperService } from '@presentation/services/mapper.service';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';

import { PatientService } from '@api-rest/services/patient.service';
import {
	BasicPatientDto,
	InternmentSummaryDto,
	AnamnesisSummaryDto,
	EpicrisisSummaryDto,
	EvaluationNoteSummaryDto,
	PatientDischargeDto,

} from '@api-rest/api-model';

import {
	AppFeature,
} from '@api-rest/api-model';

import { InternacionService } from '@api-rest/services/internacion.service';
import { InternmentEpisodeService } from '@api-rest/services/internment-episode.service';

import { INTERNACION } from '../../../../constants/summaries';
import { ROLES_FOR_EDIT_DIAGNOSIS } from '../../constants/permissions';

@Component({
	selector: 'app-internacion-paciente',
	templateUrl: './internacion-paciente.component.html',
	styleUrls: ['./internacion-paciente.component.scss']
})
export class InternacionPacienteComponent implements OnInit {

	public patient$: Observable<PatientBasicData>;

	public internacionSummary = INTERNACION;
	public anamnesisDoc: AnamnesisSummaryDto;
	public epicrisisDoc: EpicrisisSummaryDto;
	public lastEvolutionNoteDoc: EvaluationNoteSummaryDto;
	public internmentEpisodeId: number;
	public internmentEpisodeSummary$: Observable<InternmentEpisodeSummary>;
	public showDischarge: boolean;
	public editDiagnosisSummary$: boolean;
	public hasMedicalDischarge: boolean;
	constructor(
		private patientService: PatientService,
		private internmentService: InternacionService,
		private mapperService: MapperService,
		private route: ActivatedRoute,
		private router: Router,
		private featureFlagService: FeatureFlagService,
		private readonly permissionService: PermissionsService,
		private internmentEpisodeService: InternmentEpisodeService
	) { }

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				let patientId = Number(params.get('idPaciente'));
				this.internmentEpisodeId = Number(params.get('idInternacion'));

				this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(patientId).pipe(
					map(patient => this.mapperService.toPatientBasicData(patient))
				);

				this.internmentEpisodeSummary$ = this.internmentService.getInternmentEpisodeSummary(this.internmentEpisodeId).pipe(
					tap((internmentEpisode: InternmentSummaryDto) => {
						this.anamnesisDoc = internmentEpisode.documents?.anamnesis;
						this.epicrisisDoc = internmentEpisode.documents?.epicrisis;
						this.lastEvolutionNoteDoc = internmentEpisode.documents?.lastEvaluationNote;

						//La alta administrativa está disponible cuando existe el alta medica
						//o el flag de alta sin epicrisis está activa
						this.featureFlagService.isActive(AppFeature.HABILITAR_ALTA_SIN_EPICRISIS).subscribe(isOn => {
							this.showDischarge = isOn || (this.hasMedicalDischarge === true);
						});

					}),
					map((internmentEpisode: InternmentSummaryDto) => this.mapperService.toInternmentEpisodeSummary(internmentEpisode))
				);

				this.internmentEpisodeService.getPatientDischarge(this.internmentEpisodeId)
					.subscribe( (patientDischarge: PatientDischargeDto) => {
						this.hasMedicalDischarge = patientDischarge.dischargeTypeId !== 0;
					})
			}
		);
		this.permissionService.hasRole$(ROLES_FOR_EDIT_DIAGNOSIS).subscribe(
			hasRole => this.editDiagnosisSummary$ = !hasRole
		);

	}

	goToAnamnesis(): void {
		if (this.anamnesisDoc?.id)
			this.router.navigate([`${this.router.url}/anamnesis/${this.anamnesisDoc?.id}`]);
		else
			this.router.navigate([`${this.router.url}/anamnesis`]);
	}

	goToNotaEvolucion(): void {
		this.router.navigate([`${this.router.url}/nota-evolucion`]);
	}

	goToEpicrisis(): void {
		this.router.navigate([`${this.router.url}/epicrisis`]);
	}

	goToAdministrativeDischarge(): void {
		this.router.navigate([`${this.router.url}/alta`]);
	}

	goToMedicalDischarge(): void {
		this.router.navigate([`${this.router.url}/alta-medica`]);
	}

}
