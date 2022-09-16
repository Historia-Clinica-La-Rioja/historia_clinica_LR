import { Component, forwardRef, Input, OnDestroy, OnInit } from '@angular/core';
import { ControlValueAccessor, FormBuilder, NG_VALUE_ACCESSOR, Validators } from '@angular/forms';
import { CompleteDiaryDto, DiaryOpeningHoursDto, TimeDto } from '@api-rest/api-model';
import { stringToTimeDto, timeDtoToDate } from '@api-rest/mapper/date-dto.mapper';
import { MINUTES_IN_HOUR } from '@turnos/constants/appointment';
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
	private agendaFirstDay: Date;
	possibleStartingTime: TimeDto[];
	possibleEndingTime: TimeDto[];

	initDateFilter = (d: Moment | null): boolean => {
		const date = (d?.toDate() || new Date());
		date.setHours(0,0,0,0);
		return this.isPossibleAppointmentDay(date.getDay(), this.selectedAgenda.diaryOpeningHours) && date >= this.today && date <= this.agendaLastDay
		&& date >= this.agendaFirstDay;
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
		init: [null, Validators.required],
		end: [null, Validators.required],
		fullBlock: [false, Validators.nullValidator],
	});

	onChangeSub: Subscription;

	constructor(private formBuilder: FormBuilder)
	{ }

	ngOnInit(): void {
		const minimumTime = this.getMinimumBlockTime();
		const maximumTime = this.getMaximumBlockTime();
		this.possibleStartingTime = this.generateTimeInterval(minimumTime, maximumTime);

		this.today = new Date();

		this.agendaLastDay = this.fromStringToDate(this.selectedAgenda.endDate);
		this.agendaFirstDay = this.fromStringToDate(this.selectedAgenda.startDate);

		this.today.setHours(0,0,0,0);
		this.agendaLastDay.setHours(0,0,0,0);

		this.form.controls.init.setValue(this.possibleStartingTime[0]);
		this.form.controls.end.setValue(this.possibleStartingTime[1]);
		this.form.controls.init.valueChanges.subscribe(nv => {
			if (this.form.value.end < nv) {
				this.form.controls.end.setValue(nv);
			}
		});
		this.filterEndingTime();
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
		if (currentInitDate > currentEndDate) {
			this.form.controls.endDate.setValue(currentInitDate);
		}
	}

	private isPossibleAppointmentDay(dayOfWeek: number, diaryOpeningHours: DiaryOpeningHoursDto[]): boolean {
		return diaryOpeningHours.map(openingHour => openingHour.openingHours.dayWeekId === dayOfWeek).includes(true);
	}

	private getMinimumBlockTime(): TimeDto {
		const mappedTimes = this.selectedAgenda.diaryOpeningHours.map(diaryOpeningHour => stringToTimeDto(diaryOpeningHour.openingHours.from));
		return mappedTimes.reduce(function(prev, curr) {
			return timeDtoToDate(prev) <= timeDtoToDate(curr) ? prev : curr;
		});
	}

	private getMaximumBlockTime(): TimeDto {
		const mappedTimes = this.selectedAgenda.diaryOpeningHours.map(diaryOpeningHour => stringToTimeDto(diaryOpeningHour.openingHours.to));
		return mappedTimes.reduce(function(prev, curr) {
			return timeDtoToDate(prev) >= timeDtoToDate(curr) ? prev : curr;
		});
	}

	private generateTimeInterval(firstTime: TimeDto, lastTime: TimeDto): TimeDto[] {
		const possibleTimes: TimeDto[] = [];
		possibleTimes[0] = firstTime;
		let iterationTime = {
			hours: firstTime.hours,
			minutes: firstTime.minutes
		};
		let possibleTimeIterationIndex = 1;
		let lastMinutesLimit;

		lastTime.hours === 23 && lastTime.minutes === 59 ? lastMinutesLimit = 30 : lastMinutesLimit = lastTime.minutes;

		while ((iterationTime.hours < lastTime.hours || iterationTime.minutes < lastMinutesLimit)) {
			if (possibleTimes[possibleTimeIterationIndex - 1].minutes === MINUTES_IN_HOUR - this.selectedAgenda.appointmentDuration) {
				iterationTime = {
					hours: possibleTimes[possibleTimeIterationIndex - 1].hours + 1,
					minutes: 0
				};
			}
			else {
				iterationTime = {
					hours: possibleTimes[possibleTimeIterationIndex - 1].hours,
					minutes: possibleTimes[possibleTimeIterationIndex - 1].minutes + this.selectedAgenda.appointmentDuration
				};
			}
			possibleTimes.push(iterationTime);
			possibleTimeIterationIndex++;
		}
		if (possibleTimes.findIndex(time => time.hours === lastTime.hours && time.minutes === lastTime.minutes) === -1) {
			possibleTimes.push(lastTime);
		}
		return possibleTimes;
	}

	filterEndingTime() {
		this.possibleEndingTime = this.possibleStartingTime.filter(time => this.compareTimes(time, this.form.value.init));

		if (this.form.value.end <= this.form.value.init)
			this.form.patchValue({ end: this.possibleEndingTime[0] });
	}

	private compareTimes(time1: TimeDto, time2: TimeDto): boolean {
		return time1.hours > time2.hours || (time1.hours === time2.hours && time1.minutes > time2.minutes);
	}

	private fromStringToDate(date: string): Date {
		const dateData = date.split("-");
		return new Date(+dateData[0], +dateData[1]-1, +dateData[2]);
	}
}
