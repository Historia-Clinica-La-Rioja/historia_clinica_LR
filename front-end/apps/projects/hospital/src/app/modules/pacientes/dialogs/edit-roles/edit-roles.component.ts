import { Component, OnInit, Inject } from '@angular/core';
import { UntypedFormArray, UntypedFormControl, UntypedFormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { RoleDto, UserRoleDto } from '@api-rest/api-model';
import { ContextService } from '@core/services/context.service';
import { hasError } from '@core/utils/form.utils';

const ROLES_NOT_NEED_ASSOCIATED_PROFESSION = '8|12|13|14|15|16|5|9|22|23';

@Component({
	selector: 'app-edit-roles',
	templateUrl: './edit-roles.component.html',
	styleUrls: ['./edit-roles.component.scss']
})
export class EditRolesComponent implements OnInit {
	formParent: UntypedFormGroup = new UntypedFormGroup({});
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

	private initOwnFormRoles(elem: UserRoleDto): UntypedFormGroup {
		return new UntypedFormGroup({
			institutionId: new UntypedFormControl(elem.institutionId),
			roleDescription: new UntypedFormControl(elem.roleDescription),
			roleId: new UntypedFormControl(elem.roleId, [Validators.pattern(ROLES_NOT_NEED_ASSOCIATED_PROFESSION)]),
			userId: new UntypedFormControl(elem.userId)
		});
	}

	addOwnRoles(userRoles: UserRoleDto[]): void {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		if (userRoles.length === 0)
			this.addNewRole();
		else
			userRoles.forEach((elem: UserRoleDto) => {
				refRoles.push(this.initOwnFormRoles(elem));
			});
	}

	private initFormParent(): void {
		this.formParent = new UntypedFormGroup({
			roles: new UntypedFormArray([], [Validators.required])
		});
	}
	addNewRole(): void {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		refRoles.push(this.initFormRole());
	}

	private initFormRole(): UntypedFormGroup {
		return new UntypedFormGroup({
			institutionId: new UntypedFormControl(this.contextService.institutionId),
			roleDescription: new UntypedFormControl(null),
			roleId: new UntypedFormControl(null, [Validators.pattern(ROLES_NOT_NEED_ASSOCIATED_PROFESSION)]),
			userId: new UntypedFormControl(this.data.userId)
		});
	}
	private removeValidation(key: string, index: number,): void {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		const refSingle = refRoles.at(index).get(key) as UntypedFormGroup;

		refSingle.clearValidators();
		refSingle.updateValueAndValidity();
	}
	getCtrl(key: string, form: UntypedFormGroup): any {
		return form.get(key);
	}
	isDisableConfirmButton(): boolean {
		return this.isDisabledAddRole();
	}
	isDisabledAddRole(): boolean {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		const refArray = refRoles.controls;
		const i = refArray.length - 1;
		return refArray[i]?.value?.roleId <= 0;
	}
	clear(i: number): void {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		refRoles.removeAt(i);
	}
	hasValue(i: number): boolean {
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
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
		const refRoles = this.formParent.get('roles') as UntypedFormArray;
		const userRoles: UserRoleDto[] = refRoles.value;
		this.dialog.close(userRoles);
	}

}
