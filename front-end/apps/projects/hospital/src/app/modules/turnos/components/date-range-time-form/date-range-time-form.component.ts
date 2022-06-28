import { Component, forwardRef, Input, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { CompleteDiaryDto, DiaryListDto, DiaryOpeningHoursDto } from '@api-rest/api-model';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-date-range-time-form',
	templateUrl: './date-range-time-form.component.html',
	styleUrls: ['./date-range-time-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			useExisting: forwardRef(() => DateRangeTimeFormComponent),
			multi: true,
		}
	],
})
export class DateRangeTimeFormComponent implements ControlValueAccessor, OnDestroy, OnInit {

	@Input() selectedAgenda: CompleteDiaryDto;
	private today: Date;
	private agendaLastDay: Date;

	initDateFilter = (d: Moment | null): boolean => {
		const date = (d?.toDate() || new Date());
		date.setHours(0,0,0,0);
		return this.isPossibleAppointmentDay(date.getDay(), this.selectedAgenda.diaryOpeningHours) && date >= this.today && date <= this.agendaLastDay;
	}

	endDateFilter = (d: Moment | null): boolean => {
		const date = (d?.toDate() || new Date());
		date.setHours(0,0,0,0);
		return this.isPossibleAppointmentDay(date.getDay(), this.selectedAgenda.diaryOpeningHours) && date >= this.form.value.initDate
		&& date <= this.agendaLastDay;
	}

	form = this.formBuilder.group({
		initDate: [null, Validators.required],
		endDate: [null, Validators.required],
		init: ["00:00", Validators.required],
		end: ["00:00", Validators.required],
	});

	onChangeSub: Subscription;

	constructor(private formBuilder: FormBuilder)
	{ }

	ngOnInit(): void {
		this.today = new Date();
		this.agendaLastDay = new Date(this.selectedAgenda.endDate);

		this.today.setHours(0,0,0,0);
		this.agendaLastDay.setHours(0,0,0,0);

		this.form.controls.initDate.setValue(moment(this.today));
		this.form.controls.endDate.setValue(moment(this.today));
		this.form.controls.init.valueChanges.subscribe(nv => {
			if (this.form.value.end < nv) {
				this.form.controls.end.setValue(nv);
			}
		});
	}

	onTouched = () => { };

	writeValue(obj: any): void {
		if (obj)
			this.form.setValue(obj);
	}

	registerOnChange(fn: any): void {
		this.onChangeSub = this.form.valueChanges
			.subscribe(value => {
				const toEmit = this.form.valid ? value : null
				fn(toEmit);
			});
	}

	registerOnTouched(fn: any): void {
		this.onTouched = fn;
	}

	ngOnDestroy(): void {
		this.onChangeSub.unsubscribe();
	}

	updateEndDate() {
		const currentEndDate = this.form.value.endDate;
		const currentInitDate = this.form.value.initDate;
		if (currentInitDate > currentEndDate)
			this.form.controls.endDate.setValue(currentInitDate);
	}

	private isPossibleAppointmentDay(dayOfWeek: number, diaryOpeningHours: DiaryOpeningHoursDto[]): boolean {
		return diaryOpeningHours.map(openingHour => openingHour.openingHours.dayWeekId === dayOfWeek).includes(true);
	}
}
