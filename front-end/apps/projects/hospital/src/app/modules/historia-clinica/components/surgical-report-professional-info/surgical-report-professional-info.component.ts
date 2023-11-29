import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppFeature, HCEHealthcareProfessionalDto, HealthcareProfessionalDto } from '@api-rest/api-model';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';

@Component({
	selector: 'app-surgical-report-professional-info',
	templateUrl: './surgical-report-professional-info.component.html',
	styleUrls: ['./surgical-report-professional-info.component.scss']
})
export class SurgicalReportProfessionalInfoComponent implements OnInit {

	nameSelfDeterminationFF: boolean;
	professionalsTypeAhead: TypeaheadOption<any>[];
	@Input() professionalTitle: string;
	@Input() professionals: HealthcareProfessionalDto[];
	@Output() professionalChange = new EventEmitter();

	professional: HealthcareProfessionalDto;

	constructor(
		private readonly featureFlagService: FeatureFlagService
	) {
		this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
			this.nameSelfDeterminationFF = isOn
		});
	}

	ngOnInit(): void {
		this.professionalsTypeAhead = this.professionals?.map(professional => {
			const professionalName = this.getFullNameByFF(professional);
			return {
				compareValue: professionalName,
				value: professional.id,
				viewValue: professionalName
			}
		})
	}

	private getFullNameByFF(professional: HealthcareProfessionalDto): string {
		const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : professional.person.firstName;
		return (this.nameSelfDeterminationFF) ?
			professional.person.lastName + ", " + nameSelfDetermination :
			professional.person.lastName + ", " + professional.person.firstName;
	}

	setProfessional(professional: number): void {
		this.professional = this.professionals.find(p => p.id === professional);
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
