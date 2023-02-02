import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SipPlusUrlDataDto } from '@api-rest/api-model';
import { SipPlusPregnanciesService } from '@api-rest/services/sip-plus-pregnancies.service';
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
	pregnancies: any[];
	viewError:boolean =false;

	constructor(private contextService: ContextService,
		private sanitizer: DomSanitizer,
		private sipPlusService: SipPlusService,
		public dialog: MatDialog,
		private sipPlusPregnanciesService: SipPlusPregnanciesService
		) {
		this.institucionId = this.contextService.institutionId;
	}

	ngOnInit() {
		this.getPregnancies();
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

	getPregnancies(){
		this.sipPlusPregnanciesService.getPregnancies(this.patientId).subscribe(data=>{
				this.pregnancies=data;
		},error=>{
			this.viewError=true;
		})
	}
	viewGestation(gestationId:number) {
		this.trustedUrlSIP + '$'+ gestationId;
		this.viewSip=true;
	}

	backViewGestations() {
		this.viewSip=false;
	}

	newGestation() {
		const dialogRef = this.dialog.open(NewGestationPopupComponent, {
			disableClose: true ,
			width: '30%',
			data: this.pregnancies,
		  });

		  dialogRef.afterClosed().subscribe(result => {
			if(result){
				this.sipPlusPregnanciesService.createPregnancy(this.patientId,result).subscribe(res=>{
					this.getPregnancies();
					this.viewError=false;
					this.viewGestation(result);
				})
			}
		  });

	}
}
