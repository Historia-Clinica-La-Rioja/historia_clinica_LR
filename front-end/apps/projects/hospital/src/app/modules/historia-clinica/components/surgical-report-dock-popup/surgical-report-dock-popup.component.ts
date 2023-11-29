import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { DiagnosisDto, DocumentHealthcareProfessionalDto, EProfessionType, HealthcareProfessionalDto, HospitalizationProcedureDto, ProcedureTypeEnum, SurgicalReportDto } from '@api-rest/api-model';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { SurgicalReportService } from '@api-rest/services/surgical-report.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-dock-popup',
	templateUrl: './surgical-report-dock-popup.component.html',
	styleUrls: ['./surgical-report-dock-popup.component.scss'],
})
export class SurgicalReportDockPopupComponent implements OnInit {

	diagnosis: DiagnosisDto[];
	professionals: HealthcareProfessionalDto[];

	form = this.formBuilder.group({
		preoperativeDiagnosis: [],
		surgeryProcedures: [],
		healthcareProfessionals: [],
		startDateTime: [],
		endDateTime: [],
		procedures: [],
		postoperativeDiagnosis: [],
		description: [],
		anesthesia: [],
		cultures: [],
		drainages: [],
		frozenSectionBiopsies: [],
		modificationReason: [],
		prosthesisDescription: []
	});

	PATHOLOGIST = EProfessionType.PATHOLOGIST;
	TRANSFUSIONIST = EProfessionType.TRANSFUSIONIST;

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
		private readonly internmentStateService: InternmentStateService,
		private readonly healthcareProfessionalByInstitutionService: HealthcareProfessionalByInstitutionService,
		private readonly surgicalReportService: SurgicalReportService,
		private formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
	) {
		this.internmentStateService.getDiagnosesGeneralState(this.data.patientInfo.internmentEpisodeId).subscribe(response => {
			if (response)
				this.diagnosis = response;
		});
		this.healthcareProfessionalByInstitutionService.getAllDoctors().subscribe(response => {
			if (response)
				this.professionals = response;
		});
	}

	ngOnInit(): void {
	}

	save(): void {
		const value = this.form.value;
		const surgicalReport: SurgicalReportDto = {
			confirmed: true,
			preoperativeDiagnosis: value.preoperativeDiagnosis,
			surgeryProcedures: value.surgeryProcedures?.map(p => this.mapToHospitalizationProcedure(p)),
			healthcareProfessionals: value.healthcareProfessionals,
			startDateTime: value.startDateTime,
			endDateTime: value.endDateTime,
			procedures: value.procedures?.map(p => this.mapToHospitalizationProcedure(p)),
			postoperativeDiagnosis: value.postoperativeDiagnosis,
			description: value.description,
			cultures: value.cultures?.map(p => this.mapToHospitalizationProcedure(p)),
			frozenSectionBiopsies: value.frozenSectionBiopsies?.map(p => this.mapToHospitalizationProcedure(p)),
			drainages: value.drainages?.map(p => this.mapToHospitalizationProcedure(p)),
			prosthesisDescription: value.prosthesisDescription,
		}
		console.log(surgicalReport);

		this.surgicalReportService.saveSurgicalReport(this.data.patientInfo.internmentEpisodeId, surgicalReport).subscribe(
			saved => {
				this.snackBarService.showSuccess('Parte quirúrgico generado correctamente');
				this.dockPopupRef.close(true)
			},
			error => {
				this.snackBarService.showError(error.text)
			}
		);

	}

	formControlChange(event, formControl: FormControl): void {
		formControl.setValue(event);
	}

	private mapToHospitalizationProcedure(procedure): HospitalizationProcedureDto {
		return {
			snomed: procedure.snomed,
			type: ProcedureTypeEnum.PROCEDURE
		}
	}

	professionalChange(professional: DocumentHealthcareProfessionalDto, type: EProfessionType): void {
		professional.type = type;
		if (!this.form.controls.healthcareProfessionals.value)
			this.form.controls.healthcareProfessionals.setValue([professional])
		else {
			const index = this.form.controls.healthcareProfessionals.value.findIndex(p => p.type === type);
			if (professional && index == -1)
				this.form.controls.healthcareProfessionals.value.push(professional);

			if (professional && index != -1)
				this.form.controls.healthcareProfessionals.value.splice(index, 1, professional);

			if (!professional && index != -1)
				this.form.controls.healthcareProfessionals.value.splice(index, 1);
		}
	}
}
