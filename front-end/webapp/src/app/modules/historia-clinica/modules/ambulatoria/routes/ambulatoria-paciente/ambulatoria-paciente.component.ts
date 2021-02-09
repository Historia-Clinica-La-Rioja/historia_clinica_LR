import {Component, OnInit} from '@angular/core';
import {PatientBasicData} from '@presentation/components/patient-card/patient-card.component';
import {ActivatedRoute} from '@angular/router';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';
import {PatientService} from '@api-rest/services/patient.service';
import {MapperService} from '@presentation/services/mapper.service';
import {DockPopupService} from '@presentation/services/dock-popup.service';
import {NuevaConsultaDockPopupComponent} from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import {DockPopupRef} from '@presentation/services/dock-popup-ref';
import {AmbulatoriaSummaryFacadeService} from '../../services/ambulatoria-summary-facade.service';
import {HistoricalProblemsFacadeService} from '../../services/historical-problems-facade.service';
import {FeatureFlagService} from '@core/services/feature-flag.service';
import {BasicPatientDto, OrganizationDto, PatientSummaryDto, PersonPhotoDto} from '@api-rest/api-model';
import {InteroperabilityBusService} from '@api-rest/services/interoperability-bus.service';
import {SnackBarService} from '@presentation/services/snack-bar.service';
import { MatTabChangeEvent } from '@angular/material/tabs';
import {
	AppFeature,
} from '@api-rest/api-model';
import { MedicacionesService } from '../../services/medicaciones.service';


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
	public externalInstitutions: OrganizationDto[];
	public patientExternalSummary: PatientSummaryDto;
	public externalInstitutionPlaceholder: string = 'Ninguna';
	public loaded = false;
	public spinner = false;
	private timeOut = 15000;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly dockPopupService: DockPopupService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly interoperabilityBusService: InteroperabilityBusService,
		private readonly snackBarService: SnackBarService,
		private medicacionesService: MedicacionesService,
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
				.subscribe((personPhotoDto: PersonPhotoDto) => {this.personPhoto = personPhotoDto; });
		});

	}

	loadExternalInstitutions(){
		let externalInstitutions = this.interoperabilityBusService.getPatientLocation(this.patientId.toString()).subscribe(location => {
			if (location.length === 0) {
				this.snackBarService.showError('ambulatoria.bus-interoperabilidad.PACIENTE-NO-FEDERADO');
				this.loaded = false;
			} else this.externalInstitutions = location;
		});
		this.showTimeOutMessages(externalInstitutions);
	}

	loadExternalSummary(organization: OrganizationDto){
		this.spinner = true;

		let info = this.interoperabilityBusService.getPatientInfo(this.patientId.toString(), organization.custodian)
			.subscribe(summary => {
				this.patientExternalSummary = summary;
				this.spinner = false;
			});

		this.showTimeOutMessages(info);
	}

	externalInstitutionsClicked(){
		if (!this.loaded){
			this.loaded = true;
			this.loadExternalInstitutions();
			this.externalInstitutionPlaceholder = ' ';
		}
	}

	showTimeOutMessages(subscription){
		setTimeout(() => {
			if (this.spinner) {
				subscription.unsubscribe();
				this.snackBarService.showError('ambulatoria.bus-interoperabilidad.TIMEOUT-MESSAGE');
				this.spinner = false;
			}
			}, this.timeOut);
	}

	openNuevaConsulta(): void {
		if (!this.dialogRef) {
			this.patientId = Number(this.route.snapshot.paramMap.get('idPaciente'));
			this.dialogRef = this.dockPopupService.open(NuevaConsultaDockPopupComponent, {idPaciente: this.patientId});
			this.dialogRef.afterClosed().subscribe(fieldsToUpdate => {
				delete this.dialogRef;
				this.medicacionesService.updateMedication();
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
