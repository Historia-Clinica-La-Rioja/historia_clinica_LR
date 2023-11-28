import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AppFeature, ProfessionalDto } from '@api-rest/api-model';
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
	@Input() professionals: ProfessionalDto[];
	@Output() professionalChange = new EventEmitter();

	professional: ProfessionalDto;

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

	private getFullNameByFF(professional: ProfessionalDto): string {
		const firstName = (professional.middleNames) ? professional.firstName + " " + professional.middleNames : professional.firstName;
		const nameSelfDetermination = (professional.nameSelfDetermination) ? professional.nameSelfDetermination : firstName;
		const lastName = (professional.otherLastNames) ? professional.lastName + " " + professional.otherLastNames : professional.lastName;
		return (this.nameSelfDeterminationFF) ? lastName + ", " + nameSelfDetermination : lastName + ", " + firstName;
	}

	setProfessional(professional: number): void {
		this.professional = this.professionals.find(p => p.id === professional);
		this.professionalChange.emit(this.professional);
	}
}
