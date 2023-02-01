import { Component, Input, OnInit } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SipPlusService } from '@api-rest/services/sip-plus.service';
import { ContextService } from '@core/services/context.service';

@Component({
	selector: 'app-clap',
	templateUrl: './clap.component.html',
	styleUrls: ['./clap.component.scss']
})
export class ClapComponent implements OnInit {
	@Input() patientId: Number;
	trustedUrlSIP: SafeResourceUrl;
	institucionId: Number;
	tokenSIP: String;
	urlBaseSip: String;

	constructor(private contextService: ContextService,
		private sanitizer: DomSanitizer,
		private sipPlusService: SipPlusService) {
		this.institucionId = this.contextService.institutionId;
	}

	ngOnInit(): void {
		this.sipPlusService.getInfoSipPlus().subscribe(data => {
			this.urlBaseSip = data.urlBase;
			this.tokenSIP = data.token;
			this.makeUrlTrusted()
		})
	}


	makeUrlTrusted() {
		const url = this.urlBaseSip + '?embedSystem=HSI&embedToken=' + `${this.tokenSIP}$` + `${this.institucionId}$` + `${this.patientId}`;
		this.trustedUrlSIP = this.sanitizer.bypassSecurityTrustResourceUrl(url);
	}
}
