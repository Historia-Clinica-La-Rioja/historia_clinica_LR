import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AppFeature, EmergencyCareClinicalSpecialtySectorDto, ERole } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { PermissionsService } from '@core/services/permissions.service';
import { anyMatch } from '@core/utils/array.utils';
import { hasError } from '@core/utils/form.utils';
import { SpecialtySectorFormValidityService } from '../../services/specialty-sector-form-validity.service';

@Component({
	selector: 'app-specialty-sector-form',
	templateUrl: './specialty-sector-form.component.html',
	styleUrls: ['./specialty-sector-form.component.scss']
})
export class SpecialtySectorFormComponent implements OnInit {

	hasError = hasError;

	hasProffesionalRole: boolean;
	private hasAdministrativeRole: boolean;
	isAdministrativeAndHasTriageFFInTrue: boolean;
	_specialtySectors:EmergencyCareClinicalSpecialtySectorDto[];

	form: FormGroup<SpecialtySectorForm>;

	@Input() set specialtySectors(specialtySectors: EmergencyCareClinicalSpecialtySectorDto[]) {
		if(specialtySectors){
			this._specialtySectors = specialtySectors;
		}
	}
	@Output() selectedSpecialtySectorForm = new EventEmitter<FormGroup<SpecialtySectorForm>>();

	constructor(
		private permissionsService: PermissionsService,
		private readonly featureFlagService: FeatureFlagService,
		private formBuilder: FormBuilder,
		private specialtySectorFormValidityService: SpecialtySectorFormValidityService,
	) { }

	ngOnInit() {
		this.specialtySectorFormValidityService.setFormValidity(false)
		this.setRoles();
		this.checkAdministrativeFF();
		this.initializeForm();
		this.handleFormChanges();
		this.handleConfirmAttempt();
	}

	private setRoles() {
		this.permissionsService.contextAssignments$().subscribe((userRoles: ERole[]) => {
			this.hasAdministrativeRole = anyMatch<ERole>(userRoles, [ERole.ADMINISTRATIVO]);
			this.hasProffesionalRole = anyMatch<ERole>(userRoles, [ERole.ENFERMERO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA]);
		});
	}

	private checkAdministrativeFF() {
		this.featureFlagService.isActive(AppFeature.HABILITAR_TRIAGE_PARA_ADMINISTRATIVO).subscribe(isEnabled =>
			this.isAdministrativeAndHasTriageFFInTrue = isEnabled && this.hasAdministrativeRole
		)
	}

	private initializeForm() {
		this.form = this.formBuilder.group<SpecialtySectorForm>({
			specialtySector: new FormControl<EmergencyCareClinicalSpecialtySectorDto>(null, Validators.required)
		});
	}

	private handleFormChanges() {
		this.form.valueChanges.subscribe(() => {
			this.specialtySectorFormValidityService.setFormValidity(this.form.valid);
			this.emitForm();
		});
	}

	private handleConfirmAttempt() {
		this.specialtySectorFormValidityService.confirmAttempt$.subscribe((confirmAttempted) => {
			confirmAttempted ? this.form.markAllAsTouched() : this.form.markAsUntouched();
		});
	  }

	private emitForm() {
		this.selectedSpecialtySectorForm.emit(this.form.valid ? this.form : null);
	}
}

export interface SpecialtySectorForm {
	specialtySector: FormControl<EmergencyCareClinicalSpecialtySectorDto>;
}
