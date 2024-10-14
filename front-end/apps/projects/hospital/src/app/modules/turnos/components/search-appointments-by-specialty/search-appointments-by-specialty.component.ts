import { Component, OnInit, ViewChild } from '@angular/core';
import { UntypedFormBuilder, UntypedFormGroup, Validators } from '@angular/forms';
import { MatPaginator } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { AppFeature, AppointmentSearchDto, ClinicalSpecialtyDto, EAppointmentModality, EmptyAppointmentDto, MasterDataDto, SharedSnomedDto, TimeDto, UnsatisfiedAppointmentDemandDto } from '@api-rest/api-model';
import { DiaryService } from '@api-rest/services/diary.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SearchCriteria } from '../search-criteria/search-criteria.component';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';
import { PracticesService } from '@api-rest/services/practices.service';
import { SearchAppointmentInformation, SearchAppointmentsInfoService, SearchCriteriaValues } from '@access-management/services/search-appointment-info.service';
import { TabsLabel } from '@turnos/constants/tabs';
import { TabsService } from '@turnos/services/tabs.service';
import { UnsatisfiedDemandService } from '@api-rest/services/unsatisfied-demand.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { processErrors } from '@core/utils/form.utils';
import { datePlusDays } from '@core/utils/date.utils';

const FIRST_SPECIALTY_POSITION = 0
const PAGE_MIN_SIZE = 5;
const ONE_ELEMENT = 1;
const PLUS_DAY = 21;

@Component({
	selector: 'app-search-appointments-by-specialty',
	templateUrl: './search-appointments-by-specialty.component.html',
	styleUrls: ['./search-appointments-by-specialty.component.scss'],
})
export class SearchAppointmentsBySpecialtyComponent implements OnInit {

	@ViewChild('paginator') paginator: MatPaginator;
	aliasTypeaheadOptions: TypeaheadOption<number>[];
	clinicalSpecialtyTypeaheadOptions: TypeaheadOption<number>[];
	timesToFilter: TimeDto[];
	initialTimes: TimeDto[];
	endingTimes: TimeDto[];
	form: UntypedFormGroup;
	emptyAppointments: EmptyAppointmentDto[];
	emptyAppointmentsFiltered: EmptyAppointmentDto[];
	patientId: number;
	showClinicalSpecialtyError = false;
	showPracticeError = false;
	today: Date;
	MODALITY_ON_SITE_ATTENTION = EAppointmentModality.ON_SITE_ATTENTION;
	MODALITY_PATIENT_VIRTUAL_ATTENTION = EAppointmentModality.PATIENT_VIRTUAL_ATTENTION;
	isEnableTelemedicina: boolean;
	searchCriteria = SearchCriteria;
	selectedSearchCriteria = SearchCriteria.CONSULTATION;
	practices: SharedSnomedDto[];
	externalInformation: SearchAppointmentInformation;
	externalSetValueSpecialty: TypeaheadOption<number>;
	externalSetValueAlias: TypeaheadOption<number>;
	resetRegisterDemandButtonDisabled = false;
	redirectionDisabled = false;

	constructor(
		private readonly formBuilder: UntypedFormBuilder,
		private readonly diaryService: DiaryService,
		private readonly route: ActivatedRoute,
		private readonly featureFlagService: FeatureFlagService,
		private readonly practicesService: PracticesService,
		private readonly searchAppointmentsInfoService: SearchAppointmentsInfoService,
		private readonly tabsService: TabsService,
		private readonly unsatisfiedDemandService: UnsatisfiedDemandService,
		private readonly snackBarService: SnackBarService
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
			alias: [null],
			initialTime: [this.initialTimes[7], Validators.required],
			endingTime: [this.endingTimes[0], Validators.required],
			mondayControl: [true, Validators.nullValidator],
			tuesdayControl: [true, Validators.nullValidator],
			wednesdayControl: [true, Validators.nullValidator],
			thursdayControl: [true, Validators.nullValidator],
			fridayControl: [true, Validators.nullValidator],
			saturdayControl: [false, Validators.nullValidator],
			sundayControl: [false, Validators.nullValidator],
			searchInitialDate: [this.today, Validators.required],
			searchEndingDate: [{ value: datePlusDays(this.today, PLUS_DAY), disabled: true }, Validators.required],
			modality: [this.MODALITY_ON_SITE_ATTENTION, Validators.required],
			practice: [null],
		});

		this.externalInformation = this.searchAppointmentsInfoService.getSearchAppointmentInfo();
		if (!this.externalInformation) {
			this.setClinicalSpecialtiesTypeaheadOptions();
			this.setPractices();
		}
		else this.setReferenceInformation();
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

	private setClinicalSpecialtiesTypeaheadOptions() {
		this.diaryService.getClinicalSpecialtys().subscribe(clinicalSpecialties =>
			this.clinicalSpecialtyTypeaheadOptions = clinicalSpecialties.map(specialty => this.toTypeaheadOption(specialty))
		)
	}

	private toTypeaheadOption(option: MasterDataDto | ClinicalSpecialtyDto): TypeaheadOption<number> {
		let compareValue: string;

		if ('description' in option) {
			compareValue = option.description;
		} else {
			compareValue = option.name;
		}
		return {
			compareValue: compareValue,
			value: option.id,
		}
	}

	setClinicalSpecialty(clinicalSpecialty: MasterDataDto) {
		this.form.controls.clinicalSpecialty.setValue(null);
		if (clinicalSpecialty) {
			this.form.controls.clinicalSpecialty.setValue(clinicalSpecialty);
			this.showClinicalSpecialtyError = false;
			this.getAliasesByClinicalSpecialty();
		} else {
			this.emptyAppointments = null;
			this.emptyAppointmentsFiltered = null;
			this.form.controls.alias.setValue(null);
			this.aliasTypeaheadOptions = [];
			this.externalSetValueAlias = { compareValue: null, value: null };
		}

	}

	getAliasesByClinicalSpecialty() {
		let withPractices = false;
		if (this.selectedSearchCriteria === SearchCriteria.PRACTICES) {
			withPractices = true;
		}
		this.diaryService.getClinicalSpecialtyAliasesWithActiveDiaries(this.form.value.clinicalSpecialty, withPractices).subscribe(aliases => {
			this.aliasTypeaheadOptions = aliases.map(alias => this.toTypeaheadOption(alias));
		})
	}

	setAliasByClinicalSpecialty(alias: MasterDataDto) {
		this.form.controls.alias.setValue(null);
		if (alias) {
			this.form.controls.alias.setValue(alias);
		}
	}

	updateSearchEndingDate(selectedDate: Date) {
		this.form.controls.searchInitialDate.setValue(selectedDate);
		this.form.controls.searchEndingDate.setValue(datePlusDays(selectedDate, PLUS_DAY));
	}

	getSelectedDaysOfWeek(): number[] {
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
		return selectedDaysOfWeek;
	}

	submit() {
		if (this.form.valid) {
			const selectedDaysOfWeek = this.getSelectedDaysOfWeek();
			const searchAppointmentDto = this.buildAppointmentSearch(selectedDaysOfWeek);
			this.diaryService.generateEmptyAppointments(searchAppointmentDto).subscribe(emptyAppointments => {
				this.emptyAppointments = emptyAppointments;
				this.emptyAppointmentsFiltered = this.emptyAppointments.slice(0, PAGE_MIN_SIZE);
				if (this.paginator) {
					this.paginator.pageSize = PAGE_MIN_SIZE;
				}
				this.resetRegisterDemandButtonDisabled = !this.resetRegisterDemandButtonDisabled;
			});
		}
		else {
			if (!this.form.value.clinicalSpecialty && this.form.controls.clinicalSpecialty.hasValidator(Validators.required)) {
				this.showClinicalSpecialtyError = true;
			}

			if (!this.form.value.pratice && this.form.controls.practice.hasValidator(Validators.required)) {
				this.showPracticeError = true;
				this.emptyAppointments = null;
				this.emptyAppointmentsFiltered = null;
			}
		}
	}

	onPageChange($event) {
		this.emptyAppointmentsFiltered = this.emptyAppointments.slice($event.pageIndex * $event.pageSize, $event.pageIndex * $event.pageSize + $event.pageSize);
	}

	clearSearchAppointment() {
		this.clearLists();
	}

	clearInformation() {
		this.searchAppointmentsInfoService.clearInfo();
		this.clearResults();
	}

	clearResults() {
		this.externalInformation = null;
		this.externalSetValueSpecialty = { compareValue: null, value: null };
		this.externalSetValueAlias = { compareValue: null, value: null };
		this.resetControls();
		this.clearLists();
		this.selectedSearchCriteria = SearchCriteria.CONSULTATION;
		this.setPractices();
		this.setValidators();
	}

	resetDataAndSetCriteria(selectedCriteria: SearchCriteria) {
		this.selectedSearchCriteria = selectedCriteria;
		this.aliasTypeaheadOptions = [];
		this.externalSetValueAlias = { compareValue: null, value: null };
		this.externalSetValueSpecialty = { compareValue: null, value: null };
		this.resetEmptyAppointments();
		this.setValidators();
	}

	resetEmptyAppointments() {
		this.emptyAppointments = null;
		this.emptyAppointmentsFiltered = null;
	}

	setPractice(practice: SharedSnomedDto) {
		if (practice)
			this.form.controls.practice.setValue(practice.id);
		else {
			this.form.controls.practice.setValue(null);
			this.emptyAppointments = null;
			this.emptyAppointmentsFiltered = null;
		}
		this.showPracticeError = false;
	}

	searchAppointmentsInCareNetwork() {
		this.searchAppointmentsInfoService.loadInformation(this.patientId, this.externalInformation.referenceCompleteData);
		this.tabsService.setTab(TabsLabel.CARE_NETWORK);
	}

	sendPreloadedData() {
		if (this.externalInformation) {
			this.searchAppointmentsInfoService.loadInformation(this.patientId, this.externalInformation.referenceCompleteData);
		} else {
			const values: SearchCriteriaValues = {
				careModality: this.form.controls.modality.value,
				searchCriteria: this.selectedSearchCriteria,
				startDate: dateToDateDto(this.form.controls.searchInitialDate.value),
			}
			this.searchAppointmentsInfoService.setSearchCriteria(values);
		}
		this.tabsService.setTab(TabsLabel.CARE_NETWORK);
	}

	saveRegisterUnsatisfiedDemand() {
		this.unsatisfiedDemandService.saveUnsatisfiedAppointmentDemand(this.prepareRegisterUnsatisfiedDemand()).subscribe(res => {
			this.snackBarService.showSuccess('turnos.home.messages.SUCCESS_REGISTER_UNSATISFIED_DEMAND')
		}, error => processErrors(error, (msg) => {
			this.snackBarService.showError(msg);
		}));
	}

	prepareRegisterUnsatisfiedDemand(): UnsatisfiedAppointmentDemandDto {
		const selectedDaysOfWeek = this.getSelectedDaysOfWeek();
		return {
			aliasOrSpecialtyName: this.form.controls.clinicalSpecialty.value,
			daysOfWeek: selectedDaysOfWeek,
			endSearchTime: this.form.controls.endingTime.value,
			endingSearchDate: dateToDateDto(new Date(this.form.controls.searchEndingDate.value)),
			initialSearchDate: dateToDateDto(new Date(this.form.controls.searchInitialDate.value)),
			initialSearchTime: this.form.controls.initialTime.value,
			modality: this.form.controls.modality.value,
			practiceId: this.form.controls.practice.value,
		}
	}

	private setValidators() {
		if (this.selectedSearchCriteria === SearchCriteria.CONSULTATION) {
			this.form.controls.clinicalSpecialty.addValidators(Validators.required);
			this.form.controls.clinicalSpecialty.updateValueAndValidity();
			this.form.controls.practice.removeValidators(Validators.required);
			this.form.controls.practice.updateValueAndValidity();
			this.form.controls.practice.setValue(null);
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
			clinicalSpecialtyId: this.form.value.clinicalSpecialty,
			diaryId: this.form.value.alias,
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
		const { patientId, formInformation } = this.externalInformation;
		this.patientId = patientId;
		const { searchCriteria, clinicalSpecialties, practice } = formInformation;
		this.resetDataAndSetCriteria(searchCriteria);
		if (clinicalSpecialties?.length) {
			this.clinicalSpecialtyTypeaheadOptions = clinicalSpecialties.map(specialty => this.toTypeaheadOption(specialty));
			if (this.clinicalSpecialtyTypeaheadOptions.length === ONE_ELEMENT)
				this.externalSetValueSpecialty = this.clinicalSpecialtyTypeaheadOptions[FIRST_SPECIALTY_POSITION];
		}
		if (practice) {
			this.practices = [practice];
			this.setPractice(practice);
		}
		if (!this.externalInformation.referenceCompleteData.careLine.id) {
			this.redirectionDisabled = true;
		}
		this.searchAppointmentsInfoService.clearInfo();
	}

	private clearLists() {
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

	private setPractices() {
		this.practicesService.getByActiveDiaries().subscribe(practices => {
			this.practices = practices;
		});
	}


}
