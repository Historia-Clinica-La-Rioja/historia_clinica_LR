import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { AppFeature } from '@api-rest/api-model';
import { HealthcareProfessionalByInstitutionService } from '@api-rest/services/healthcare-professional-by-institution.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';

@Component({
	selector: 'app-professional-form-input',
	templateUrl: './professional-form-input.component.html',
	styleUrls: ['./professional-form-input.component.scss']
})
export class ProfessionalFormInputComponent implements OnInit {

	@Input() label: string;
	@Input() hint: string;
	@Output() professionalChange = new EventEmitter<string[]>();

	professionalForm = new FormGroup({
		professional: new FormControl(),
	});

	professionalList = [];
	nameSelfDeterminationFF: boolean;

	constructor(
		private readonly healthcareProfessionalByInstitutionService: HealthcareProfessionalByInstitutionService,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {
		this.healthcareProfessionalByInstitutionService.getAll().subscribe(
			professionals => {
				this.professionalList = professionals.map(
					professional => {
						const firstName = (professional.middleNames) ? professional.firstName + " " + professional.middleNames : professional.firstName;
						const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : firstName;
						const lastName = (professional.otherLastNames) ? professional.lastName + " " + professional.otherLastNames : professional.lastName;
						const fullName = (this.nameSelfDeterminationFF) ? lastName + ", " + nameSelfDetermination : lastName + ", " + firstName;
					return fullName;
				})
			});
		this.professionalForm.valueChanges.subscribe(value => this.emitProfessionalChange(value));
	}

	emitProfessionalChange(value) {
		this.professionalChange.emit(value.professional? [value.professional] : null);
	}

	clear(control: AbstractControl): void {
		control.reset();
	}
}
