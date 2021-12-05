import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root'
})
export class NavigationService {

	public currentUrl: string = null;
	constructor() { }

	private delete(): void {
		this.currentUrl = null;
	}

	public resetURL(){
		this.delete();
	}
	public goBackFromNewPatient(): boolean {
		if ((this.currentUrl != null) && (this.currentUrl.toString().indexOf('/new') != -1)) {
			this.delete();
			return true;
		}
		return false;
	}

	public saveURL(currentUrl: string): void {
		this.currentUrl = currentUrl;
	}
}
