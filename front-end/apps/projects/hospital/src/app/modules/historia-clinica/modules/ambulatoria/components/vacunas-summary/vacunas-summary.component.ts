import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { VACUNAS } from '../../../../constants/summaries';
import { TableModel } from '@presentation/components/table/table.component';
import { ImmunizationDto, EvolutionNoteDto } from '@api-rest/api-model';
import { ERole } from '@api-rest/api-model';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
import { MatDialog } from '@angular/material/dialog';
import { AddInmunizationComponent, Immunization } from '../../../../dialogs/add-inmunization/add-inmunization.component';
import { EvolutionNoteService } from '@api-rest/services/evolution-note.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { anyMatch } from "@core/utils/array.utils";
import { PermissionsService } from "@core/services/permissions.service";
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";

@Component({
	selector: 'app-vacunas-summary',
	templateUrl: './vacunas-summary.component.html',
	styleUrls: ['./vacunas-summary.component.scss']
})
export class VacunasSummaryComponent implements OnChanges {

	public readonly vacunasSummary = VACUNAS;
	@Input() internmentEpisodeId: number;
	@Input() immunizations: ImmunizationDto[];
	@Input() editable = false;

	isNursingEvolutionNote: boolean;
	tableModel: TableModel<ImmunizationDto>;

	constructor(
		public dialog: MatDialog,
		private readonly internmentStateService: InternmentStateService,
		private readonly evolutionNoteService: EvolutionNoteService,
		private readonly snackBarService: SnackBarService,
		private readonly permissionsService: PermissionsService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService
	) {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.isNursingEvolutionNote = !anyMatch<ERole>(userRoles, [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD]) && anyMatch<ERole>(userRoles, [ERole.ENFERMERO]);
		})
	}

	ngOnChanges(changes: SimpleChanges) {
		this.tableModel = this.buildTable(this.immunizations);
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddInmunizationComponent, {
			disableClose: true,
			width: '35%',
		});

		dialogRef.afterClosed().subscribe(submitted => {

			if (submitted) {
				this.evolutionNoteService.createDocument(this.buildEvolutionNote(submitted), this.internmentEpisodeId).subscribe(_ => {
					this.internmentStateService.getImmunizations(this.internmentEpisodeId).subscribe(data => this.tableModel = this.buildTable(data));
					this.snackBarService.showSuccess('internaciones.internacion-paciente.vacunas-summary.save.SUCCESS');
					this.internmentSummaryFacadeService.setFieldsToUpdate({evolutionClinical: true});
				}, _ => {
					this.snackBarService.showError('internaciones.internacion-paciente.vacunas-summary.save.ERROR');
				});
			}
		});
	}

	private buildEvolutionNote(immunization: Immunization): EvolutionNoteDto {
		const inmunizationDto: ImmunizationDto = {
			administrationDate: immunization.administrationDate,
			note: null,
			snomed: immunization.snomed
		};

		return {
			confirmed: true,
			immunizations: [inmunizationDto],
			isNursingEvolutionNote: this.isNursingEvolutionNote
		};

	}

	private buildTable(data: ImmunizationDto[]): TableModel<ImmunizationDto> {
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
