import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { EViolenceTowardsUnderageType, SnomedDto, SnomedECL, ViolenceEpisodeDetailDto } from '@api-rest/api-model';
import { hasError, updateControlValidator } from '@core/utils/form.utils';
import { ViolenceSituationsNewConsultationService } from '../../services/violence-situations-new-consultation.service';
import { ViolenceModalityNewConsultationService } from '../../services/violence-modality-new-consultation.service';
import { Observable } from 'rxjs';
import { BasicOptions, EscolarizationLevels, RiskLevels, ViolenceTypes } from '../../constants/violence-masterdata';
import { dateToDateDto } from '@api-rest/mapper/date-dto.mapper';


@Component({
	selector: 'app-new-violence-episode-section',
	templateUrl: './new-violence-episode-section.component.html',
	styleUrls: ['./new-violence-episode-section.component.scss']
})
export class NewViolenceEpisodeSectionComponent implements OnInit {
	@Input() confirmForm: Observable<boolean>;
	@Output() violenceEpisodeInfo = new EventEmitter<any>();
	
	hasError = hasError;

	form: FormGroup;

	eclModality: SnomedECL = SnomedECL.VIOLENCE_MODALITY;
	eclType: SnomedECL = SnomedECL.VIOLENCE_TYPE;

	violenceSituations: SnomedDto[] = [];
	violenceModalities: SnomedDto[] = [];

	violenceTypeDirect = EViolenceTowardsUnderageType.DIRECT_VIOLENCE;
	violenceTypeIndirect = EViolenceTowardsUnderageType.INDIRECT_VIOLENCE;

	violenceTypes = ViolenceTypes;

	escolarizationLevels = EscolarizationLevels;

	riskLevels = RiskLevels;

	basicOptions = BasicOptions;
	markAsTouched = false;

	todayDate = new Date();

	constructor(private readonly violenceSituationService: ViolenceSituationsNewConsultationService,
				private readonly violenceModalityService: ViolenceModalityNewConsultationService) {}

	ngOnInit(): void {
		this.form = new FormGroup({
			episodeDate: new FormControl(null, Validators.required),
			ageTypeKid: new FormControl(null, Validators.required),
			isKidEscolarized: new FormControl(null),
			escolarizationLevel: new FormControl(null),
			riskLevelTest: new FormControl(null, Validators.required),
		});
	}

	ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			if(this.form.valid){
				this.violenceEpisodeInfo.emit(this.mapViolenceEpisode());
			}else{
				this.markAsTouched = true
				this.form.markAllAsTouched();
			}
		}
	}

	dateChanged(date: Date){
		this.form.controls.episodeDate.setValue(date)
	}

	addViolenceSituation(violenceConcept: SnomedDto) {
		this.violenceSituationService.addToList(violenceConcept);
		this.setViolenceSituations();
	}

	addViolenceModality(violenceConcept: SnomedDto) {
		this.violenceModalityService.addToList(violenceConcept);
		this.setViolenceModalities();
	}

	mapViolenceEpisode() : ViolenceEpisodeDetailDto {
		return {
			episodeDate : dateToDateDto(new Date(this.form.value.episodeDate)),
			riskLevel: this.form.value.riskLevelTest,
			violenceTowardsUnderage: {
				schoolLevel: this.form.value.escolarizationLevel,
				schooled: this.form.value.isKidEscolarized,
				type: this.form.value.ageTypeKid,
			},
			violenceModalitySnomedList: this.violenceModalities,
			violenceTypeSnomedList: this.violenceSituations,
		}
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

	updateValidationKidEscolarized(){
		if (this.form.value.isKidEscolarized) {
			updateControlValidator(this.form, 'escolarizationLevel', Validators.required);
		} else {
			updateControlValidator(this.form, 'escolarizationLevel', []);
			this.form.controls.escolarizationLevel.setValue(null);
		}
	}

	updateValidationAgeTypeKid() {
		if (this.form.value.ageTypeKid === EViolenceTowardsUnderageType.NO_INFORMATION || this.form.value.ageTypeKid === EViolenceTowardsUnderageType.NO_VIOLENCE) {
			this.form.controls.isKidEscolarized.reset();
			this.updateValidationKidEscolarized();
		}
	}
}
