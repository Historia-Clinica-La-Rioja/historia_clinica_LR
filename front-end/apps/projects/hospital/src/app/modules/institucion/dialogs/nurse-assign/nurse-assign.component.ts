import { Component, OnInit, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ApiErrorMessageDto, BedInfoDto, BedNurseDto, PersonDataDto } from '@api-rest/api-model';
import { BedService } from '@api-rest/services/bed.service';
import { UserService } from '@api-rest/services/user.service';
import { TypeaheadOption } from '@presentation/components/typeahead/typeahead.component';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { map } from 'rxjs';

const NURSE_ROLE: number = 7;

@Component({
	selector: 'app-nurse-assign',
	templateUrl: './nurse-assign.component.html',
	styleUrls: ['./nurse-assign.component.scss']
})
export class NurseAssignComponent implements OnInit {

	nurses: TypeaheadOption<PersonDataDto>[];
	selectedNurseId: number;
	preloadedNurse: TypeaheadOption<BedNurseDto>;

	constructor(@Inject(MAT_DIALOG_DATA) public data: {bed: BedInfoDto},
				private readonly userService: UserService,
				private readonly bedService: BedService,
				private readonly snackBarService: SnackBarService,
				private dialogRef: MatDialogRef<NurseAssignComponent>) { }

	ngOnInit(): void {
		this.preloadNurse();
		this.setNurses();
	}

	private setNurses() {
		this.userService.getUsersByInstitutionRoles([NURSE_ROLE])
		.pipe(
			map(toTypeaheadOptionList)
		)
		.subscribe((nurses: TypeaheadOption<PersonDataDto>[]) => {
			this.nurses = nurses;
		})

		function toTypeaheadOptionList(nurses: PersonDataDto[]):
		TypeaheadOption<PersonDataDto>[] {
			return nurses.map(toTypeaheadOption);

			function toTypeaheadOption(person: PersonDataDto): TypeaheadOption<PersonDataDto> {
				return {
					compareValue: person.fullName,
					value: person
				};
			}
		}
	}

	private preloadNurse() {
		this.preloadedNurse = {
			compareValue: this.data.bed.bedNurse?.fullName,
			value: this.data.bed?.bedNurse
		}
		this.selectedNurseId = this.data.bed.bedNurse?.userId;
	}

	setNurse(nurse: PersonDataDto) {
		this.selectedNurseId = nurse?.userId;
	}

	saveAssignedNurse() {
		this.bedService.updateBedNurse(this.data.bed.bed.id, this.selectedNurseId)
		.subscribe({
			next: (_) => {
				this.dialogRef.close(true);
				this.snackBarService.showSuccess('gestion-camas.detail.more-actions.ASSIGN_SUCCESS');
			},
			error: (err: ApiErrorMessageDto) => {
				this.dialogRef.close();
				this.snackBarService.showError(err.text);
			}
		})
	}
}