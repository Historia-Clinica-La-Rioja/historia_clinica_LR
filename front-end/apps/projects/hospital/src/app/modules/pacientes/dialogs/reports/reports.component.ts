import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {ActionDisplays, TableModel} from "@presentation/components/table/table.component";
import {ConsultationsDto} from "@api-rest/api-model";
import {DateFormat, momentFormat, momentParseDate} from "@core/utils/moment.utils";
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
		@Inject(MAT_DIALOG_DATA) public data: { patientId: number, patientName: string },
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
						check: (row, consultationSelected) => {
							if (consultationSelected) {
								this.consultationsToDownload.push(row)
							} else {
								this.consultationsToDownload.forEach((oc, index) => {
									if (oc.documentId == row.documentId) this.consultationsToDownload.splice(index, 1);
								});
							}
						},
						isChecked: (row) => {
							return this.consultationsToDownload.some(c => c.documentId === row.documentId);
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

	downloadReports() {
		this.consultationsToDownload.forEach(oc => {
			if (this.isCheckedDownloadAnexo && this.isCheckedDownloadFormulario) {
				this.patientReportsService.getFormPdf(oc, this.data.patientName).subscribe();
				this.patientReportsService.getAnnexPdf(oc, this.data.patientName).subscribe();
			} else if (this.isCheckedDownloadFormulario && !this.isCheckedDownloadAnexo) {
				this.patientReportsService.getFormPdf(oc, this.data.patientName).subscribe();
			} else {
				this.patientReportsService.getAnnexPdf(oc, this.data.patientName).subscribe();
			}
		})
	}

}
