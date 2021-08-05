import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { VALIDATIONS, hasError, updateForm } from '@core/utils/form.utils';
import { PatientService } from '@api-rest/services/patient.service';
import { PatientMasterDataService } from '@api-rest/services/patient-master-data.service';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { ContextService } from '@core/services/context.service';
import { IDENTIFICATION_TYPE_IDS } from '@core/utils/patient.utils';
import { ScanPatientPopupComponent } from '@pacientes/dialogs/scan-patient-popup/scan-patient-popup.component';
import { MatDialog } from '@angular/material/dialog';

const ROUTE_SEARCH = 'pacientes/search';
const ROUTE_PROFILE = 'pacientes/profile/';

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
	public identityVerificationStatusArray;
	public identifyTypeArray;
	public hasError = hasError;
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

	search(): void {
		this.formSearchSubmitted = true;
		if (this.formSearch.valid) {
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
			this.disableButtonScan = false;
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
			this.disableButtonScan = false;
			this.formSearch.controls.identifNumber.setValidators([Validators.required, Validators.maxLength(VALIDATIONS.MAX_LENGTH.identif_number), Validators.pattern(/^\S*$/)]);
			updateForm(this.formSearch);
		}
	}

	openScanPatientDialog(): void {
		const dialogRef = this.dialog.open(ScanPatientPopupComponent);
	}
}
