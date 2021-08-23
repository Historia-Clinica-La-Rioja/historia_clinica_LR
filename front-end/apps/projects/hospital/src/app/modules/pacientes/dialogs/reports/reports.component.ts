import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from "@angular/material/dialog";
import { ActionDisplays, TableModel } from "@presentation/components/table/table.component";
import { ConsultationsDto } from "@api-rest/api-model";
import {PatientReportsService} from "@api-rest/services/patient-reports.service";

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
		private readonly patientReportsService: PatientReportsService
	) {
	}

	ngOnInit(): void {
		this.patientReportsService.getConsultations(this.data.patientId).subscribe(data => {
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
						checked: (consultation, consultationSelected) => {
							if (consultationSelected) {
								this.consultationsToDownload.push(consultation)
							} else {
								this.consultationsToDownload.forEach((c, index) => {
									if (c.id == consultation.id) this.consultationsToDownload.splice(index, 1);
								});
							}
						}
					}
				},
				{
					columnDef: 'date',
					header: 'Fecha',
					text: (row) => row.date
				},
				{
					columnDef: 'especiality',
					header: 'Especialidad',
					text: (row) => row.especiality
				},
				{
					columnDef: 'professional',
					header: 'Profesional',
					text: (row) => row.professional
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
