import { Component, Input, OnInit, SimpleChanges, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { AppFeature, AppointmentSearchDto, ClinicalSpecialtyDto, EAppointmentModality, EmptyAppointmentDto, SharedSnomedDto, TimeDto } from '@api-rest/api-model';
import { DiaryService } from '@api-rest/services/diary.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import * as moment from 'moment';
import { Moment } from 'moment';
import { SearchCriteria } from '../search-criteria/search-criteria.component';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { PracticesService } from '@api-rest/services/practices.service';
import { SearchAppointmentInformation, SearchAppointmentsInfoService } from '@access-management/services/search-appointment-info.service';
import { Tabs } from '@turnos/constants/tabs';
import { TabsService } from '@turnos/services/tabs.service';

const PAGE_MIN_SIZE = 5;

@Component({
	selector: 'app-search-appointments-by-specialty',
	templateUrl: './search-appointments-by-specialty.component.html',
	styleUrls: ['./search-appointments-by-specialty.component.scss'],
})
export class SearchAppointmentsBySpecialtyComponent implements OnInit {

	@ViewChild('paginator') paginator: MatPaginator;
	@Input()
	set isVisible(value: boolean) {
		if (value)
			this.setReferenceInformation();
	};
	aliasTypeaheadOptions: TypeaheadOption<string>[];
	timesToFilter: TimeDto[];
	initialTimes: TimeDto[];
	endingTimes: TimeDto[];
	form: UntypedFormGroup;
	emptyAppointments: EmptyAppointmentDto[];
	emptyAppointmentsFiltered: EmptyAppointmentDto[];
	patientId: number;
	showClinicalSpecialtyError = false;
	showPracticeError = false;
	private today: Date;
	MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	isEnableTelemedicina: boolean;
	searchCriteria = SearchCriteria;
	selectedSearchCriteria = SearchCriteria.CONSULTATION;
	practices: SharedSnomedDto[];
	externalInformation: SearchAppointmentInformation;
	externalSetValueSpecialty: TypeaheadOption<string>;
	showCareNetworkSection = false;
	resetTypeahead = false;

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
		private readonly practicesService: PracticesService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService,
	) { this.featureFlagService.isActive(AppFeature.HABILITAR_TELEMEDICINA).subscribe(isEnabled => this.isEnableTelemedicina = isEnabled) }

	ngOnInit(): void {
		this.route.queryParams.subscribe(qp => {
			this.patientId = Number(qp.idPaciente);
		});
		this.today = new Date();
		this.today.setHours(0, 0, 0, 0);

		this.initializeTimeFilters();
		this.form = this.formBuilder.group({
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
			modality: [this.MODALITY_ON_SITE_ATTENTION, Validators.required],
			practice: [null],
		});

		this.setClinicalSpecialtiesTypeaheadOptions();
		this.practicesService.getByActiveDiaries().subscribe(practices => {
			this.practices = practices;
		});
	}

	ngOnChanges(changes: SimpleChanges): void {
		if (changes['isVisible'].previousValue && !changes['isVisible'].currentValue) {
			this.clearResults();
			this.ngOnInit();
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
		this.endingTimes = this.timesToFilter.filter(time => this.compareTimes(time, this.form.value.initialTime));
		this.form.controls.endingTime.setValue(this.endingTimes[0]);
	}

	private compareTimes(time1: TimeDto, time2: TimeDto): boolean {
		return time1.hours > time2.hours || (time1.hours === time2.hours && time1.minutes > time2.minutes);
	}

	private setClinicalSpecialtiesTypeaheadOptions(): void {
		this.diaryService.getActiveDiariesAliases().subscribe(clinicalSpecialties =>
			this.aliasTypeaheadOptions = clinicalSpecialties.map(specialty => this.toTypeaheadOption(specialty))
		)
	}

	private toTypeaheadOption(clinicalSpecialty: string): TypeaheadOption<string> {
		return {
			compareValue: clinicalSpecialty,
			value: clinicalSpecialty
		}
	}

	setClinicalSpecialty(clinicalSpecialty: ClinicalSpecialtyDto) {
		this.form.controls.clinicalSpecialty.setValue(null);
		if (clinicalSpecialty) {
			this.resetTypeahead = false;
			this.form.controls.clinicalSpecialty.setValue(clinicalSpecialty);
			this.showClinicalSpecialtyError = false;
		} else {
			this.emptyAppointments = [];
			this.emptyAppointmentsFiltered = [];
		}
	}

	updateSearchEndingDate(changedValue) {
		if (changedValue.value) {
			const newInitialDate = changedValue.value.clone();
			this.form.controls.searchEndingDate.setValue(newInitialDate.add(21, "days"));
		}
	}

	submit() {
		if (this.form.valid) {
			const selectedDaysOfWeek = [];
			if (this.form.value.mondayControl)
				selectedDaysOfWeek.push(1);
			if (this.form.value.tuesdayControl)
				selectedDaysOfWeek.push(2);
			if (this.form.value.wednesdayControl)
				selectedDaysOfWeek.push(3);
			if (this.form.value.thursdayControl)
				selectedDaysOfWeek.push(4);
			if (this.form.value.fridayControl)
				selectedDaysOfWeek.push(5);
			if (this.form.value.saturdayControl)
				selectedDaysOfWeek.push(6);
			if (this.form.value.sundayControl)
				selectedDaysOfWeek.push(0);
			const searchAppointmentDto = this.buildAppointmentSearch(selectedDaysOfWeek);
			this.diaryService.generateEmptyAppointments(searchAppointmentDto).subscribe(emptyAppointments => {
				this.emptyAppointments = emptyAppointments;
				this.emptyAppointmentsFiltered = this.emptyAppointments.slice(0, PAGE_MIN_SIZE);
				if (this.paginator) {
					this.paginator.pageSize = PAGE_MIN_SIZE;
				}
				this.showCareNetworkSection = this.externalInformation?.enableSectionToSearchAppointmentInOtherTab;
			});
		}
		else {
			if (!this.form.value.clinicalSpecialty && this.form.controls.clinicalSpecialty.hasValidator(Validators.required)) {
				this.showClinicalSpecialtyError = true;
			}

			if (!this.form.value.pratice && this.form.controls.practice.hasValidator(Validators.required)) {
				this.showPracticeError = true;
				this.emptyAppointments = [];
				this.emptyAppointmentsFiltered = [];
			}
		}
	}

	onPageChange($event) {
		this.emptyAppointmentsFiltered = this.emptyAppointments.slice($event.pageIndex * $event.pageSize, $event.pageIndex * $event.pageSize + $event.pageSize);
	}

	clearInformation() {
		this.searchAppointmentsInfoService.clearInfo();
		this.clearResults();
	}

	clearResults() {
		this.externalInformation = null;
		this.externalSetValueSpecialty = null;
		this.resetControls();
		this.clearLists();		
		this.resetTypeahead = true;
		this.selectedSearchCriteria = SearchCriteria.CONSULTATION;
		this.showCareNetworkSection = false;
	}

	setCriteria(selectedCriteria: SearchCriteria) {
		this.selectedSearchCriteria = selectedCriteria;
		this.setValidators();
	}

	setPractice(practice: SharedSnomedDto) {
		if (practice)
			this.form.controls.practice.setValue(practice.id);
		else {
			this.form.controls.practice.setValue(null);
			this.emptyAppointments = [];
			this.emptyAppointmentsFiltered = [];
		}
		this.showPracticeError = false;
	}

	searchAppointmentsInCareNetwork() {
		this.searchAppointmentsInfoService.loadInformation(this.patientId, this.externalInformation.referenceCompleteData);
		this.tabsService.setTab(Tabs.CARE_NETWORK);
	}

	private setValidators() {
		if (this.selectedSearchCriteria === SearchCriteria.CONSULTATION) {
			this.form.controls.clinicalSpecialty.addValidators(Validators.required);
			this.form.controls.clinicalSpecialty.updateValueAndValidity();
			this.form.controls.practice.removeValidators(Validators.required);
			this.form.controls.practice.updateValueAndValidity();
		}
		else {
			this.form.controls.practice.addValidators(Validators.required);
			this.form.controls.practice.updateValueAndValidity();
			this.form.controls.clinicalSpecialty.removeValidators(Validators.required);
			this.form.controls.clinicalSpecialty.updateValueAndValidity();
		}
		this.showClinicalSpecialtyError = false;
		this.showPracticeError = false;
	}

	private buildAppointmentSearch(selectedDaysOfWeek: number[]): AppointmentSearchDto {
		return {
			aliasOrSpecialtyName: this.form.value.clinicalSpecialty,
			daysOfWeek: selectedDaysOfWeek,
			endSearchTime: this.form.value.endingTime,
			initialSearchTime: this.form.value.initialTime,
			initialSearchDate: dateToDateDto(new Date(this.form.controls.searchInitialDate.value)),
			endingSearchDate: dateToDateDto(new Date(this.form.controls.searchEndingDate.value)),
			modality: this.form.controls.modality.value,
			practiceId: this.form.controls.practice.value
		}
	}

	private setReferenceInformation(): void {
		this.externalInformation = this.searchAppointmentsInfoService.getSearchAppointmentInfo();

		if (!this.externalInformation)
			return;

		const { patientId, formInformation } = this.externalInformation;

		this.patientId = patientId;

		const { searchCriteria, clinicalSpecialty, practice } = formInformation;
		this.setCriteria(searchCriteria);

		if (clinicalSpecialty) {
			const specialtyValue = clinicalSpecialty.value;
			this.externalSetValueSpecialty = this.toTypeaheadOption(specialtyValue.name);
			this.aliasTypeaheadOptions = [this.toTypeaheadOption(specialtyValue.name)];
			this.setClinicalSpecialty(specialtyValue);
		}

		if (practice) {
			this.practices = [practice];
			this.setPractice(practice);
		}

		this.searchAppointmentsInfoService.clearInfo();
	}

	private clearLists() {
		this.practices = [];
		this.emptyAppointments = null;
		this.emptyAppointmentsFiltered = null;
	}

	private resetControls() {
		const formControls = this.form.controls;
		formControls.clinicalSpecialty.setValue(null);
		formControls.clinicalSpecialty.enable();
		formControls.practice.setValue(null);
		formControls.practice.enable();
	}


}
