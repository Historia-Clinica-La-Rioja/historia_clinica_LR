import { Component, OnDestroy, ChangeDetectorRef, Input } from '@angular/core';
import { MediaMatcher } from '@angular/cdk/layout';
import { MenuItem } from '../menu/menu.component';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs';
import { LogoutService } from '@core/services/logout.service';

const MARGIN_LEFT_COLLAPSED = 94;
const MARGIN_LEFT_NOT_COLLAPSED = 194;

@Component({
	selector: 'app-main-layout',
	templateUrl: './main-layout.component.html',
	styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnDestroy {
	opened = false;
	mobileQuery: MediaQueryList;
	private _mobileQueryListener: () => void;
	private _menuItems: MenuItem[];
	isCollapsed = false;
	isCollapsedBolck = true;
	marginLeft = MARGIN_LEFT_NOT_COLLAPSED;


	constructor(changeDetectorRef: ChangeDetectorRef, media: MediaMatcher,
		private readonly logoutService: LogoutService,
		private readonly router: Router,
	) {
		this.mobileQuery = media.matchMedia('(max-width: 600px)');
		this._mobileQueryListener = () => changeDetectorRef.detectChanges();
		this.mobileQuery.addEventListener('change', this._mobileQueryListener);

		this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe((event: NavigationEnd) => {
			if (event.urlAfterRedirects === '/auth/login') {
				this.logoutService.run();
			}
		});
	}

	@Input() set menuItems(items: MenuItem[]) {
		this._menuItems = items;
		this.opened = (items && items.length) && !this.mobileQuery.matches;
	}

	get menuItems(): MenuItem[] {
		return this._menuItems;
	}

	ngOnDestroy(): void {
		this.mobileQuery.removeEventListener('change', this._mobileQueryListener);
	}

	logout(): void {
		this.logoutService.run();
	};

	toggleSidebarBlock() {
		this.isCollapsedBolck = !this.isCollapsedBolck;
		this.isCollapsed = false;
		this.marginLeft = MARGIN_LEFT_NOT_COLLAPSED;
	}

	toggleSidebarHoverOver() {
		if (!this.isCollapsedBolck) {
			this.isCollapsed = false;
			this.marginLeft = MARGIN_LEFT_NOT_COLLAPSED;
		}
	}

	toggleSidebarHoverOut() {
		if (!this.isCollapsedBolck) {
			this.isCollapsed = true;
			this.marginLeft = MARGIN_LEFT_COLLAPSED;
		}
	}
}
