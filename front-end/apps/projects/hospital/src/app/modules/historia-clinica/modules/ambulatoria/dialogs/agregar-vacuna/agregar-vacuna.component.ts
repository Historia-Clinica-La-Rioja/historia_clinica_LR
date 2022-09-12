import { AfterContentInit, Component, ElementRef, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Moment } from 'moment';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { DateFormat, momentParseDate, newMoment } from '@core/utils/moment.utils';
import {
	ImmunizationDto,
	SnomedDto,
	VaccineConditionsDto,
	VaccineDoseInfoDto,
	VaccineInformationDto,
	VaccineSchemeDto
} from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { SnomedECL } from '@api-rest/api-model';
import { VaccineService } from '@api-rest/services/vaccine.service';
import { scrollIntoError } from '@core/utils/form.utils';
import { MIN_DATE } from "@core/utils/date.utils";
import { VaccineSearchComponent } from '../vaccine-search/vaccine-search.component';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-agregar-vacuna',
	templateUrl: './agregar-vacuna.component.html',
	styleUrls: ['./agregar-vacuna.component.scss'],
	encapsulation: ViewEncapsulation.None
})
export class AgregarVacunaComponent implements OnInit, AfterContentInit {

	readonly HALF_COLUMN_WIDTH: number = 47.5;
	readonly DISABLED_LABEL_COLOR: string = 'rgba(0, 0, 0, 0.38)';
	selectedTab: number = 0;
	doses: VaccineDoseInfoDto[];
	schemes: VaccineSchemeDto[];
	conditions: VaccineConditionsDto[];
	today: Moment = newMoment();
	minDate = MIN_DATE;
	searchConceptsLocallyFF: boolean;
	ecl = SnomedECL.VACCINE;

	// billable form attributes (new vaccine application)
	billableForm: FormGroup;
	searchBillableVaccineForm: FormGroup;
	newVaccineSnomedConcept: SnomedDto;
	tryToSubmit: boolean = false;

	// previous form attributes (register previous vaccine application)
	previousForm: FormGroup;
	searchPreviousVaccineForm: FormGroup;
	previousVaccineSnomedConcept: SnomedDto;
	tryToSubmitPrevious: boolean = false;

	private CLEAR_CASES = {
		condition: (form) => {
			this.disableSchemes(form);
			this.disableDoses(form);
		},
		scheme: (form) => {
			this.disableDoses(form)
		},
	};

	constructor(
		@Inject(MAT_DIALOG_DATA) public data:
			{
				immunization?: ImmunizationDto,
				edit?: boolean,
				patientId: number,
				hasConfirmedAppointment: boolean
			},
		private readonly formBuilder: FormBuilder,
		private readonly vaccineService: VaccineService,
		private readonly dialog: MatDialog,
		private readonly el: ElementRef,
		private readonly featureFlagService: FeatureFlagService,
		public dialogRef: MatDialogRef<AgregarVacunaComponent>
	) { }

	ngAfterContentInit(): void {
		if (this.data?.edit)
			if (!this.data.immunization?.billable)
				this.setSelectedTab(1);
	}

	ngOnInit(): void {

		this.searchPreviousVaccineForm = this.formBuilder.group({
			search: [null]
		});
		this.previousForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [{ value: null, disabled: true }],
			scheme: [{ value: null, disabled: true }],
			dose: [{ value: null, disabled: true }],
			lot: [null],
			institution: [null],
			professional: [null]
		});

		this.searchBillableVaccineForm = this.formBuilder.group({
			search: [null]
		});
		this.billableForm = this.formBuilder.group({
			date: [this.today, Validators.required],
			snomed: [null, Validators.required],
			condition: [{ value: null, disabled: true }],
			scheme: [{ value: null, disabled: true }],
			dose: [{ value: null, disabled: true }],
			lot: [null],
			note: [null]
		});

		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS)
			.subscribe(isOn => this.searchConceptsLocallyFF = isOn);

		if (this.data?.edit) { // then load information to form fields and class attributes

			if (this.data.immunization?.billable) { // then load data to billable form and its attributes

				this.billableForm.controls.date.setValue(momentParseDate(this.data.immunization.administrationDate));

				this.newVaccineSnomedConcept = this.data.immunization.snomed;
				this.billableForm.controls.snomed.setValue(this.newVaccineSnomedConcept.pt);

				this.billableForm.controls.note.setValue(this.data.immunization.note);

				if (this.data.immunization.lotNumber)
					this.billableForm.controls.lot.setValue(this.data.immunization.lotNumber);

				this.vaccineService.vaccineInformation(this.newVaccineSnomedConcept.sctid).subscribe(
					(vaccineInformation: VaccineInformationDto) => {

						// If the vaccine has conditions, then we should load the pre-selected data for condition/scheme/dose
						if (vaccineInformation.conditions.length > 0) {
							this.conditions = vaccineInformation.conditions;
							this.billableForm.get("condition").enable();
							const conditionIndex: number = this.conditions.findIndex(condition => condition.id === this.data.immunization.conditionId);
							if (conditionIndex >= 0) // mean that a condition was selected therefore also a scheme and dose
							{
								this.billableForm.get("condition").setValue(conditionIndex);

								this.schemes = this.conditions[conditionIndex].schemes;
								this.billableForm.get("scheme").enable();
								const schemeIndex: number = this.schemes.findIndex(scheme => scheme.id === this.data.immunization.schemeId);
								if (schemeIndex >= 0) { // it should always be >=0 since the condition was loaded
									this.billableForm.get("scheme").setValue(schemeIndex);

									this.doses = this.schemes[schemeIndex].doses;
									this.billableForm.get("dose").enable();
									const doseIndex: number = this.doses.findIndex(dose => (dose.order == this.data.immunization.dose.order) && (dose.description == this.data.immunization.dose.description));
									if (doseIndex >= 0)
										this.billableForm.get("dose").setValue(doseIndex);
								}
							}
						}

					}
				);

			}
			else { // then load data to previous form and its attributes
				this.previousForm.controls.date.setValue(momentParseDate(this.data.immunization.administrationDate));

				this.previousVaccineSnomedConcept = this.data.immunization.snomed;
				this.previousForm.controls.snomed.setValue(this.previousVaccineSnomedConcept.pt);

				if (this.data.immunization.doctorInfo)
					this.previousForm.controls.professional.setValue(this.data.immunization.doctorInfo);

				if (this.data.immunization.institutionInfo)
					this.previousForm.controls.institution.setValue(this.data.immunization.institutionInfo);

				if (this.data.immunization.lotNumber)
					this.previousForm.controls.lot.setValue(this.data.immunization.lotNumber);

				this.vaccineService.vaccineInformation(this.previousVaccineSnomedConcept.sctid).subscribe(
					(vaccineInformation: VaccineInformationDto) => {

						// If the vaccine has conditions, then we should load the pre-selected data for condition/scheme/dose
						if (vaccineInformation.conditions.length > 0) {
							this.conditions = vaccineInformation.conditions;
							this.previousForm.get("condition").enable();
							const conditionIndex: number = this.conditions.findIndex(condition => condition.id === this.data.immunization.conditionId);
							if (conditionIndex >= 0) // mean that a condition was selected therefore also a scheme and dose
							{
								this.previousForm.get("condition").setValue(conditionIndex);

								this.schemes = this.conditions[conditionIndex].schemes;
								this.previousForm.get("scheme").enable();
								const schemeIndex: number = this.schemes.findIndex(scheme => scheme.id === this.data.immunization.schemeId);
								if (schemeIndex >= 0) { // it should always be >=0 since the condition was loaded
									this.previousForm.get("scheme").setValue(schemeIndex);

									this.doses = this.schemes[schemeIndex].doses;
									this.previousForm.get("dose").enable();
									const doseIndex: number = this.doses.findIndex(dose => (dose.order == this.data.immunization.dose.order) && (dose.description == this.data.immunization.dose.description));
									if (doseIndex >= 0)
										this.previousForm.get("dose").setValue(doseIndex);
								}
							}
						}

					}
				);
			}
		}

	}

	public setSelectedTab(value: number): void {
		this.selectedTab = value;
	}

	public chosenYearHandler(newDate: Moment, form: FormGroup): void {
		if (form.controls.date.value !== null) {
			const ctrlDate: Moment = form.controls.date.value;
			ctrlDate.year(newDate.year());
			form.controls.date.setValue(ctrlDate);
		} else {
			form.controls.date.setValue(newDate);
		}
	}

	public chosenMonthHandler(newDate: Moment, form: FormGroup): void {
		if (form.controls.date.value !== null) {
			const ctrlDate: Moment = form.controls.date.value;
			ctrlDate.month(newDate.month());
			form.controls.date.setValue(ctrlDate);
		} else {
			form.controls.date.setValue(newDate);
		}
	}

	public submit(vaccineInputContainer: HTMLElement): void {
		if (this.billableForm.valid) {
			this.tryToSubmit = false;
			const appliedVaccine: ImmunizationDto = {
				snomed: this.newVaccineSnomedConcept,
				administrationDate: this.billableForm.value.date.format(DateFormat.API_DATE),
				billable: true,
				note: this.billableForm.value.note ? this.billableForm.value.note : "",
			};

			if (this.billableForm.controls.lot.value != null)
				appliedVaccine.lotNumber = this.billableForm.value.lot;

			if (this.conditions && this.billableForm.controls.condition.value != null) {
				appliedVaccine.conditionId = this.conditions[this.billableForm.value.condition].id;
				appliedVaccine.schemeId = this.schemes[this.billableForm.value.scheme].id;
				appliedVaccine.dose = this.doses[this.billableForm.value.dose];
			}

			this.dialogRef.close(appliedVaccine);
		}
		else {
			this.tryToSubmit = true;
			if (this.billableForm.controls.snomed.invalid)
				vaccineInputContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
			else
				scrollIntoError(this.billableForm, this.el);
		}
	}

	public submitPreviousForm(vaccineInputContainer: HTMLElement): void {
		if (this.previousForm.valid) {
			this.tryToSubmitPrevious = false;
			const appliedVaccine: ImmunizationDto = {
				snomed: this.previousVaccineSnomedConcept,
				administrationDate: this.previousForm.value.date.format(DateFormat.API_DATE),
				note: "",
			};

			if (this.previousForm.controls.lot.value != null)
				appliedVaccine.lotNumber = this.previousForm.value.lot;

			if (this.previousForm.controls.institution.value != null || this.previousForm.controls.institution.value !== "")
				appliedVaccine.institutionInfo = this.previousForm.value.institution;

			if (this.previousForm.controls.professional.value != null || this.previousForm.controls.professional.value !== "")
				appliedVaccine.doctorInfo = this.previousForm.value.professional;

			if (this.conditions && this.previousForm.controls.condition.value != null) {
				appliedVaccine.conditionId = this.conditions[this.previousForm.value.condition].id;
				appliedVaccine.schemeId = this.schemes[this.previousForm.value.scheme].id;
				appliedVaccine.dose = this.doses[this.previousForm.value.dose];
			}

			this.dialogRef.close(appliedVaccine);
		}
		else {
			this.tryToSubmitPrevious = true;
			if (this.previousForm.controls.snomed.invalid)
				vaccineInputContainer.scrollIntoView({ behavior: 'smooth', block: 'center' });
			else
				scrollIntoError(this.previousForm, this.el);
		}
	}

	public openSearchBillableVaccineDialog(): void {
		const dialogRef = this.dialog.open(VaccineSearchComponent, {
			data: {
				searchValue: this.searchBillableVaccineForm.value.search
			},
			autoFocus: false,
			width: '70%',
			disableClose: true,
		})

		dialogRef.afterClosed().subscribe((vaccineSelected: SnomedDto) => {
			if (vaccineSelected) {
				this.setBillableConcept(vaccineSelected);
			}
			else {
				this.searchBillableVaccineForm.get("search").setValue(null);
			}
		});
	}

	public openSearchPreviousVaccineDialog(): void {
		const dialogRef = this.dialog.open(VaccineSearchComponent, {
			data: {
				searchValue: this.searchPreviousVaccineForm.value.search
			},
			autoFocus: false,
			width: '70%',
			disableClose: true,
		})

		dialogRef.afterClosed().subscribe((vaccineSelected: SnomedDto) => {
			if (vaccineSelected) {
				this.setPreviousConcept(vaccineSelected);
			}
			else {
				this.searchPreviousVaccineForm.get("search").setValue(null);
			}
		});
	}

	public setBillableConcept(vaccineSelected: SnomedDto): void {
		this.newVaccineSnomedConcept = vaccineSelected;
		this.billableForm.controls.snomed.setValue(vaccineSelected.pt);
		this.loadConditions(vaccineSelected.sctid, this.billableForm);
	}

	public setPreviousConcept(vaccineSelected: SnomedDto): void {
		this.previousVaccineSnomedConcept = vaccineSelected;
		this.previousForm.controls.snomed.setValue(vaccineSelected.pt);
		this.loadConditions(vaccineSelected.sctid, this.previousForm);
	}

	public resetConceptBillableForm(): void {
		delete this.newVaccineSnomedConcept;
		this.billableForm.get("snomed").setValue(null);
		this.searchBillableVaccineForm.get("search").setValue(null);
		this.tryToSubmit = false;
		this.disableDoses(this.billableForm);
		this.disableSchemes(this.billableForm);
		this.disableConditions(this.billableForm);
	}

	public resetConceptPreviousForm(): void {
		delete this.previousVaccineSnomedConcept;
		this.previousForm.get("snomed").setValue(null);
		this.searchPreviousVaccineForm.get("search").setValue(null);
		this.tryToSubmitPrevious = false;
		this.disableDoses(this.previousForm);
		this.disableSchemes(this.previousForm);
		this.disableConditions(this.previousForm);
	}

	private loadConditions(sctid: string, form: FormGroup): void {
		this.vaccineService.vaccineInformation(sctid).subscribe(
			(vaccineInformation: VaccineInformationDto) => {
				this.disableDoses(form);
				this.disableSchemes(form);
				if (vaccineInformation?.conditions.length > 0) {
					this.conditions = vaccineInformation.conditions;
					form.get("condition").enable();
					form.get("condition").setValue(null);
				}
				else
					this.disableConditions(form);
			}
		);
	}

	public loadSchemes(conditionIndex: number, form: FormGroup): void {
		this.disableDoses(form);
		if (form.value.condition != null) {
			this.schemes = this.conditions[conditionIndex].schemes;
			form.get("scheme").enable();
			form.get("scheme").setValue(null);
		}
		else
			this.disableSchemes(form);
	}

	public loadDoses(schemeIndex: number, form: FormGroup): void {
		this.doses = this.schemes[schemeIndex].doses;
		form.get("dose").enable();
		form.get("dose").setValue(null);
	}

	private disableConditions(form: FormGroup): void {
		delete this.conditions;
		form.get("condition").setValue(null);
		form.get("condition").disable();
	}

	private disableSchemes(form: FormGroup): void {
		delete this.schemes;
		form.get("scheme").setValue(null);
		form.get("scheme").disable();
	}

	private disableDoses(form: FormGroup): void {
		delete this.doses;
		form.get("dose").setValue(null);
		form.get("dose").disable();
	}

	clear(control: AbstractControl, value: string, form: FormGroup): void {
		if (this.CLEAR_CASES[value]) {
			this.CLEAR_CASES[value](form);
		}
		control.reset();
	}

}
