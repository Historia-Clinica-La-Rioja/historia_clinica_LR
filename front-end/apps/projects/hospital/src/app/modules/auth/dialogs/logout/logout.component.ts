import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../services/authentication.service';
import { OauthAuthenticationService } from '../../services/oauth-authentication.service';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';
import { ButtonType } from '@presentation/components/button/button.component';
import { AvailableToLogoutStorageService , PendingItem} from '@core/services/available-to-logout-storage.service';
import { PendingTaskItem } from '../../components/pending-task-item/pending-task-item.component';

const LOGIN_ROUTE = '/auth/login'
@Component({
	selector: 'app-logout',
	templateUrl: './logout.component.html',
	styleUrls: ['./logout.component.scss']
})
export class LogoutComponent implements OnInit {

	pendings: Pending[];
	readonly forceLogoutButtonType = ButtonType.STROKED;
	readonly backButtonType = ButtonType.RAISED;

	constructor(
		private readonly customEventsPendingsService: AvailableToLogoutStorageService,
		private readonly authenticationService: AuthenticationService,
		private readonly oauthAuthenticationService: OauthAuthenticationService,
		private readonly router: Router,
		private readonly dialog: MatDialogRef<LogoutComponent>,
	) { }

	ngOnInit(): void {

	const pendings:  Map<string, PendingItem> = this.customEventsPendingsService.getPendings();
		this.pendings = this.toPendingList(pendings);
		if (!this.pendings.length) {
			this.logOut();
		}
	}

	private logOut() {
		this.authenticationService.logout().subscribe(_ => {
			this.router.navigate([LOGIN_ROUTE]);
		});
		this.oauthAuthenticationService.logout();
	}

	navigate(url: string) {
		this.dialog.close();
		this.router.navigate([url])
	}

	forceLogout() {
		this.logOut();
		this.dialog.close();
		this.customEventsPendingsService.restart();
	}

	private toPendingList(pendings:  Map<string, PendingItem>) {
		return Array.from(pendings.entries()).map(([extension, info]) => ({item: { extensionName: info.extensionName, emittedMessage: info.extensionMessage }, absolutUrl: info.absolutUrl}));
	}

}

interface Pending {
	item: PendingTaskItem
	absolutUrl: string;
}
