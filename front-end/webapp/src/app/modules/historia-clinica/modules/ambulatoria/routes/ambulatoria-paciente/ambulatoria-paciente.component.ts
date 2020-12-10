import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';
import { ActivatedRoute, Router } from '@angular/router';
import { BasicPatientDto, PersonPhotoDto } from '@api-rest/api-model';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { PatientService } from '@api-rest/services/patient.service';
import { MapperService } from '@presentation/services/mapper.service';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { NuevaConsultaDockPopupComponent } from '../../dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { AmbulatoriaSummaryFacadeService } from '../../services/ambulatoria-summary-facade.service';
import { HistoricalProblemsFacadeService } from '../../services/historical-problems-facade.service';
import { MatDialog } from '@angular/material/dialog';
import { NuevaPreinscripcionComponent } from '../../dialogs/ordenes-preinscripciones/nueva-preinscripcion/nueva-preinscripcion.component';
import { ConfirmarPreinscripcionComponent } from '../../dialogs/ordenes-preinscripciones/confirmar-preinscripcion/confirmar-preinscripcion.component';

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss'],
	providers: [ HistoricalProblemsFacadeService, AmbulatoriaSummaryFacadeService ]

})
export class AmbulatoriaPacienteComponent implements OnInit {

	dialogRef: DockPopupRef;
	patient$: Observable<PatientBasicData>;
	public personPhoto: PersonPhotoDto;
	public hasNewConsultationEnabled$: Observable<boolean>;

	private patientId: number;

	constructor(
		private readonly route: ActivatedRoute,
		private readonly appointmentsService: AppointmentsService,
		private readonly patientService: PatientService,
		private readonly mapperService: MapperService,
		private readonly router: Router,
		private readonly dockPopupService: DockPopupService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly dialog: MatDialog,
	) {}

	ngOnInit(): void {
		this.route.paramMap.subscribe((params) => {
			this.patientId = Number(params.get('idPaciente'));
			this.patient$ = this.patientService.getPatientBasicData<BasicPatientDto>(this.patientId).pipe(
				map(patient => this.mapperService.toPatientBasicData(patient))
			);

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

	openDialog(): void {
		const newPrescriptionDialog = this.dialog.open(NuevaPreinscripcionComponent,
			{
				data: {
					patientId: this.patientId,
					titleLabel: 'Nueva medicación',
					addLabel: 'Agregar Medicación',
					hasMedicalCoverage: false,
					prescriptionItemList: undefined,
					childData: {
						titleLabel: 'Agregar medicación',
						searchSnomedLabel: 'ambulatoria.paciente.ordenes_prescripciones.add_prescription_item_dialog.MEDICATION',
						showDosage: true,
					}
				}
			});

		newPrescriptionDialog.afterClosed().subscribe(data => {
			if (data) {
				const confirmPrescriptionDialog = this.dialog.open(ConfirmarPreinscripcionComponent,
					{
						disableClose: true,
						data: {
							titleLabel: 'Su medicación fue agregada correctamente',
						}
					});
			}

		})
	}
}
