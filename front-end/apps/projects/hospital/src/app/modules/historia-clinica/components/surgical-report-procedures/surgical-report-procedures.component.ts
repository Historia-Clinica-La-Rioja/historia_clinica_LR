import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, UntypedFormBuilder, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { AppFeature, HospitalizationProcedureDto, ProcedureTypeEnum, SurgicalReportDto, TimeDto } from '@api-rest/api-model';
import { dateTimeDtoToDate, dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { sameDayMonthAndYear } from '@core/utils/date.utils';
import { NewConsultationProcedureFormComponent } from '@historia-clinica/dialogs/new-consultation-procedure-form/new-consultation-procedure-form.component';
import { ProcedimientosService } from '@historia-clinica/services/procedimientos.service';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { TimePickerData } from '@presentation/components/time-picker/time-picker.component';
import { DateFormatPipe } from '@presentation/pipes/date-format.pipe';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Moment } from 'moment';
import { BehaviorSubject } from 'rxjs';
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
	private isEmptySubject = new BehaviorSubject<boolean>(true);
	isEmpty$ = this.isEmptySubject.asObservable();
	procedureService = new ProcedimientosService(this.formBuilder, this.snomedService, this.snackBarService, this.dateFormatPipe);
	searchConceptsLocallyFF = false;
	description: string;
	minDate: Date;
	maxDate: Date;
	endTimeBeforeStartTimeInvalid = false;
	startTimePickerData: TimePickerData = {
		defaultTime: {
			hours: 0,
			minutes: 0
		},
		hideLabel: true,
		isRequired: true
	};
	endTimePickerData: TimePickerData = {
		defaultTime: {
			hours: 0,
			minutes: 0
		},
		hideLabel: true,
		isRequired: true
	}

	dateForm = new FormGroup({
		startDate: new FormControl(null, [Validators.required]),
		startTime: new FormControl({ value: null }, [Validators.required]),
		endDate: new FormControl(null, [Validators.required]),
		endTime: new FormControl({ value: null }, [Validators.required]),
	});

	constructor(
		private readonly snackBarService: SnackBarService,
		private readonly snomedService: SnomedService,
		private readonly formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly featureFlagService: FeatureFlagService,
		private readonly dateFormatPipe: DateFormatPipe,
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFF = isOn;
		})
		this.procedureService.procedimientos$.subscribe(procedures => {
			this.isEmpty(procedures);
			this.changeProcedure(procedures)});
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
			this.startTimePickerData.defaultTime = this.surgicalReport.startDateTime.time;

			this.dateForm.controls.endDate.setValue(endDate);
			this.maxDate = endDate;
			this.endTimePickerData.defaultTime = this.surgicalReport.endDateTime.time;
		}
		this.description = this.surgicalReport.description;
	}

	private validateTime(): void {
		const startTime: TimeDto = this.dateForm.value.startTime;
		const endTime: TimeDto = this.dateForm.value.endTime;

		const startTimeValid = !this.dateForm.get('startTime').hasError('required');
		const endTimeValid = !this.dateForm.get('endTime').hasError('required');
		const endTimeBeforeStartTime = endTime.hours < startTime.hours || (endTime.hours === startTime.hours && endTime.minutes <= startTime.minutes);

		const endDateControl = this.dateForm.value.endTime;
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
		this.dateForm.get('endTime').setErrors(null);
		const startDate = this.dateForm.get('startDate').value;
		const endDate = this.dateForm.get('endDate').value;
		const sameDate = (startDate && endDate) ? sameDayMonthAndYear(startDate, endDate) : false;

		if (sameDate)
			this.validateTime();

		this.validDate.emit(this.dateForm.valid);
	}



	changeStartDate(moment: Moment): void {
		const date = moment?.toDate();
		if (date) {
			this.surgicalReport.startDateTime.date = dateToDateDto(date);
			this.dateForm.controls.startDate.setValue(date);
			this.minDate = date;
		}
		this.validateDate();
	}

	changeEndDate(moment: Moment): void {
		const date = moment?.toDate();
		if (date) {
			this.surgicalReport.endDateTime.date = dateToDateDto(date);
			this.dateForm.controls.endDate.setValue(date);
			this.maxDate = date;
		}
		this.validateDate();
	}

	changeDescription(description): void {
		this.surgicalReport.description = description;
	}

	private isEmpty(procedures) {
		if (procedures.length) {
		  this.isEmptySubject.next(false);
		}
	  }
 
	changeStartTime(startTime: TimeDto) {
		this.dateForm.controls.startTime.setValue(startTime);
		this.surgicalReport.startDateTime.time = startTime;
		this.validateDate();
	}

	changeEndTime(endTime: TimeDto) {
		this.dateForm.controls.endTime.setValue(endTime);
		this.surgicalReport.endDateTime.time = endTime;
		this.validateDate();
	}

	private changeProcedure(procedures): void {
		procedures.forEach(procedure =>
			this.surgicalReport.procedures = pushIfNotExists(this.surgicalReport.procedures, this.mapToHospitalizationProcedure(procedure, ProcedureTypeEnum.PROCEDURE), this.compare));
			this.isEmpty(procedures);
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

	deleteProcedure(index: number): void {
		this.surgicalReport.procedures = removeFrom(this.surgicalReport.surgeryProcedures, index);
		this.procedureService.remove(index);
	}
}
