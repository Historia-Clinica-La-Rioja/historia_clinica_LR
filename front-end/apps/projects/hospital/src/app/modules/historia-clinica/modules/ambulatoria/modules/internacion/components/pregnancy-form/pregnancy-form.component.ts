import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ObstetricEventDto } from '@api-rest/api-model';
import { EPregnancyTermination } from '@api-rest/api-model';
import { dateDtoToDate, dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { Subject } from 'rxjs';
import { ObstetricFormService } from '../../services/obstetric-form.service';

@Component({
	selector: 'app-pregnancy-form',
	templateUrl: './pregnancy-form.component.html',
	styleUrls: ['./pregnancy-form.component.scss']
})
export class PregnancyFormComponent implements OnInit{

	VAGINAL = EPregnancyTermination.VAGINAL;
	CESAREAN = EPregnancyTermination.CESAREAN;
	UNDEFINED = EPregnancyTermination.UNDEFINED;
	form: UntypedFormGroup;
	enableDelete = true;
	currentPregnancyEndDate = new Subject<Date>();
	protected currentPregnancyEndDate$ = this.currentPregnancyEndDate.asObservable();
	@Output() event = new EventEmitter<ObstetricEventDto>();
	constructor(
		private formBuilder: UntypedFormBuilder,
		readonly obstetricFormService: ObstetricFormService,

	) {
		this.form = this.formBuilder.group({
			currentPregnancyEndDate: (null),
			gestationalAge: [null, Validators.pattern('^(?:[1-9]|[1-3][0-9]|4[0-2])$')],
			pregnancyTerminationType: (null),
			previousPregnancies: (null),
		});
	}

	ngOnInit() {
		this.obstetricFormService.getValue().subscribe((obstetricEvent: ObstetricEventDto) => {
			if (obstetricEvent?.currentPregnancyEndDate) {
				const date = dateDtoToDate(obstetricEvent.currentPregnancyEndDate);
				this.form.controls.currentPregnancyEndDate.setValue(obstetricEvent.currentPregnancyEndDate)
				this.currentPregnancyEndDate.next(date);
			}
			this.form.controls.gestationalAge.setValue(obstetricEvent?.gestationalAge);

			this.form.controls.pregnancyTerminationType.setValue(obstetricEvent?.pregnancyTerminationType);
			this.form.controls.previousPregnancies.setValue(obstetricEvent?.previousPregnancies);
			this.emmitEvent()
		})
	}

	setIndicationDate($event: Date) {
		let date = null;
		if ($event) {
			date = dateToDateDto($event);
		}
		this.form.controls.currentPregnancyEndDate.setValue(date);
		this.currentPregnancyEndDate.next(dateDtoToDate(date));
		this.emmitEvent();
	}

	emmitEvent() {
		if (this.form.valid) {
			const value = this.form.value;
			this.event.emit(value);
		}
	}

}

