import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppFeature, HCEHealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
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
	@Input() professionals: ProfessionalDto[];
	@Input() externalSetProfessional: HCEHealthcareProfessionalDto;
	@Output() professionalChange = new EventEmitter();

	professional: ProfessionalDto;
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
		this.professionalsTypeAhead = this.professionals?.map(professional => this.mapToTypeaheadOption(this.getFullName(professional), professional.id));
		if (this.externalSetProfessional) {
			this.identificationNumber = this.externalSetProfessional.person.identificationNumber || NO_INFO;
			this.licenseNumber = this.externalSetProfessional.licenseNumber || NO_INFO;
			this.externalSetValue = this.professionalsTypeAhead.find(p => p.value === this.externalSetProfessional.id);
		}
	}

	private getFullName(professional: ProfessionalDto): string {
		let fullName = professional.lastName;
		if (professional.otherLastNames && professional.otherLastNames.trim() !== '')
			fullName += " " + professional.otherLastNames;
		if (this.nameSelfDeterminationFF && professional.nameSelfDetermination && professional.nameSelfDetermination.trim() !== '')
			fullName += ", " + professional.nameSelfDetermination;
		else
			fullName += ", " + professional.firstName;
		if (!this.nameSelfDeterminationFF && professional.middleNames && professional.middleNames.trim() !== '')
			fullName += " " + professional.middleNames;
		return fullName;
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
			this.identificationNumber = this.professional?.identificationNumber || NO_INFO;
			this.licenseNumber = this.professional?.licenceNumber || NO_INFO;
		}
		else {
			this.identificationNumber = '';
			this.licenseNumber = '';
		}
		this.professionalChange.emit(this.mapToHCEHealthcareProfessionalDto(this.professional));
	}

	private mapToHCEHealthcareProfessionalDto(professional: ProfessionalDto): HCEHealthcareProfessionalDto {
		if (!professional)
			return null;
		else
			return {
				id: professional.id,
				licenseNumber: professional.licenceNumber,
				person: {
					birthDate: null,
					fullName: this.getFullName(professional),
					id: professional.id,
					identificationNumber: professional.identificationNumber
				}
			}
	}
}
