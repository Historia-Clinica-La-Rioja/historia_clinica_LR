import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TimeDto } from '@api-rest/api-model';

@Component({
	selector: 'app-search-appointments-by-specialty',
	templateUrl: './search-appointments-by-specialty.component.html',
	styleUrls: ['./search-appointments-by-specialty.component.scss'],
})
export class SearchAppointmentsBySpecialtyComponent implements OnInit {

	timesToFilter: TimeDto[];
	initialTimes: TimeDto[];
	endingTimes: TimeDto[];
	searchBySpecialtyForm: FormGroup;

	constructor(
		private readonly formBuilder: FormBuilder,
	) {}

	ngOnInit(): void {
		this.initializeTimeFilters();
		this.searchBySpecialtyForm = this.formBuilder.group({
			initialTime: [this.timesToFilter[0], Validators.required],
			endingTime: [this.endingTimes[0], Validators.required],
		});
	}

	private initializeTimeFilters() {
		this.timesToFilter = this.generateInitialTimes();
		this.endingTimes = this.timesToFilter.slice(1);
	}

	private generateInitialTimes(): TimeDto[] {
		const initializedTimes = [];
		for (let currentHour = 0; currentHour < 24; currentHour++) {
			initializedTimes.push({
				hours: currentHour,
				minutes: 0
			});
		}
		initializedTimes.push({
			hours: 23,
			minutes: 59
		});
		return initializedTimes;
	}

	filterEndingTime() {
		this.endingTimes = this.timesToFilter.filter(time => this.compareTimes(time, this.searchBySpecialtyForm.value.initialTime));
		this.searchBySpecialtyForm.controls.endingTime.setValue(this.endingTimes[0]);
	}

	private compareTimes(time1: TimeDto, time2: TimeDto): boolean {
		return time1.hours > time2.hours || (time1.hours === time2.hours && time1.minutes > time2.minutes);
	}

}
