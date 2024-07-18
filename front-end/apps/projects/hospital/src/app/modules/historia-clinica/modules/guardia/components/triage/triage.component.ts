import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { DoctorsOfficeDto, ERole, OutpatientReasonDto, TriageListDto } from '@api-rest/api-model';
import { TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { Observable, forkJoin, take } from 'rxjs';
import { SECTOR_AMBULATORIO, TRIAGE_LEVEL_V_ID, Triages } from '../../constants/masterdata';
import { PermissionsService } from '@core/services/permissions.service';
import { ToFormGroup } from '@core/utils/form.utils';
import { toOutpatientReasons } from '../../utils/mapper';
import { MotivoConsulta } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { MatSelectChange } from '@angular/material/select';
import { ActivatedRoute } from '@angular/router';

const ROLE_ALLOWED_NOT_TO_DEFINE_TRIAGE_LEVEL = [ERole.ADMINISTRATIVO];
const WITHOUT_TRIAGE_CATEGORY_ID = 6;

@Component({
	selector: 'app-triage',
	templateUrl: './triage.component.html',
	styleUrls: ['./triage.component.scss']
})
export class TriageComponent implements OnInit {

	triageForm = this.buildTriageForm();
	triageLevel: TriageCategory;
	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	triageCategories: TriageCategory[];
	lastTriageReasons: MotivoConsulta[] = [];

	patientId: number;
	patientDescription: string;

	@Input() cancelFunction: () => void;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() set lastTriage(lastTriage: TriageListDto) {
		if (lastTriage) {
			this.preloadedLastTriageData(lastTriage);
		}
	};
	@Output() triageData = new EventEmitter<Triage>();

	constructor(
		private doctorsOfficeService: DoctorsOfficeService,
		private triageMasterDataService: TriageMasterDataService,
		private permissionsService: PermissionsService,
		private readonly route: ActivatedRoute,
	) { }

	ngOnInit(): void {
		this.subscribeToFormChanges();

		const hasValidRoleToNotDefineTriageLevel$ = this.permissionsService.hasContextAssignments$(ROLE_ALLOWED_NOT_TO_DEFINE_TRIAGE_LEVEL).pipe(take(1));
		const triageCategories$ = this.triageMasterDataService.getCategories();

		forkJoin([hasValidRoleToNotDefineTriageLevel$, triageCategories$]).subscribe(([hasValidRoleToNotDefineTriageLevel, triageCategories]) => {
			this.triageCategories = triageCategories;
			if (hasValidRoleToNotDefineTriageLevel && this.canAssignNotDefinedTriageLevel) {
				this.triageForm.controls.triageCategoryId.setValue(WITHOUT_TRIAGE_CATEGORY_ID);
				this.triageLevel = this.triageCategories.find(category => category.id === WITHOUT_TRIAGE_CATEGORY_ID);
			}
			else {
				this.triageCategories = this.triageCategories.filter(category => category.id != WITHOUT_TRIAGE_CATEGORY_ID);
				this.triageLevel = this.triageCategories.find(category => category.id === TRIAGE_LEVEL_V_ID);
				this.triageForm.controls.triageCategoryId.setValue(this.triageLevel.id);
			}
		});

		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);

		this.route.queryParams.pipe(take(1)).subscribe(param => {
            if (param.patientId) this.patientId = (Number(param.patientId));
            else if (param.patientDescription) this.patientDescription = param.patientDescription;
        });
	}

	clear(control: AbstractControl) {
		control.reset();
	}

	setSelectedReasons(reasons: MotivoConsulta[]) {
		const outpatientReasons = toOutpatientReasons(reasons);
		this.triageForm.controls.reasons.setValue(outpatientReasons);
	}

	changeTriageLevel(triageLevelSelected: MatSelectChange) {
		this.triageLevel = this.triageCategories.find(category => category.id === triageLevelSelected.value);
	}

	private subscribeToFormChanges() {
		this.triageForm.valueChanges.subscribe(() => this.setTriageAndEmit());
	}

	private setTriageAndEmit() {
		const triage = this.buildTriage();
		this.triageData.emit(triage);
	}

	private buildTriage(): Triage {
		const { triageCategoryId, doctorsOfficeId, reasons } = this.triageForm.value;
		return { triageCategoryId, doctorsOfficeId, reasons };
	}

	private buildTriageForm(): FormGroup<ToFormGroup<Triage>> {
		return new FormGroup<ToFormGroup<Triage>>({
			triageCategoryId: new FormControl<number>(null, Validators.required),
			doctorsOfficeId: new FormControl<number>(null),
			reasons: new FormControl<OutpatientReasonDto[]>(null)
		});
	}

	private preloadedLastTriageData(lastTriage: TriageListDto) {
		this.lastTriageReasons = lastTriage.reasons;
		const preloadedTriageCategotyId = this.triageCategories.find(triageCategory => triageCategory.id === lastTriage.category.id).id;
		this.triageForm.controls.triageCategoryId.setValue(preloadedTriageCategotyId);
	}

}
export interface Triage {
	triageCategoryId: Triages;
	doctorsOfficeId: number;
	reasons: OutpatientReasonDto[];
}
