import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { DoctorsOfficeDto, ERole, OutpatientReasonDto, TriageListDto } from '@api-rest/api-model';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { Observable, forkJoin, take } from 'rxjs';
import { SECTOR_AMBULATORIO, TRIAGE_LEVEL_V_ID } from '../../constants/masterdata';
import { PermissionsService } from '@core/services/permissions.service';
import { ToFormGroup } from '@core/utils/form.utils';
import { toOutpatientReasons } from '../../utils/mapper';
import { MotivoConsulta } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';

const ROLE_ALLOWED_NOT_TO_DEFINE_TRIAGE_LEVEL = [ERole.ADMINISTRATIVO];
const WITHOUT_TRIAGE_CATEGORY_ID = 6;

@Component({
	selector: 'app-triage',
	templateUrl: './triage.component.html',
	styleUrls: ['./triage.component.scss']
})
export class TriageComponent implements OnInit {

	triageForm = this.buildTriageForm();
	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	triageCategories: TriageCategoryDto[];
	lastTriageReasons: MotivoConsulta[] = [];

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
	) { }

	ngOnInit(): void {
		this.subscribeToFormChanges();

		const hasValidRoleToNotDefineTriageLevel$ = this.permissionsService.hasContextAssignments$(ROLE_ALLOWED_NOT_TO_DEFINE_TRIAGE_LEVEL).pipe(take(1));
		const triageCategories$ = this.triageMasterDataService.getCategories();

		forkJoin([hasValidRoleToNotDefineTriageLevel$, triageCategories$]).subscribe(([hasValidRoleToNotDefineTriageLevel, triageCategories]) => {
			this.triageCategories = triageCategories;
			if (hasValidRoleToNotDefineTriageLevel && this.canAssignNotDefinedTriageLevel)
				this.triageForm.controls.triageCategoryId.setValue(WITHOUT_TRIAGE_CATEGORY_ID);
			else {
				this.triageCategories = this.triageCategories.filter(category => category.id != WITHOUT_TRIAGE_CATEGORY_ID);
				this.triageForm.controls.triageCategoryId.setValue(this.triageCategories.find(category => category.id === TRIAGE_LEVEL_V_ID).id);
			}
		});

		this.doctorsOffices$ = this.doctorsOfficeService.getBySectorType(SECTOR_AMBULATORIO);
	}

	clear(control: AbstractControl) {
		control.reset();
	}

	setSelectedReasons(reasons: MotivoConsulta[]) {
		const outpatientReasons = toOutpatientReasons(reasons);
		this.triageForm.controls.reasons.setValue(outpatientReasons);
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
	triageCategoryId: number;
	doctorsOfficeId: number;
	reasons: OutpatientReasonDto[];
}
