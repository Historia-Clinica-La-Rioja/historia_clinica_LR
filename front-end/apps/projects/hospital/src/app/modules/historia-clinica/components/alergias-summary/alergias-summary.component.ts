import { Component, Input } from '@angular/core';
import { AllergyConditionDto, HCEAllergyDto } from '@api-rest/api-model';
import { ALERGIAS } from '../../constants/summaries';
import { MatDialog } from '@angular/material/dialog';
import { AddAllergyComponent } from '../../dialogs/add-allergy/add-allergy.component';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { PatientAllergiesService } from '@historia-clinica/modules/ambulatoria/services/patient-allergies.service';
import { InternmentSummaryFacadeService } from "@historia-clinica/modules/ambulatoria/modules/internacion/services/internment-summary-facade.service";
import { forkJoin } from 'rxjs';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
@Component({
	selector: 'app-alergias-summary',
	templateUrl: './alergias-summary.component.html',
	styleUrls: ['./alergias-summary.component.scss']
})
export class AlergiasSummaryComponent {
	public readonly alergiasSummary = ALERGIAS;
	readonly LOWCRITICALITY = 1;
	readonly HIGHCRITICALITY = 2;
	allergies: Allergy[] = [];
	private criticalityMasterData = [];
	private categoryMasterData = [];
	@Input() internmentEpisodeId: number;
	@Input() editable = false;
	@Input() patientId: number;
	@Input()
	set allergiesDto(allergiesDto: HCEAllergyDto[] | AllergyConditionDto[]) {
		if (allergiesDto?.length) {
			this.setCategoryAndCriticallyAndAllergies(allergiesDto);
		}else{
			this.allergies = [];
		}
	}

	constructor(
		private readonly internmentStateService: InternmentStateService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		public dialog: MatDialog,
		private readonly patientAllergies: PatientAllergiesService,
		private readonly internmentSummaryFacadeService: InternmentSummaryFacadeService,
	) {
	}

	openDialog() {
		const dialogRef = this.dialog.open(AddAllergyComponent, {
			disableClose: true,
			width: '35%',
			data: {
				internmentEpisodeId: this.internmentEpisodeId
			}
		});
		dialogRef.afterClosed().subscribe((allergies: AllergyConditionDto[]) => {
			if (allergies) {
				this.internmentStateService.getAllergies(this.internmentEpisodeId)
					.subscribe(allergies => this.setAllergies(allergies));
				this.patientAllergies.updateCriticalAllergies(this.patientId);
				if (this.internmentEpisodeId)
					this.internmentSummaryFacadeService.unifyAllergies(this.patientId);
			}
		});
	}

	setAllergies(allergiesDto: HCEAllergyDto[] | AllergyConditionDto[]) {
		this.allergies = allergiesDto.map(allergy => {
			return this.mapToAllergy(allergy);
		});
	}

	private getCriticalityDisplayName(criticalityId): string {
		return (criticalityId && this.criticalityMasterData) ? this.criticalityMasterData.find(c => c.id === criticalityId).display : '';
	}

	private getCriticalityColor(criticalityId): string {
		if (criticalityId === this.LOWCRITICALITY) {
			return 'grey';
		}
		if (criticalityId === this.HIGHCRITICALITY) {
			return 'warn';
		}
	}

	private getCategoryDisplayName(categoryId): string {
		return (categoryId && this.categoryMasterData) ? this.categoryMasterData.find(c => c.id === categoryId).display : '';
	}

	private setCategoryAndCriticallyAndAllergies(allergiesDto: HCEAllergyDto[] | AllergyConditionDto[]) {
		if (!this.categoryMasterData.length || !this.criticalityMasterData.length) {
			const categories$ = this.internacionMasterDataService.getAllergyCategories();
			const criticalities$ = this.internacionMasterDataService.getAllergyCriticality();
			forkJoin([categories$, criticalities$]).subscribe(masterdataInfo => {
				this.categoryMasterData = masterdataInfo[0];
				this.criticalityMasterData = masterdataInfo[1];
				this.setAllergies(allergiesDto);
			});
		}
		else
			this.setAllergies(allergiesDto);
	}

	private mapToAllergy(allergy: HCEAllergyDto | AllergyConditionDto): Allergy {
		return {
			data: allergy,
			categoryName: this.getCategoryDisplayName(allergy.categoryId),
			criticality: this.getCriticalityDisplayName(allergy.criticalityId),
			color: this.getCriticalityColor(allergy.criticalityId)
		}
	}
}
interface Allergy {
	data: HCEAllergyDto | AllergyConditionDto;
	categoryName: string;
	criticality: string;
	color: string;
}