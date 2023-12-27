import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppFeature, HCEHealthcareProfessionalDto, HealthcareProfessionalDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

const NO_INFO = 'Sin información';

@Component({
	selector: 'app-surgical-report-professional-info',
	templateUrl: './surgical-report-professional-info.component.html',
	styleUrls: ['./surgical-report-professional-info.component.scss']
})
export class SurgicalReportProfessionalInfoComponent implements OnInit {

	@Input() professionalTitle: string;
	@Input() professionals: HealthcareProfessionalDto[];
	@Input() externalSetProfessional: HCEHealthcareProfessionalDto;
	@Output() professionalChange = new EventEmitter();

	professional: HealthcareProfessionalDto;
	nameSelfDeterminationFF: boolean;
	professionalsTypeAhead: TypeaheadOption<any>[];
	identificationNumber: string = '';
	licenseNumber: string = '';
	externalSetValue: TypeaheadOption<any>;

	constructor(
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {
		this.professionalsTypeAhead = this.professionals?.map(professional => this.mapToTypeaheadOption(this.getFullNameByFF(professional), professional.id));
		if (this.externalSetProfessional){
			this.identificationNumber = this.externalSetProfessional.person.identificationNumber || NO_INFO;
			this.licenseNumber = this.externalSetProfessional.licenseNumber || NO_INFO;
			this.externalSetValue = this.professionalsTypeAhead.find(p => p.value === this.externalSetProfessional.id);
		}
	}

	private getFullNameByFF(professional: HealthcareProfessionalDto): string {
		const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : professional.person.firstName;
		return (this.nameSelfDeterminationFF) ?
			professional.person.lastName + ", " + nameSelfDetermination :
			professional.person.lastName + ", " + professional.person.firstName;
	}

	private mapToTypeaheadOption(professionalName: string, professionalId: number): TypeaheadOption<any> {
		return {
			compareValue: professionalName,
			value: professionalId,
			viewValue: professionalName
		}
	}

	setProfessional(professional: number): void {
		this.professional = this.professionals.find(p => p.id === professional);
		if (this.professional) {
			this.identificationNumber = this.professional?.person.identificationNumber || NO_INFO;
			this.licenseNumber = this.professional?.licenseNumber || NO_INFO;
		}
		else {
			this.identificationNumber = '';
			this.licenseNumber = '';
		}
		this.professionalChange.emit(this.mapToHCEHealthcareProfessionalDto(this.professional));
	}

	private mapToHCEHealthcareProfessionalDto(professional: HealthcareProfessionalDto): HCEHealthcareProfessionalDto {
		if (!professional)
			return null;
		else
			return {
				id: professional.id,
				licenseNumber: professional.licenseNumber,
				person: {
					birthDate: professional.person.birthDate,
					fullName: this.getFullNameByFF(professional),
					id: professional.personId,
					identificationNumber: professional.person.identificationNumber
				}
			}
	}
}
