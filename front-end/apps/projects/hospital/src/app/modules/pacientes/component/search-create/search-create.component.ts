import {Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormGroupDirective, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { VALIDATIONS, hasError, updateForm } from '@core/utils/form.utils';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { ScanPatientComponent } from '@pacientes/dialogs/scan-patient/scan-patient.component';
import { MatDialog } from '@angular/material/dialog';

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_PROFILE = 'pacientes/profile/';
const INVALID_INPUT = 'invalid_input';

@Component({
	selector: 'app-search-create',
	templateUrl: './search-create.component.html',
	styleUrls: ['./search-create.component.scss']
})
export class SearchCreateComponent implements OnInit {

	public formSearch: FormGroup;
	public formSearchSubmitted = false;
	public genderOptions;
	public noIdentity = false;
	public disableButtonScan = false;
	public disableButtonConfirm = false;
	public identityVerificationStatusArray;
	public identifyTypeArray;
	public hasError = hasError;
	public patientInformationError = -1;
	public messageOff = false;
	private readonly routePrefix;

	constructor(
		private formBuilder: FormBuilder,
		private router: Router,
		private patientService: PatientService,
		private patientMasterDataService: PatientMasterDataService,
		private personMasterDataService: PersonMasterDataService,
		private contextService: ContextService,
		private dialog: MatDialog,
	) {
		this.routePrefix = 'institucion/' + this.contextService.institutionId + '/';
	}

	ngOnInit(): void {
		this.formSearch = this.formBuilder.group({
			identifType: [null, Validators.required],
			identifNumber: [null, [Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]],
			gender: [null, Validators.required],
			IdentityVerificationStatus: [null],
			comments: [],
		});

		this.personMasterDataService.getIdentificationTypes().subscribe(
			identificationTypes => { this.identifyTypeArray = identificationTypes; });

		this.personMasterDataService.getGenders().subscribe(
			genders => { this.genderOptions = genders; });

		this.patientMasterDataService.getIdentityVerificationStatus().subscribe(
			data => { this.identityVerificationStatusArray = data; });
	}

	search(formDirectiveSearchForm: FormGroupDirective): void {
		this.formSearchSubmitted = true;
		this.validatorsOffInDialog(false, formDirectiveSearchForm);
		if (this.formSearch.valid)  {
			this.disableButtonScan = true;
			this.disableButtonConfirm = true;
			const searchRequest = {
				identificationTypeId: this.formSearch.controls.identifType.value,
				identificationNumber: this.formSearch.controls.identifNumber.value,
				genderId: this.formSearch.controls.gender.value,
			};
			if (this.noIdentity) {
				this.navigateToSearchPatient();
			} else {
				this.patientService.getPatientMinimal(searchRequest).subscribe(
					(data: number[]) => {
						if (!data.length) {
							this.navigateToSearchPatient();
						} else {
							const id = data[0];
							this.router.navigate([this.routePrefix + ROUTE_PROFILE + `${id}`]);
						}
					}
				);
			}
		}
	}

	private navigateToSearchPatient(): void {
		this.router.navigate([this.routePrefix + ROUTE_SEARCH],
			{
				queryParams: {
					identificationTypeId: this.formSearch.controls.identifType.value,
					identificationNumber: this.formSearch.controls.identifNumber.value,
					genderId: this.formSearch.controls.gender.value,
					IdentityVerificationStatus: this.formSearch.controls.IdentityVerificationStatus.value,
					comments: this.formSearch.controls.comments.value,
					noIdentity: this.noIdentity
				}
			});
	}

	noIdentityChange() {
		this.noIdentity = !this.noIdentity;
		if (this.noIdentity) {
			this.disableButtonScan = true;
			this.formSearch.controls.identifType.clearValidators();
			this.formSearch.controls.identifNumber.clearValidators();
			this.formSearch.controls.gender.clearValidators();
			this.formSearch.controls.IdentityVerificationStatus.setValidators(Validators.required);
			updateForm(this.formSearch);
		} else {
			if (this.formSearch.controls.identifType.value === IDENTIFICATION_TYPE_IDS.NO_POSEE){
				this.disableButtonScan = true;
			}
			else {
				this.disableButtonScan = false;
			}
			this.formSearch.controls.identifType.setValidators(Validators.required);
			this.formSearch.controls.gender.setValidators(Validators.required);
			this.formSearch.controls.IdentityVerificationStatus.clearValidators();
			if (this.formSearch.controls.identifType.value !== IDENTIFICATION_TYPE_IDS.NO_POSEE) {
				this.formSearch.controls.identifNumber.setValidators([Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]);
			}
			updateForm(this.formSearch);
		}
	}

	onIdentifTypeChange() {
		const identifTypeID = this.formSearch.controls.identifType.value;

		if (identifTypeID === IDENTIFICATION_TYPE_IDS.NO_POSEE) {
			this.disableButtonScan = true;
			this.formSearch.controls.identifNumber.clearValidators();
			updateForm(this.formSearch);
		} else {
			if (this.noIdentity){
				this.disableButtonScan = true;
			}
			else {
				this.disableButtonScan = false;
			}
			this.formSearch.controls.identifNumber.setValidators([Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]);
			updateForm(this.formSearch);
		}
	}

	public openScanPatientDialog(formGroupeDirective : FormGroupDirective): void {
		this.patientInformationError = -1;
		const identifType = this.formSearch.controls.identifType.value;
		const identifNumber = this.formSearch.controls.identifNumber.value;
		const gender = this.formSearch.controls.gender.value;
		this.validatorsOffInDialog(true, formGroupeDirective);
		this.formSearch.controls.identifType.setValue(identifType);
		this.formSearch.controls.identifNumber?.setValue(identifNumber);
		this.formSearch.controls.gender?.setValue(gender);
		const dialogRef = this.dialog.open(ScanPatientComponent, {
			width: "32%",
			height: "600px",
			data: {
				genderOptions: this.genderOptions,
				identifyTypeArray: this.identifyTypeArray,
			}
		});
		dialogRef.afterClosed().subscribe((patientInformationScan) => {
			if((patientInformationScan !== undefined) && (patientInformationScan !== INVALID_INPUT)){
				this.formSearch.controls.identifType.setValue(patientInformationScan.identifType);
				this.formSearch.controls.identifNumber?.setValue(patientInformationScan.identifNumber);
				this.formSearch.controls.gender?.setValue(patientInformationScan.gender);
				if (this.formSearch.valid){
					this.patientInformationError = 0;
					this.search(formGroupeDirective);
				}
				else{
					this.patientInformationError = 1;
				}
			}
			else {
				if (patientInformationScan === undefined) {
					this.patientInformationError = -1
				}
				else {
					this.patientInformationError = 1
				}
			}
		});

	}

	private validatorsOffInDialog(messageOff: boolean, formDirectiveSearchForm: FormGroupDirective): void {
		if (messageOff) {
			this.messageOff = true;
			formDirectiveSearchForm.resetForm();
			this.formSearch.reset();
		}
		else {
			this.messageOff = false;
		}
	}
}
