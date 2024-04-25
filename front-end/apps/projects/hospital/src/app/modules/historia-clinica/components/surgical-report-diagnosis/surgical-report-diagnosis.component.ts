import { Component, Input } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto, HospitalizationProcedureDto, ProblemTypeEnum, ProcedureTypeEnum, SurgicalReportDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';

@Component({
	selector: 'app-surgical-report-diagnosis',
	templateUrl: './surgical-report-diagnosis.component.html',
	styleUrls: ['./surgical-report-diagnosis.component.scss']
})
export class SurgicalReportDiagnosisComponent {

	@Input() diagnosis: DiagnosisDto[];
	@Input() surgicalReport: SurgicalReportDto;

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService,this.dateFormatPipe);
	searchConceptsLocallyFF = false;

	constructor(
		private formBuilder: FormBuilder,
		public dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly featureFlagService: FeatureFlagService,
		private readonly dateFormatPipe: DateFormatPipe
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeSurgeryProcedure(procedures));

	}

	setDiagnosis(diagnosis: DiagnosisDto): void {
		if (diagnosis){
			diagnosis.id = null;
			diagnosis.type = ProblemTypeEnum.PREOPERATIVE_DIAGNOSIS;
			this.surgicalReport.preoperativeDiagnosis = pushIfNotExists(this.surgicalReport.preoperativeDiagnosis, diagnosis, this.compare);
		}

	}


	isEmpty(): boolean {
		return !this.surgicalReport.preoperativeDiagnosis?.length && !this.surgicalReport.surgeryProcedures?.length;
    }

	private compare(first, second): boolean {
		return first.snomed.sctid === second.snomed.sctid;
	}

	deleteDiagnosis(index: number): void {
		this.surgicalReport.preoperativeDiagnosis.splice(index, 1);
	}

	addDiagnosis(): void {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: false
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			this.setDiagnosis(diagnosis);
		});
	}

	addProcedure(): void  {
		this.dialog.open(NewConsultationProcedureFormComponent, {
			data: {
				procedureService: this.procedureService,
				searchConceptsLocallyFF: this.searchConceptsLocallyFF,
				hideDate: true
			},
			autoFocus: false,
			width: '35%',
			disableClose: true,
		});
	}

	deleteProcedure(index: number): void {
		this.surgicalReport.surgeryProcedures = removeFrom(this.surgicalReport.surgeryProcedures, index);
		this.procedureService.remove(index);
	}

	private changeSurgeryProcedure(procedures): void {
		procedures.forEach(procedure =>
			this.surgicalReport.surgeryProcedures = pushIfNotExists(this.surgicalReport.surgeryProcedures, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.SURGICAL_PROCEDURE), this.compare));
	}

	private mapToHospitalizationProcedure(procedure, type: ProcedureTypeEnum): HospitalizationProcedureDto {
		return {
			snomed: procedure.snomed,
			type: type
		}
	}

}
