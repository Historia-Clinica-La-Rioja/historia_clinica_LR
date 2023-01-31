import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SipPlusUrlDataDto } from '@api-rest/api-model';
import { SipPlusService } from '@api-rest/services/sip-plus.service';
import { ContextService } from '@core/services/context.service';
import { NewGestationPopupComponent } from '../../dialogs/new-gestation-popup/new-gestation-popup.component';

@Component({
	selector: 'app-clap',
	templateUrl: './clap.component.html',
	styleUrls: ['./clap.component.scss']
})
export class ClapComponent implements OnInit {
	@Input() patientId: number;
	trustedUrlSIP: SafeResourceUrl;
	institucionId: number;
	tokenSIP: string;
	urlBaseSip: string;
	embedSystem: string;
	viewSip:boolean= false;
	gestations= [1,2,3];

	constructor(private contextService: ContextService,
		private sanitizer: DomSanitizer,
		private sipPlusService: SipPlusService,
		public dialog: MatDialog) {
		this.institucionId = this.contextService.institutionId;
	}

	ngOnInit() {
		this.sipPlusService.getUrlInfo().subscribe((sipUrlData : SipPlusUrlDataDto) => {
			this.urlBaseSip = sipUrlData.urlBase;
			this.tokenSIP = sipUrlData.token;
			this.embedSystem = sipUrlData.embedSystem;
			this.makeUrlTrusted()
		})
	}


	makeUrlTrusted() {
		const url = this.urlBaseSip + '?embedSystem='+`${this.embedSystem}&embedToken=`  + `${this.tokenSIP}$` + `${this.institucionId}$` + `${this.patientId}`;
		this.trustedUrlSIP = this.sanitizer.bypassSecurityTrustResourceUrl(url);
	}

	viewGestation(gestationId:number) {
		const url = this.trustedUrlSIP + '$'+ gestationId;
		this.trustedUrlSIP = this.sanitizer.bypassSecurityTrustResourceUrl(url);
		this.viewSip=true;
	}

	backViewGestations() {
		this.viewSip=false;
	}

	newGestation() {
		const dialogRef = this.dialog.open(NewGestationPopupComponent, {
			disableClose: true ,
			width: '30%',
		  });

		  dialogRef.afterClosed().subscribe(result => {
			if(result){
				this.viewSip=true;
				let aux = result;
			}
		  });

	}
}
