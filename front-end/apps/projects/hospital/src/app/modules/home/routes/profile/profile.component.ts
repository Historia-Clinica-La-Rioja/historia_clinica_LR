import { Component } from '@angular/core';
import { Router } from "@angular/router";
import { MatDialog } from "@angular/material/dialog";
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { FeatureFlagService } from "@core/services/feature-flag.service";

import { InstitutionDto, RoleAssignmentDto } from '@api-rest/api-model';
import { AppFeature } from '@api-rest/api-model';
import { TwoFactorAuthenticationService } from "@api-rest/services/two-factor-authentication.service";
import { InstitutionService } from '@api-rest/services/institution.service';
import { SnackBarService } from "@presentation/services/snack-bar.service";

import { LoggedUserService } from '../../../auth/services/logged-user.service';
import { ActivateTwoFactorAuthenticationComponent } from "../../dialogs/activate-two-factor-authentication/activate-two-factor-authentication.component";

@Component({
	selector: 'app-profile',
	templateUrl: './profile.component.html',
	styleUrls: ['./profile.component.scss']
})
export class ProfileComponent {
	roleAssignments$: Observable<{ label: string, institution?: {name: string} }[]>;

	twoFactorAuthenticationFFEnabled = false;
	twoFactorAuthenticationEnabledForUser = false;

	constructor(
		institutionService: InstitutionService,
		loggedUserService: LoggedUserService,
		private router: Router,
		private readonly dialog: MatDialog,
		private readonly snackBarService: SnackBarService,
		private readonly twoFactorAuthenticationService: TwoFactorAuthenticationService,
		private readonly featureFlagService: FeatureFlagService,
	) {
		this.roleAssignments$ = loggedUserService.assignments$.pipe(
			mergeMap((roleAssignments: RoleAssignmentDto[]) =>
				institutionService.getInstitutions(roleAssignments.map(roleAssignment => roleAssignment.institutionId))
					.pipe(
						map((institutions: InstitutionDto[]) => roleAssignments.map(roleAssignment => ({
							label: `auth.roles.names.${roleAssignment.role}`,
							institution: institutions.find(institution => institution.id === roleAssignment.institutionId),
						})))
					),
			),
		);
		this.featureFlagService.isActive(AppFeature.HABILITAR_2FA)
			.subscribe(isOn => this.twoFactorAuthenticationFFEnabled = isOn);
		this.twoFactorAuthenticationService.loggedUserHasTwoFactorAuthenticationEnabled()
			.subscribe((enabled) => this.twoFactorAuthenticationEnabledForUser = enabled);
	}

	updatePassword(): void {
		this.router.navigate(['home/update-password']);
	}

	openTwoFactorAuthenticationDialog() {
		const dialogRef = this.dialog.open(ActivateTwoFactorAuthenticationComponent, {
			width: '40%'
		});

		dialogRef.afterClosed().subscribe((result) => {
			if (result?.confirmed) {
				this.snackBarService.showSuccess('profile.two-factor-authentication.ENABLE_2FA_SUCCESS')
				this.twoFactorAuthenticationEnabledForUser = true;
			}
		});
	}
}
