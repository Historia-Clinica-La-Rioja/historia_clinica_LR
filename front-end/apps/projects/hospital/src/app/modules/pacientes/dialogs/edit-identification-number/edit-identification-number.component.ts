import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { GenderDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { PersonService } from '@api-rest/services/person.service';
import { hasError } from '@core/utils/form.utils';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { finalize } from 'rxjs';

@Component({
	selector: 'app-edit-identification-number',
	templateUrl: './edit-identification-number.component.html',
	styleUrls: ['./edit-identification-number.component.scss']
})
export class EditIdentificationNumberComponent implements OnInit {
	hasError = hasError;
	form: FormGroup;
	genders: GenderDto[];
	formSubmitted:boolean=false;
	isLoading:boolean;

	constructor(private formBuilder: FormBuilder,
		private personMasterDataService: PersonMasterDataService,
		private personService: PersonService,
		private snackBarService: SnackBarService) { }

	ngOnInit(): void {
		this.personMasterDataService.getGenders()
		.subscribe(genders => {
			this.genders = genders;
		});
		this.form = this.formBuilder.group({
			identificationNumber: [null, [Validators.required]],
			genderId: [null,[Validators.required]]
		})
	}

	save() {
		this.formSubmitted=true;
		if(this.form.valid){
			this.formSubmitted=true;
			this.callRenaperService();
		}
	}

	private callRenaperService(): void {
		this.personService.getRenaperPersonData({
			identificationNumber: this.form.controls.identificationNumber.value,
			genderId: this.form.controls.genderId.value,
		})
			.pipe(finalize(() => this.isLoading = false))
			.subscribe(
				personData => {

				}, () => {
					this.snackBarService.showError('pacientes.search.RENAPER_TIMEOUT');
				});
	}

}
