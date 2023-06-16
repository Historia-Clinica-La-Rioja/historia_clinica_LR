import { AfterViewInit, Component } from '@angular/core';
import { UntypedFormArray, UntypedFormBuilder, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { EBirthCondition, EGender, NewbornDto, ObstetricEventDto } from '@api-rest/api-model';
import { ControlDynamicFormService } from '../../services/control-dynamic-form.service';
import { ObstetricFormService } from '../../services/obstetric-form.service';
import { TypeOfPregnancy } from '../type-of-pregnancy/type-of-pregnancy.component';

@Component({
	selector: 'app-form-dynamic-new-born',
	templateUrl: './form-dynamic-new-born.component.html',
	styleUrls: ['./form-dynamic-new-born.component.scss']
})
export class FormDynamicNewBornComponent implements AfterViewInit {

	BORN_ALIVE = EBirthCondition.BORN_ALIVE;
	FETAL_DEATH = EBirthCondition.FETAL_DEATH;

	FEMALE = EGender.FEMALE;
	MALE = EGender.MALE;
	X = EGender.X;
	selectedOption = 0;
	form: UntypedFormGroup;
	disabledAddNewBorn = true;
	formSend = false;

	constructor(
		private controlService: ControlDynamicFormService,
		private formBuilder: UntypedFormBuilder,
		private obstetricFormService: ObstetricFormService,
	) {

		this.form = this.formBuilder.group({
			newBorns: new UntypedFormArray([])
		});

		this.obstetricFormService.getValue().subscribe((obstetricEvent: ObstetricEventDto) => {
			const array = obstetricEvent?.newborns;
			array?.forEach((newborn: NewbornDto) => {
				this.addChild(newborn);
			});

		});


	}

	ngAfterViewInit(): void {
		this.controlService.selectedOption$.subscribe((option: TypeOfPregnancy) => {
			const newBornsFormArray = this.form.get('newBorns') as UntypedFormArray;
			if (option === TypeOfPregnancy.UNDEFINED) {
				newBornsFormArray.clear();
				this.disabledAddNewBorn = true;
			} else {
				this.formSend = false;
				this.form.reset();
				this.disabledAddNewBorn = option === TypeOfPregnancy.SIMPLE;
				const desiredLength = option === TypeOfPregnancy.SIMPLE ? 1 : 2;
				while (newBornsFormArray.length > desiredLength) {
					newBornsFormArray.removeAt(newBornsFormArray.length - 1);
				}
				while (newBornsFormArray.length < desiredLength) {
					this.addChild();
				}
			}
		});
	}

	addChild(newBorn?: NewbornDto) {
		const arrayBorns = this.form.get('newBorns') as UntypedFormArray;
		const addNewBorn = this.addNewBorn(newBorn);
		arrayBorns.push(addNewBorn);
	}

	delete(i: number) {
		const arrayBorns = this.form.get('newBorns') as UntypedFormArray;
		arrayBorns.removeAt(i);
	}

	getCtrl(key: string, form: UntypedFormGroup): any {
		return form.get(key);
	}

	isValidForm(): boolean {
		this.formSend = true;
		this.form.markAllAsTouched();
		return this.form.valid
	}

	getForm(): NewbornDto[] {
		this.form.markAllAsTouched();
		const array = this.form.get('newBorns') as UntypedFormArray;
		const newborns = array?.getRawValue();
		return this.form.valid ? newborns : null
	}

	private addNewBorn(newBorn?: NewbornDto): UntypedFormGroup {
		return this.formBuilder.group({
			birthConditionType: new UntypedFormControl(newBorn?.birthConditionType || null, Validators.required),
			weight: new UntypedFormControl(newBorn?.weight || null, Validators.required),
			genderId: new UntypedFormControl(newBorn?.genderId || null, Validators.required),
			id: new UntypedFormControl(newBorn?.id || null)
		});
	}

}
