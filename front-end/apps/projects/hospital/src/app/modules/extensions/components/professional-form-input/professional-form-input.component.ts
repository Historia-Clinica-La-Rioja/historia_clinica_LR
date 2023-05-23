import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {UntypedFormControl, UntypedFormGroup} from '@angular/forms';
import { AppFeature } from '@api-rest/api-model';
import { ProfessionalDto } from '@api-rest/api-model';

import {
	HealthcareProfessionalByInstitutionService
} from '@api-rest/services/healthcare-professional-by-institution.service';
import {FeatureFlagService} from '@core/services/feature-flag.service';

@Component({
	selector: 'app-professional-form-input',
	templateUrl: './professional-form-input.component.html',
	styleUrls: ['./professional-form-input.component.scss']
})
export class ProfessionalFormInputComponent implements OnInit {

	@Input() label: string;
	@Output() professionalChange = new EventEmitter<string[]>();

	professionalForm = new UntypedFormGroup({
		professional: new UntypedFormControl(),
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
		this.healthcareProfessionalByInstitutionService.getAll().subscribe(professionals => {
				this.professionalList = professionals.map( professional => {
						const professionalName = this.getFullNameByFF(professional);
						return {
							compareValue: professionalName,
							value: professionalName
						}
					})
			});
		this.professionalForm.valueChanges.subscribe(value => this.emitProfessionalChange(value));
	}

	emitProfessionalChange(value) {
		this.professionalChange.emit(value ? [value] : null);
	}

	getFullNameByFF(professional: ProfessionalDto): string {
		const firstName = (professional.middleNames) ? professional.firstName + " " + professional.middleNames : professional.firstName;
		const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : firstName;
		const lastName = (professional.otherLastNames) ? professional.lastName + " " + professional.otherLastNames : professional.lastName;
		return (this.nameSelfDeterminationFF) ? lastName + ", " + nameSelfDetermination : lastName + ", " + firstName;
	}


}
