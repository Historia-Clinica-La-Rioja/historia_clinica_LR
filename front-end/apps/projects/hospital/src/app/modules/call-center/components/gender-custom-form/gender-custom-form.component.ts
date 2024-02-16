import { Component, EventEmitter, Input, OnDestroy, OnInit, forwardRef } from '@angular/core';
import { NG_VALUE_ACCESSOR, NG_VALIDATORS, FormGroup, FormControl } from '@angular/forms';
import { GenderDto } from '@api-rest/api-model';
import { PersonMasterDataService } from '@api-rest/services/person-master-data.service';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { Observable } from 'rxjs';

@Component({
	selector: 'app-gender-custom-form',
	templateUrl: './gender-custom-form.component.html',
	styleUrls: ['./gender-custom-form.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => GenderCustomFormComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => GenderCustomFormComponent),
		},
	],
})
export class GenderCustomFormComponent extends AbstractCustomForm implements OnInit, OnDestroy {

	genders$: Observable<GenderDto[]>;
	form: FormGroup<GenderCustomForm>;
	@Input()
	set submitParentFormEvent(event: EventEmitter<void>) {
		super.subscribeToSubmitParentForm(event);
	};

	constructor(
		private readonly personMasterDataService: PersonMasterDataService,
	) {
		super();
	}

	ngOnInit() {
		this.createForm();
		this.setGenders();
	}

	ngOnDestroy() {
		super.unSubcribeFormChanges();
	}

	createForm() {
		this.form = new FormGroup<GenderCustomForm>({
			genderId: new FormControl(Gender.FEMALE),
		});
	}

	private setGenders() {
		this.genders$ = this.personMasterDataService.getGenders();
	}
}

enum Gender {
	FEMALE = 1, MASCULINE, X
}

interface GenderCustomForm {
	genderId: FormControl<number>;
}