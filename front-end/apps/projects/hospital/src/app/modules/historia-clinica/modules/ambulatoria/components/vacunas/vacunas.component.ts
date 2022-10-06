import { Component, Input, OnInit } from '@angular/core';
import { HCEImmunizationDto, ProfessionalInfoDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { VACUNAS } from '@historia-clinica/constants/summaries';
import { AgregarVacunasComponent } from '../../dialogs/agregar-vacunas/agregar-vacunas.component';
import { DetalleVacunaComponent } from '../../dialogs/detalle-vacuna/detalle-vacuna.component';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { Color } from '@presentation/colored-label/colored-label.component';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	private patientId: number;
	public readonly vacunasSummary = VACUNAS;
	public vaccines: HCEImmunizationDto[] = [];
	@Input() hasNewConsultationEnabled: boolean;
	public dialogRef: any;
	Color = Color;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly route: ActivatedRoute,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		public dialog: MatDialog,
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.hceGeneralStateService.getImmunizations(this.patientId).subscribe(dataTable => {
					this.vaccines = dataTable;
				});
			});
	}

	goToAgregarVacunas() {
		const dialogRef = this.dialog.open(AgregarVacunasComponent, {
			disableClose: true,
			width: '40%',
			data: {
				patientId: this.patientId
			},
			autoFocus: false
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.hceGeneralStateService.getImmunizations(this.patientId).subscribe(dataTable => {
					this.vaccines = dataTable;
				});
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(submitted);
			}
		});
	}

	goToDetailsVaccine(vaccine: HCEImmunizationDto) {
		const dialogRef = this.dialog.open(DetalleVacunaComponent, {
			disableClose: false,
			width: '30%',
			data: {
				vaccineTitleName: vaccine.snomed.pt,
				appliedDoses: vaccine.dose?.description,
				applicationDate: vaccine.administrationDate,
				lotNumber: vaccine.lotNumber,
				institutionName: vaccine.institution?.name,
				ProfessionalCompleteName: this.setCompletName(vaccine.doctor),
				vaccineConditinDescription: vaccine.condition?.description,
				vaccinationSchemeDescription: vaccine.scheme?.description,
				vaccineObservations: vaccine.note,
			}
		});
	}

	setCompletName(doctor: ProfessionalInfoDto) {
		if (doctor == null)
			return null
		if (doctor.firstName != null && doctor.lastName == null)
			return doctor.firstName
		if (doctor.firstName == null && doctor.lastName != null)
			return doctor?.lastName
		if (doctor.firstName != null && doctor.lastName != null)
			return doctor.firstName + ' ' + doctor?.lastName
		return null
	}
}
