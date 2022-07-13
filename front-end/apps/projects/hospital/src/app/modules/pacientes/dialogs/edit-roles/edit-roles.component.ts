import { Component, OnInit, Inject } from '@angular/core';
import { FormArray, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RoleDto, UserRoleDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { hasError } from '@core/utils/form.utils';

const ROLES_NOT_NEED_ASSOCIATED_PROFESSION = '12|13|14|15|16|5|9';

@Component({
	selector: 'app-edit-roles',
	templateUrl: './edit-roles.component.html',
	styleUrls: ['./edit-roles.component.scss']
})
export class EditRolesComponent implements OnInit {
	formParent: FormGroup = new FormGroup({});
	roles: RoleDto[] = [];
	hasError = hasError;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: { personId: number; isProfessional: boolean; roles: RoleDto[]; userId: number; rolesByUser: UserRoleDto[] },
		private contextService: ContextService,
		private dialog: MatDialogRef<EditRolesComponent>

	) {

	}

	ngOnInit(): void {
		this.roles = this.data.roles;
		this.initFormParent();
		this.initFormRole();
		this.addOwnRoles(this.data.rolesByUser);
	}

	private initOwnFormRoles(elem: UserRoleDto): FormGroup {
		return new FormGroup({
			institutionId: new FormControl(elem.institutionId),
			roleDescription: new FormControl(elem.roleDescription),
			roleId: new FormControl(elem.roleId, [Validators.pattern(ROLES_NOT_NEED_ASSOCIATED_PROFESSION)]),
			userId: new FormControl(elem.userId)
		});
	}

	addOwnRoles(userRoles: UserRoleDto[]): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		if (userRoles.length === 0)
			this.addNewRole();
		else
			userRoles.forEach((elem: UserRoleDto) => {
				refRoles.push(this.initOwnFormRoles(elem));
			});
	}

	private initFormParent(): void {
		this.formParent = new FormGroup({
			roles: new FormArray([], [Validators.required])
		});
	}
	addNewRole(): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		refRoles.push(this.initFormRole());
	}

	private initFormRole(): FormGroup {
		return new FormGroup({
			institutionId: new FormControl(this.contextService.institutionId),
			roleDescription: new FormControl(null),
			roleId: new FormControl(null, [Validators.pattern(ROLES_NOT_NEED_ASSOCIATED_PROFESSION)]),
			userId: new FormControl(this.data.userId)
		});
	}
	private removeValidation(key: string, index: number,): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refSingle = refRoles.at(index).get(key) as FormGroup;

		refSingle.clearValidators();
		refSingle.updateValueAndValidity();
	}
	getCtrl(key: string, form: FormGroup): any {
		return form.get(key);
	}
	isDisableConfirmButton(): boolean {
		return this.isDisabledAddRole();
	}
	isDisabledAddRole(): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		const refArray = refRoles.controls;
		const i = refArray.length - 1;
		return refArray[i]?.value?.roleId <= 0;
	}
	clear(i: number): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		refRoles.removeAt(i);
	}
	hasValue(i: number): boolean {
		const refRoles = this.formParent.get('roles') as FormArray;
		return refRoles.at(i).value.roleId > 0;
	}

	needsValidation(control: string, index: number): boolean {
		if (this.data.isProfessional) {
			this.removeValidation(control, index);
			return true;
		}
		return false;
	}
	save(): void {
		const refRoles = this.formParent.get('roles') as FormArray;
		const userRoles: UserRoleDto[] = refRoles.value;
		this.dialog.close(userRoles);
	}

}
