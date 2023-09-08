import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { AppFeature, ClinicalSpecialtyDto, EAppointmentModality, EmptyAppointmentDto, TimeDto } from '@api-rest/api-model';
import { DiaryService } from '@api-rest/services/diary.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import * as moment from 'moment';
import { Moment } from 'moment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Component({
	selector: 'app-search-appointments-by-specialty',
	templateUrl: './search-appointments-by-specialty.component.html',
	styleUrls: ['./search-appointments-by-specialty.component.scss'],
})
export class SearchAppointmentsBySpecialtyComponent implements OnInit {

	@Input() isVisible = false;
	aliasTypeaheadOptions$: Observable<TypeaheadOption<string>[]>;
	timesToFilter: TimeDto[];
	initialTimes: TimeDto[];
	endingTimes: TimeDto[];
	searchBySpecialtyForm: UntypedFormGroup;
	emptyAppointments: EmptyAppointmentDto[];
	emptyAppointmentsFiltered: EmptyAppointmentDto[];
	patientId: number;
	showClinicalSpecialtyError = false;
	private today: Date;
	MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	isEnableTelemedicina:boolean;

	dateSearchFilter = (d: Moment): boolean => {
		const parsedDate = d?.toDate();
		parsedDate?.setHours(0, 0, 0, 0);
		return parsedDate >= this.today;
	};

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly diaryService: DiaryService,
		private readonly route: ActivatedRoute,
		private readonly featureFlagService: FeatureFlagService,
	) { this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.isEnableTelemedicina = isEnabled)}

	ngOnInit(): void {
		this.route.queryParams.subscribe(qp => {
			this.patientId = Number(qp.idPaciente);
		});

		this.today = new Date();
		this.today.setHours(0, 0, 0, 0);

		this.initializeTimeFilters();
		this.searchBySpecialtyForm = this.formBuilder.group({
			clinicalSpecialty: [null, Validators.required],
			initialTime: [this.initialTimes[7], Validators.required],
			endingTime: [this.endingTimes[0], Validators.required],
			mondayControl: [true, Validators.nullValidator],
			tuesdayControl: [true, Validators.nullValidator],
			wednesdayControl: [true, Validators.nullValidator],
			thursdayControl: [true, Validators.nullValidator],
			fridayControl: [true, Validators.nullValidator],
			saturdayControl: [false, Validators.nullValidator],
			sundayControl: [false, Validators.nullValidator],
			searchInitialDate: [moment(), Validators.required],
			searchEndingDate: [{ value: moment().add(21, "days"), disabled: true }, Validators.required],
			modality: [this.MODALITY_ON_SITE_ATTENTION, Validators.required]
		});

		this.aliasTypeaheadOptions$ = this.getClinicalSpecialtiesTypeaheadOptions$();
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['isVisible'].previousValue && !changes['isVisible'].currentValue) {
			this.ngOnInit();
			this.resetEmptyAppointmentList(null);
		}
	}
	private initializeTimeFilters() {
		this.timesToFilter = this.generateInitialTimes();
		this.endingTimes = this.timesToFilter.slice(8);
		this.initialTimes = this.timesToFilter.slice(0, -1);
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
		if(clinicalSpecialty){
			this.searchBySpecialtyForm.controls.clinicalSpecialty.setValue(clinicalSpecialty);
			this.showClinicalSpecialtyError = false;
		}else{
			this.emptyAppointments = [];
			this.emptyAppointmentsFiltered = [];
		}
	}

	updateSearchEndingDate(changedValue) {
		if (changedValue.value) {
			const newInitialDate = changedValue.value.clone();
			this.searchBySpecialtyForm.controls.searchEndingDate.setValue(newInitialDate.add(21, "days"));
		}
	}

	submit() {
		if (this.searchBySpecialtyForm.valid) {
			const selectedDaysOfWeek = [];
			if (this.searchBySpecialtyForm.value.mondayControl)
				selectedDaysOfWeek.push(1);
			if (this.searchBySpecialtyForm.value.tuesdayControl)
				selectedDaysOfWeek.push(2);
			if (this.searchBySpecialtyForm.value.wednesdayControl)
				selectedDaysOfWeek.push(3);
			if (this.searchBySpecialtyForm.value.thursdayControl)
				selectedDaysOfWeek.push(4);
			if (this.searchBySpecialtyForm.value.fridayControl)
				selectedDaysOfWeek.push(5);
			if (this.searchBySpecialtyForm.value.saturdayControl)
				selectedDaysOfWeek.push(6);
			if (this.searchBySpecialtyForm.value.sundayControl)
				selectedDaysOfWeek.push(0);
			this.diaryService.generateEmptyAppointments(
				{
					aliasOrSpecialtyName: this.searchBySpecialtyForm.value.clinicalSpecialty,
					daysOfWeek: selectedDaysOfWeek,
					endSearchTime: {
						hours: this.searchBySpecialtyForm.value.endingTime.hours,
						minutes: this.searchBySpecialtyForm.value.endingTime.minutes
					},
					initialSearchTime: {
						hours: this.searchBySpecialtyForm.value.initialTime.hours,
						minutes: this.searchBySpecialtyForm.value.initialTime.minutes
					},
					initialSearchDate: {
						year: this.searchBySpecialtyForm.value.searchInitialDate.year(),
						month: this.searchBySpecialtyForm.value.searchInitialDate.month() + 1,
						day: this.searchBySpecialtyForm.value.searchInitialDate.date()
					},
					endingSearchDate: {
						year: this.searchBySpecialtyForm.controls.searchEndingDate.value.year(),
						month: this.searchBySpecialtyForm.controls.searchEndingDate.value.month() + 1,
						day: this.searchBySpecialtyForm.controls.searchEndingDate.value.date()
					},
					modality: this.searchBySpecialtyForm.controls.modality.value,
				}
			).subscribe(emptyAppointments => {
				this.emptyAppointments = emptyAppointments;
				this.emptyAppointmentsFiltered = this.emptyAppointments.slice(0, 5);
			});
		}
		else {
			if (!this.searchBySpecialtyForm.value.clinicalSpecialty) {
				this.showClinicalSpecialtyError = true;
			}
		}
	}

	onPageChange($event) {
		this.emptyAppointmentsFiltered = this.emptyAppointments.slice($event.pageIndex * $event.pageSize, $event.pageIndex * $event.pageSize + $event.pageSize);
	}

	resetEmptyAppointmentList(event) {
		this.emptyAppointments = null;
	}

}
