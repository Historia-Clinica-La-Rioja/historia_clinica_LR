import { Component, OnInit, Input } from '@angular/core';
import { HCEImmunizationDto, OutpatientImmunizationDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { AplicarVacunaComponent } from '../../dialogs/aplicar-vacuna/aplicar-vacuna.component';
import { AddInmunizationComponent, Immunization } from 'src/app/modules/historia-clinica/dialogs/add-inmunization/add-inmunization.component';
import { TableModel } from '@presentation/components/table/table.component';
import { momentFormat, momentParseDate, DateFormat } from '@core/utils/moment.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { HceImmunizationService } from '@api-rest/services/hce-immunization.service';
import { VACUNAS } from 'src/app/modules/historia-clinica/constants/summaries';
import { AppointmentsService } from '@api-rest/services/appointments.service';

@Component({
	selector: 'app-vacunas',
	templateUrl: './vacunas.component.html',
	styleUrls: ['./vacunas.component.scss']
})
export class VacunasComponent implements OnInit {

	private patientId: number;
	public readonly vacunasSummary = VACUNAS;
	public tableModel: TableModel<HCEImmunizationDto>;
	@Input() hasConfirmedAppointment: boolean;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
		private readonly route: ActivatedRoute,
		private readonly appointmentsService: AppointmentsService,
		public dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly hceImmunizationService: HceImmunizationService
	) {
	}

	ngOnInit(): void {
		this.route.paramMap.subscribe(
			(params) => {
				this.patientId = Number(params.get('idPaciente'));
				this.hceGeneralStateService.getImmunizations(this.patientId).subscribe(dataTable => {
					this.tableModel = this.buildTable(dataTable)
				});
			});
	}

	goToAplicarVacuna() {
		const dialogRef = this.dialog.open(AplicarVacunaComponent, {
			disableClose: true,
			width: '45%',
			data: {
				patientId: this.patientId
			}
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.hceGeneralStateService.getImmunizations(this.patientId).subscribe(dataTable => {
					this.tableModel = this.buildTable(dataTable)
				});
				this.appointmentsService.hasConfirmedAppointment(this.patientId).subscribe(response => {
					this.hasConfirmedAppointment = response;
				});
			}
		});
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddInmunizationComponent, {
			disableClose: true,
			width: '35%',
		});

		dialogRef.afterClosed().subscribe(submitted => {
			if (submitted) {
				this.hceImmunizationService.updateImmunization(this.buildApplyImmunization(submitted), this.patientId).subscribe(_ => {
					this.hceGeneralStateService.getImmunizations(this.patientId).subscribe(dataTable => {
						this.tableModel = this.buildTable(dataTable)
					});
					this.snackBarService.showSuccess('internaciones.internacion-paciente.vacunas-summary.save.SUCCESS');

				}, _ => {
					this.snackBarService.showError('internaciones.internacion-paciente.vacunas-summary.save.ERROR');

				});
			}
		});
	}

	private buildApplyImmunization(immunization: Immunization): OutpatientImmunizationDto {
		return {
			administrationDate: immunization.administrationDate,
			note: null,
			snomed: immunization.snomed
		};
	}

	private buildTable(data: HCEImmunizationDto[]): TableModel<HCEImmunizationDto> {
		return {
			columns: [
				{
					columnDef: 'vacuna',
					header: 'Vacuna',
					text: (row) => row.snomed.pt
				},
				{
					columnDef: 'fecha',
					header: 'Fecha de vacunación',
					text: (row) => row.administrationDate ? momentFormat(momentParseDate(row.administrationDate), DateFormat.VIEW_DATE) : undefined
				}
			],
			data
		};
	}
}
