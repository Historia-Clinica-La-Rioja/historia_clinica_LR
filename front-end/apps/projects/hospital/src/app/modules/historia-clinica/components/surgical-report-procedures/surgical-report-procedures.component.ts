import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto, HospitalizationProcedureDto, ProblemTypeEnum, ProcedureTypeEnum, SurgicalReportDto } from '@api-rest/api-model';
import { dateTimeDtoToDate, stringToTimeDto, timeDtotoString } from '@api-rest/mapper/date-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { momentToDateDto } from '@core/utils/moment.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
@Component({
	selector: 'app-surgical-report-procedures',
	templateUrl: './surgical-report-procedures.component.html',
	styleUrls: ['./surgical-report-procedures.component.scss']
})

export class SurgicalReportProceduresComponent implements OnInit {

	@Input() surgicalReport: SurgicalReportDto;
	@Output() validDate = new EventEmitter();

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService);
	searchConceptsLocallyFF = false;
	diagnosis: DiagnosisDto[] = [];
	description: string;

	dateForm = new FormGroup({
		startDate: new FormControl(null, [Validators.required]),
		startTime: new FormControl(null, [Validators.required, this.timeFormatValidator]),
		endDate: new FormControl(null, [Validators.required]),
		endTime: new FormControl(null, [Validators.required, this.timeFormatValidator]),
	});

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeProcedure(procedures));

		this.dateForm.valueChanges.subscribe(data => {
			if (data.startTime)
				this.surgicalReport.startDateTime.time = stringToTimeDto(data.startTime);
			if (data.endTime)
				this.surgicalReport.endDateTime.time = stringToTimeDto(data.endTime);
			this.validateDate();
		});
	}

	ngOnInit(): void {
		if (this.surgicalReport.confirmed) {
			this.loadData();
			this.validateDate();
		}
	}

	private loadData(): void {
		if (this.surgicalReport.startDateTime) {
			this.dateForm.controls.startDate.setValue(dateTimeDtoToDate(this.surgicalReport.startDateTime));
			this.dateForm.controls.startTime.setValue(timeDtotoString(this.surgicalReport.startDateTime.time));

		}
		if (this.surgicalReport.endDateTime) {
			this.dateForm.controls.endDate.setValue(dateTimeDtoToDate(this.surgicalReport.endDateTime));
			this.dateForm.controls.endTime.setValue(timeDtotoString(this.surgicalReport.endDateTime.time));
		}
		this.description = this.surgicalReport.description;
	}

	private timeFormatValidator(control: FormControl): { [key: string]: boolean } | null {
		const validTime = /^([01]?[0-9]|2[0-3]):[0-5][0-9]$/.test(control.value);
		if (!validTime) {
			return { 'invalidTime': true };
		}
		return null;
	}

	addProcedure() {
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

	addDiagnosis(): void {
		const dialogRef = this.dialog.open(DiagnosisCreationEditionComponent, {
			width: '450px',
			data: {
				type: 'CREATION',
				isMainDiagnosis: false
			}
		});

		dialogRef.afterClosed().subscribe(diagnosis => {
			if (diagnosis) {
				diagnosis.id = null;
				diagnosis.type = ProblemTypeEnum.POSTOPERATIVE_DIAGNOSIS;
				this.surgicalReport.postoperativeDiagnosis = pushIfNotExists(this.surgicalReport.postoperativeDiagnosis, diagnosis, this.compare);
			}
		});
	}

	deleteDiagnosis(index: number): void {
		this.surgicalReport.postoperativeDiagnosis = removeFrom(this.surgicalReport.postoperativeDiagnosis, index);
	}

	deleteProcedure(index: number): void {
		this.surgicalReport.procedures = removeFrom(this.surgicalReport.surgeryProcedures, index);
	}

	changeStartDateTime(date: Moment): void {
		if (date)
			this.surgicalReport.startDateTime.date = momentToDateDto(date);
		this.validateDate();
	}

	changeEndDateTime(date: Moment): void {
		if (date)
			this.surgicalReport.endDateTime.date = momentToDateDto(date);
		this.validateDate();
	}

	changeDescription(description): void {
		this.surgicalReport.description = description;
	}

	validateDate(): void {
		this.validDate.emit(this.dateForm.valid);
	}

	private changeProcedure(procedures): void {
		procedures.forEach(procedure =>
			this.surgicalReport.procedures = pushIfNotExists(this.surgicalReport.procedures, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.PROCEDURE), this.compare));
	}

	private compare(first, second): boolean {
		return first.snomed.sctid === second.snomed.sctid;
	}

	private mapToHospitalizationProcedure(procedure, type: ProcedureTypeEnum): HospitalizationProcedureDto {
		return {
			snomed: procedure.snomed,
			type: type
		}
	}
}
