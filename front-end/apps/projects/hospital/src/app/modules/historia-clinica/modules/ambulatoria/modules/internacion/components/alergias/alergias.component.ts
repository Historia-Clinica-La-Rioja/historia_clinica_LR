import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AllergyConditionDto, AppFeature, SnomedDto, SnomedECL } from '@api-rest/api-model';
import { pushIfNotExists, removeFrom } from '@core/utils/array.utils';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ConceptsList } from 'projects/hospital/src/app/modules/hsi-components/concepts-list/concepts-list.component';
import { AlergiasNuevaConsultaService } from '@historia-clinica/modules/ambulatoria/services/alergias-nueva-consulta.service';
import { UntypedFormBuilder } from '@angular/forms';
import { SnomedService } from '@historia-clinica/services/snomed.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { InternacionMasterDataService } from '@api-rest/services/internacion-master-data.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { SearchSnomedConceptComponent } from '@historia-clinica/modules/ambulatoria/dialogs/search-snomed-concept/search-snomed-concept.component';

@Component({
	selector: 'app-alergias',
	templateUrl: './alergias.component.html',
	styleUrls: ['./alergias.component.scss']
})
export class AlergiasComponent {


	@Output() allergiesChange = new EventEmitter();
	@Input() allergies: AllergyConditionDto[] = [];
	@Output() isAllergyNoRefer = new EventEmitter<boolean>();
	alergiasNuevaConsultaService: AlergiasNuevaConsultaService;
	emptyAllergies: boolean = true;

	allergyContent: ConceptsList = {
		id: 'allergy-checkbox-concepts-list',
		header: {
			text: 'ambulatoria.paciente.nueva-consulta.alergias.TITLE',
			icon: 'cancel'
		},
		titleList: 'ambulatoria.paciente.nueva-consulta.alergias.table.TITLE',
		actions: {
			button: 'ambulatoria.paciente.nueva-consulta.alergias.ADD',
			checkbox: 'ambulatoria.paciente.nueva-consulta.alergias.NO_REFER',
		}
	}
	searchConceptsLocallyFFIsOn: boolean = false;

	constructor(
		formBuilder: UntypedFormBuilder,
		private readonly dialog: MatDialog,
		private readonly snomedService: SnomedService,
		private readonly snackBarService: SnackBarService,
		private readonly internacionMasterDataService: InternacionMasterDataService,
		private readonly featureFlagService: FeatureFlagService
	) {
		this.setFeatureFlags();
		this.alergiasNuevaConsultaService = new AlergiasNuevaConsultaService(formBuilder, this.snomedService, this.snackBarService, this.internacionMasterDataService);
	}

	addSnomedConcept(snomedConcept: SnomedDto) {
		if (snomedConcept) {
			const alergia: AllergyConditionDto = {
				categoryId: null,
				date: null,
				verificationId: null,
				id: null,
				snomed: snomedConcept,
				criticalityId: null,
				statusId: null
			};
			this.add(alergia);
		}
	}

	add(a: AllergyConditionDto) {
		const lenght = this.allergies?.length;
		this.allergies = pushIfNotExists<AllergyConditionDto>(this.allergies, a, this.compare);
		if (this.allergies.length > lenght) {
			this.allergiesChange.emit(this.allergies);
		}
	}

	compare(concept1: AllergyConditionDto, concept2: AllergyConditionDto): boolean {
		return concept1.snomed.sctid === concept2.snomed.sctid
	}

	remove(index: number) {
		this.allergies = removeFrom<AllergyConditionDto>(this.allergies, index);
		this.allergiesChange.emit(this.allergies);
	}

	addAlergies() {
		const dialogConfig = new MatDialogConfig();
		dialogConfig.width = '35%';
		dialogConfig.disableClose = false;
		dialogConfig.data = {
			label: 'internaciones.anamnesis.alergias.ALLERGY',
			title: 'internaciones.anamnesis.alergias.ADD',
			eclFilter: SnomedECL.ALLERGY
		};

		const dialogRef = this.dialog.open(SearchSnomedConceptComponent, dialogConfig);

		dialogRef.afterClosed().subscribe(snomedConcept => {
			if (snomedConcept) {
				this.addSnomedConcept(snomedConcept)
				this.alergiasNuevaConsultaService.add({snomed: snomedConcept, criticalityId: null});
			}
		});
	}

	checkAllergyEvent($event) {
		if ($event.addPressed) {
			this.addAlergies();
		}
		this.isAllergyNoRefer.emit(!$event.checkboxSelected);
	}

	private setFeatureFlags = () => {
		this.featureFlagService.isActive(AppFeature.HABILITAR_BUSQUEDA_LOCAL_CONCEPTOS).subscribe(isOn => {
			this.searchConceptsLocallyFFIsOn = isOn;
		});
	}
}
