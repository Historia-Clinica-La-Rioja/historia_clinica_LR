import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { ActivatedRoute } from '@angular/router';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@presentation/services/mapper.service';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PersonPhotoDto, BasicPatientDto } from "@api-rest/api-model";
import {
	AppFeature,
} from '@api-rest/api-model';
import { MatTabChangeEvent } from '@angular/material/tabs';

const RESUMEN_INDEX = 0;

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],
	providers: [ HistoricalProblemsFacadeService, AmbulatoriaSummaryFacadeService ]

})
export class AmbulatoriaPacienteComponent implements OnInit {

	dialogRef: DockPopupRef;
	patient$: Observable<PatientBasicData>;
	patientId: number;
	public personPhoto: PersonPhotoDto;
	public hasNewConsultationEnabled$: Observable<boolean>;
	public showOrders: boolean;


	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly dockPopupService: DockPopupService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly featureFlagService: FeatureFlagService
	) {}

	ngOnInit(): void {
		this.route.paramMap.subscribe((params) => {
			this.patientId = Number(params.get('idPaciente'));
			this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
				map(patient => this.mapperService.toPatientBasicData(patient))
			);

			this.featureFlagService.isActive(AppFeature.HABILITAR_ORDENES_PRESCRIPCIONES).subscribe(isOn => {
				this.showOrders = isOn;
			});

			this.ambulatoriaSummaryFacadeService.setIdPaciente(this.patientId);
			this.hasNewConsultationEnabled$ = this.ambulatoriaSummaryFacadeService.hasNewConsultationEnabled$;
			this.patientService.getPatientPhoto(this.patientId)
					.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto;});


		});
	}

	openNuevaConsulta(): void {
		if (!this.dialogRef) {
			this.patientId = Number(this.route.snapshot.paramMap.get('idPaciente'));
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupComponent, {idPaciente: this.patientId});
			this.dialogRef.afterClosed().subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				if (fieldsToUpdate) {
					this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
				}
			});
		} else {
			if (this.dialogRef.isMinimized()) {
				this.dialogRef.maximize();
			}
		}
	}

	onTabChanged(event: MatTabChangeEvent)
	{
		// TODO Utilizar este método para actualizar componentes asociados a Tabs

		if (event.index == RESUMEN_INDEX){
			this.ambulatoriaSummaryFacadeService.setFieldsToUpdate({
				allergies: false,
				familyHistories: false,
				personalHistories: false,
				vitalSigns: false,
				medications: true,
				anthropometricData: false,
				problems: false
			});
		}
	}
}
