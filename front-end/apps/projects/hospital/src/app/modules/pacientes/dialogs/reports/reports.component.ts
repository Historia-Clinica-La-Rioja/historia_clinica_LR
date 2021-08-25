import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ActionDisplays, TableModel} from "@presentation/components/table/table.component";
import {ConsultationsDto} from "@api-rest/api-model";
import {OutpatientConsultationService} from "@api-rest/services/outpatient-consultation.service";
import {DateFormat, momentFormat, momentParseDate} from "@core/utils/moment.utils";

@Component({
	selector: 'app-reports',
	templateUrl: './reports.component.html',
	styleUrls: ['./reports.component.scss']
})
export class ReportsComponent implements OnInit {

	tableModel: TableModel<ConsultationsDto>;
	consultationsToDownload: ConsultationsDto[] = [];

	isCheckedDownloadAnexo = false;
	isCheckedDownloadFormulario = false;

	constructor(
		private readonly dialogRef: MatDialogRef<ReportsComponent>,
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number },
		private readonly outpatientConsultationService: OutpatientConsultationService
	) {
	}

	ngOnInit(): void {
		this.outpatientConsultationService.getOutpatientConsultations(this.data.patientId).subscribe(data => {
			this.tableModel = this.buildTable(data);
		});
	}

	closeModal() {
		this.dialogRef.close();
	}

	private buildTable(data: ConsultationsDto[]): TableModel<ConsultationsDto> {
		return {
			columns: [
				{
					columnDef: 'action',
					action: {
						displayType: ActionDisplays.CHECKBOX,
						display: ' ',
						check: (row, consultationSelected) => {
							if (consultationSelected) {
								this.consultationsToDownload.push(row)
							} else {
								this.consultationsToDownload.forEach((c, index) => {
									if (c.id == row.id) this.consultationsToDownload.splice(index, 1);
								});
							}
						},
						isChecked: (row) => {
							return this.consultationsToDownload.some(c => c.id === row.id);
						}
					}
				},
				{
					columnDef: 'date',
					header: 'pacientes.reports.table.columns.DATE',
					text: (row) => momentFormat(momentParseDate(String(row.consultationDate)), DateFormat.VIEW_DATE)
				},
				{
					columnDef: 'specialty',
					header: 'pacientes.reports.table.columns.SPECIALTY',
					text: (row) => row.specialty
				},
				{
					columnDef: 'professional',
					header: 'pacientes.reports.table.columns.PROFESSIONAL',
					text: (row) => row.completeProfessionalName
				},
			],
			data,
			enablePagination: true
		};
	}

	enableDownloadAnexo(option: boolean) {
		this.isCheckedDownloadAnexo = option;
	}

	enableDownloadFormulario(option: boolean) {
		this.isCheckedDownloadFormulario = option;
	}

	downloadReports() {}

}
