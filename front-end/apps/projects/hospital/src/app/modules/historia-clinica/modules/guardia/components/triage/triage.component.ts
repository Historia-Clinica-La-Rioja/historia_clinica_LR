import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { DoctorsOfficeService } from '@api-rest/services/doctors-office.service';
import { DoctorsOfficeDto, ERole, OutpatientReasonDto } from '@api-rest/api-model';
import { TriageCategoryDto, TriageMasterDataService } from '@api-rest/services/triage-master-data.service';
import { Observable, combineLatest } from 'rxjs';
import { SECTOR_AMBULATORIO, TRIAGE_LEVEL_V_ID } from '../../constants/masterdata';
import { PermissionsService } from '@core/services/permissions.service';
import { MotivoConsulta } from '@historia-clinica/modules/ambulatoria/services/motivo-nueva-consulta.service';
import { ToFormGroup } from '@core/utils/form.utils';
import { toOutpatientReasons } from '../../utils/mapper';

const WITHOUT_TRIAGE_LEVEL_NOT_VALID_ROLES: ERole[] = [ERole.ENFERMERO, ERole.ESPECIALISTA_MEDICO, ERole.PROFESIONAL_DE_SALUD, ERole.ESPECIALISTA_EN_ODONTOLOGIA];
const WITHOUT_TRIAGE_CATEGORY_ID = 6;

@Component({
	selector: 'app-triage',
	templateUrl: './triage.component.html',
	styleUrls: ['./triage.component.scss']
})
export class TriageComponent implements OnInit {

	triageForm: FormGroup<ToFormGroup<Triage>>;
	doctorsOffices$: Observable<DoctorsOfficeDto[]>;
	triageCategories: TriageCategoryDto[];

	@Input() cancelFunction: () => void;
	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Output() triageData = new EventEmitter<Triage>();

	constructor(
		private doctorsOfficeService: DoctorsOfficeService,
		private triageMasterDataService: TriageMasterDataService,
		private permissionsService: PermissionsService,
	) { }

	ngOnInit(): void {
		this.triageForm = this.buildTriageForm();
		this.subscribeToFormChanges();

		let role$ = this.permissionsService.hasContextAssignments$(WITHOUT_TRIAGE_LEVEL_NOT_VALID_ROLES);
		let triages$ = this.triageMasterDataService.getCategories();

		combineLatest([role$, triages$]).subscribe(([hasntTriageNotDefinedRole, triageCategories]) => {
			this.triageCategories = triageCategories;
			if (!hasntTriageNotDefinedRole && this.canAssignNotDefinedTriageLevel)
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

}
export interface Triage {
	triageCategoryId: number;
	doctorsOfficeId: number;
	reasons: OutpatientReasonDto[];
}
