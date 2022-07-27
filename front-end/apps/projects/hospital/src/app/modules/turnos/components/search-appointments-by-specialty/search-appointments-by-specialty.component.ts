import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ClinicalSpecialtyDto, TimeDto } from '@api-rest/api-model';
import { DiaryService } from '@api-rest/services/diary.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-search-appointments-by-specialty',
	templateUrl: './search-appointments-by-specialty.component.html',
	styleUrls: ['./search-appointments-by-specialty.component.scss'],
})
export class SearchAppointmentsBySpecialtyComponent implements OnInit {

	aliasTypeaheadOptions$: Observable<TypeaheadOption<string>[]>;
	timesToFilter: TimeDto[];
	initialTimes: TimeDto[];
	endingTimes: TimeDto[];
	searchBySpecialtyForm: FormGroup;
	private today: Date;

	dateSearchFilter = (d: Moment): boolean => {
		const parsedDate = d?.toDate();
		parsedDate?.setHours(0,0,0,0);
		return parsedDate >= this.today;
	};

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly diaryService: DiaryService
	) {}

	ngOnInit(): void {
		this.today = new Date();
		this.today.setHours(0,0,0,0);

		this.initializeTimeFilters();
		this.searchBySpecialtyForm = this.formBuilder.group({
			clinicalSpecialty: [null, Validators.required],
			initialTime: [this.timesToFilter[0], Validators.required],
			endingTime: [this.endingTimes[0], Validators.required],
			mondayControl: [true, Validators.nullValidator],
			tuesdayControl: [true, Validators.nullValidator],
			wednesdayControl: [true, Validators.nullValidator],
			thursdayControl: [true, Validators.nullValidator],
			fridayControl: [true, Validators.nullValidator],
			saturdayControl: [false, Validators.nullValidator],
			sundayControl: [false, Validators.nullValidator],
			searchInitialDate: [null, Validators.required]
		});

		this.aliasTypeaheadOptions$ = this.getClinicalSpecialtiesTypeaheadOptions$();
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

	private getClinicalSpecialtiesTypeaheadOptions$() {
		this.diaryService.getActiveDiariesAliases().subscribe(r => console.log(r))
		return this.diaryService.getActiveDiariesAliases().pipe(map(toTypeaheadOptionList));

		function toTypeaheadOptionList(clinicalSpecialtiesList: string[]):
			TypeaheadOption<string>[] {
			return clinicalSpecialtiesList.map(toTypeaheadOption);

			function toTypeaheadOption(clinicalSpecialty: string): TypeaheadOption<string> {
				return {
					compareValue: clinicalSpecialty,
					value: clinicalSpecialty
				};
			}
		}
	}

	setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
		this.searchBySpecialtyForm.controls.clinicalSpecialty.setValue(clinicalSpecialty);
	}

}
