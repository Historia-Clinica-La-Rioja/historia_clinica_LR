import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { SnomedDto, SnomedECL } from '@api-rest/api-model';
import { hasError } from '@core/utils/form.utils';
import { Moment } from 'moment';
import { ViolenceSituationsNewConsultationService } from '../../services/violence-situations-new-consultation.service';
import { ViolenceModalityNewConsultationService } from '../../services/violence-modality-new-consultation.service';
import { BasicOption, FormOption } from '../violence-situation-person-information/violence-situation-person-information.component';

enum ViolenceType {
	DIRECT = 'Sí, es violencia directa contra NNyA',
	INDIRECT = 'Sí, es una violencia indirecta contra NNyA',
	NO = 'No',
	WITHOUT_DATA = 'Sin información',
}

enum EscolarizationLevel {
	MATERNAL = 'Maternal',
	INITIAL = 'Inicial',
	PRIMARY = 'Primario',
	SECONDARY = 'Secundario'
}

enum RiskLevel {
	LOW = 'Bajo',
	MEDIUM = 'Medio',
	HIGH = 'Alto'
}

@Component({
	selector: 'app-new-violence-episode-section',
	templateUrl: './new-violence-episode-section.component.html',
	styleUrls: ['./new-violence-episode-section.component.scss']
})
export class NewViolenceEpisodeSectionComponent implements OnInit {

	hasError = hasError;

	form: FormGroup<{
		episodeDate: FormControl<Moment>,
		ageTypeKid: FormControl<string>,
		isKidEscolarized: FormControl<boolean>,
		escolarizationLevel: FormControl<string>
		riskLevelTest: FormControl<string>
	}>;

	ecl: SnomedECL = SnomedECL.EVENT;

	violenceSituations: SnomedDto[] = [];
	violenceModalities: SnomedDto[] = [];

	violenceType = ViolenceType;

	violenceTypes: string[] = [ViolenceType.DIRECT, ViolenceType.INDIRECT, ViolenceType.NO, ViolenceType.WITHOUT_DATA];

	escolarizationLevels: string[] = [EscolarizationLevel.MATERNAL, EscolarizationLevel.INITIAL, EscolarizationLevel.PRIMARY, EscolarizationLevel.SECONDARY];

	riskLevels: string[]= [RiskLevel.LOW, RiskLevel.MEDIUM, RiskLevel.HIGH];

	basicOptions: BasicOption[] = [
		{
			text: FormOption.YES,
			value: true
		},
		{
			text: FormOption.NO,
			value: false
		},
		{
			text: FormOption.WITHOUT_DATA,
			value: null
		}
	]

	constructor(private readonly violenceSituationService: ViolenceSituationsNewConsultationService,
				private readonly violenceModalityService: ViolenceModalityNewConsultationService) {}

	ngOnInit(): void {
		this.form = new FormGroup({
			episodeDate: new FormControl(null, Validators.required),
			ageTypeKid: new FormControl(null, Validators.required),
			isKidEscolarized: new FormControl(null, Validators.required),
			escolarizationLevel: new FormControl(null, Validators.required),
			riskLevelTest: new FormControl(null, Validators.required),
		});
	}

	addViolenceSituation(violenceConcept: SnomedDto) {
		this.violenceSituationService.addToList(violenceConcept);
		this.setViolenceSituations();
	}

	addViolenceModality(violenceConcept: SnomedDto) {
		this.violenceModalityService.addToList(violenceConcept);
		this.setViolenceModalities();
	}

	get ageTypeKid() {
		return this.form.value.ageTypeKid;
	}

	get isKidEscolarized() {
		return this.form.value.isKidEscolarized;
	}

	private setViolenceSituations() {
		this.violenceSituationService.violenceSituations$
			.subscribe((concepts: SnomedDto[]) => this.violenceSituations = concepts);
	}

	private setViolenceModalities() {
		this.violenceModalityService.violenceModalities$
			.subscribe((concepts: SnomedDto[]) => this.violenceModalities = concepts);
	}

}
