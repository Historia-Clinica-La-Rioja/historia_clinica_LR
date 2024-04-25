import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, DiagnosisDto, HospitalizationProcedureDto, ProblemTypeEnum, ProcedureTypeEnum, SnomedECL, SurgicalReportDto } from '@api-rest/api-model';
import { dateTimeDtoToDate, dateToDateDto, stringToTimeDto, timeDtotoString } from '@api-rest/mapper/date-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { sameDayMonthAndYear } from '@core/utils/date.utils';
import { AnesthesiaFormComponent } from '@historia-clinica/dialogs/anesthesia-form/anesthesia-form.component';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { DiagnosisCreationEditionComponent } from '@historia-clinica/modules/ambulatoria/modules/internacion/dialogs/diagnosis-creation-edition/diagnosis-creation-edition.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
@Component({
	selector: 'app-surgical-report-procedures',
	templateUrl: './surgical-report-procedures.component.html',
	styleUrls: ['./surgical-report-procedures.component.scss']
})

export class SurgicalReportProceduresComponent implements OnInit {

	@Input() surgicalReport: SurgicalReportDto;
	@Input()
	set markAsTouched(value: boolean) {
		if (value) {
			this.dateForm.get('startDate')?.markAsTouched();
			this.dateForm.get('endDate')?.markAsTouched();
		}
	}

	@Output() validDate = new EventEmitter();

	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
	anesthesiaService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
	searchConceptsLocallyFF = false;
	diagnosis: DiagnosisDto[] = [];
	description: string;
	minDate: Date;
	maxDate: Date;
	endTimeBeforeStartTimeInvalid = false;

	dateForm = new FormGroup({
		startDate: new FormControl(null, [Validators.required]),
		startTime: new FormControl({ value: null, disabled: true }, [Validators.required, this.timeFormatValidator]),
		endDate: new FormControl(null, [Validators.required]),
		endTime: new FormControl({ value: null, disabled: true }, [Validators.required, this.timeFormatValidator]),
	});

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly dateFormatPipe: DateFormatPipe
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => this.changeProcedure(procedures));
		this.anesthesiaService.setECL(SnomedECL.ANESTHESIA);
		this.anesthesiaService.procedimientos$.subscribe(anesthesias => this.changeAnesthesia(anesthesias));

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
		if (this.surgicalReport.startDateTime && this.surgicalReport.endDateTime) {
			const startDate = dateTimeDtoToDate(this.surgicalReport.startDateTime);
			const endDate = dateTimeDtoToDate(this.surgicalReport.endDateTime)
			this.dateForm.controls.startDate.setValue(startDate);
			this.minDate = startDate;
			this.dateForm.controls.startTime.setValue(timeDtotoString(this.surgicalReport.startDateTime.time));
			this.dateForm.get('startTime')?.enable();
			this.dateForm.controls.endDate.setValue(endDate);
			this.maxDate = endDate;
			this.dateForm.controls.endTime.setValue(timeDtotoString(this.surgicalReport.endDateTime.time));
			this.dateForm.get('endTime')?.enable();
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

	private validateTime(): void {
		const startTime = this.dateForm.get('startTime').value;
		const endTime = this.dateForm.get('endTime').value;

		const startHours = parseInt(startTime?.split(':')[0], 10);
		const startMinutes = parseInt(startTime?.split(':')[1], 10);
		const endHours = parseInt(endTime?.split(':')[0], 10);
		const endMinutes = parseInt(endTime?.split(':')[1], 10);

		const startTimeValid = !(this.dateForm.get('startTime').hasError('required') || this.dateForm.get('startTime').hasError('invalidTime'));
		const endTimeValid = !(this.dateForm.get('endTime').hasError('required') || this.dateForm.get('endTime').hasError('invalidTime'));
		const endTimeBeforeStartTime = endHours < startHours || (endHours === startHours && endMinutes <= startMinutes);

		const endDateControl = this.dateForm.get('endTime');
		const currentErrors = endDateControl.errors;

		if (startTimeValid && endTimeValid && endTimeBeforeStartTime) {
			endDateControl.setErrors({ ...(currentErrors), 'endTimeBeforeStartTime': true });
		} else {
			if (currentErrors && currentErrors['endTimeBeforeStartTime']) {
				delete currentErrors['endTimeBeforeStartTime'];
				if (Object.keys(currentErrors).length)
					endDateControl.setErrors(currentErrors);
				else
					endDateControl.setErrors(null);
			}
		}
	}

	private validateDate(): void {
		const startDate: Date = this.dateForm.get('startDate').value;
		const endDate = this.dateForm.get('endDate').value;
		const startDateTime = new Date(startDate);
		const endDateTime = new Date(endDate);
		const sameDate = sameDayMonthAndYear(startDateTime, endDateTime);

		if (sameDate)
			this.validateTime();

		this.validDate.emit(this.dateForm.valid);
	}

	private changeProcedure(procedures): void {
		procedures.forEach(procedure =>
			this.surgicalReport.procedures = pushIfNotExists(this.surgicalReport.procedures, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.PROCEDURE), this.compare));
	}

	private changeAnesthesia(anesthesias): void {
		anesthesias.forEach(procedure =>
			this.surgicalReport.anesthesia = pushIfNotExists(this.surgicalReport.anesthesia, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.ANESTHESIA_PROCEDURE), this.compare));
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

	addAnesthesia() {
		this.dialog.open(AnesthesiaFormComponent, {
			data: {
				anesthesiaService: this.anesthesiaService,
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
		this.procedureService.remove(index);
	}

	deleteAnesthesia(index: number): void {
		this.surgicalReport.anesthesia = removeFrom(this.surgicalReport.anesthesia, index);
		this.anesthesiaService.remove(index);
	}

	changeStartDate(moment: Moment): void {
		const date = moment?.toDate();
		if (date) {
			this.surgicalReport.startDateTime.date = dateToDateDto(date);
			this.minDate = date;
			this.dateForm.get('startTime')?.enable();
		}
		else
			this.dateForm.get('startTime')?.disable();
		this.dateForm.get('startTime')?.markAsTouched();
		this.validateDate();
	}

	changeEndDate(moment: Moment): void {
		const date = moment?.toDate();
		if (date) {
			this.surgicalReport.endDateTime.date = dateToDateDto(date);
			this.maxDate = date;
			this.dateForm.get('endTime')?.enable();
		}
		else
			this.dateForm.get('endTime')?.disable();
		this.dateForm.get('endTime')?.markAsTouched();
		this.validateDate();
	}

	changeDescription(description): void {
		this.surgicalReport.description = description;
	}

	isEmpty(): boolean {
		return (
			!this.dateForm.get('startDate').value &&
			!this.dateForm.get('startTime').value &&
			!this.dateForm.get('endDate').value &&
			!this.dateForm.get('endTime').value &&
			!this.surgicalReport.procedures?.length &&
			!this.surgicalReport.description &&
			!this.surgicalReport.postoperativeDiagnosis?.length
		)
    }
}
