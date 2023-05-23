import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { SipPlusUrlDataDto } from '@api-rest/api-model';
import { SipPlusMotherService } from '@api-rest/services/sip-plus-mother.service';
import { SipPlusPregnanciesService } from '@api-rest/services/sip-plus-pregnancies.service';
import { SipPlusService } from '@api-rest/services/sip-plus.service';
import { ContextService } from '@core/services/context.service';
import { NuevaConsultaDockPopupComponent } from '@historia-clinica/modules/ambulatoria/dialogs/nueva-consulta-dock-popup/nueva-consulta-dock-popup.component';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { NewGestationPopupComponent } from '../../dialogs/new-gestation-popup/new-gestation-popup.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { PREGNANCY_EVOLUTION, PREGNANCY_PROBLEM } from '../../constants/problem-information';
import { take } from 'rxjs';

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
	viewSip: boolean = false;
	pregnancies: any[];
	viewError: boolean = false;
	messageError: string;
	dischargeMother: boolean = false;

	private nuevaConsultaRef: DockPopupRef;

	constructor(private contextService: ContextService,
		private sanitizer: DomSanitizer,
		private sipPlusService: SipPlusService,
		public dialog: MatDialog,
		private sipPlusPregnanciesService: SipPlusPregnanciesService,
		private sipPlusMotherService: SipPlusMotherService,
		private dockPopupService: DockPopupService,
		private ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService
	) {
		this.institucionId = this.contextService.institutionId;
	}

	ngOnInit() {
		this.getPregnancies();
		this.sipPlusService.getUrlInfo().subscribe((sipUrlData: SipPlusUrlDataDto) => {
			this.urlBaseSip = sipUrlData.urlBase;
			this.tokenSIP = sipUrlData.token;
			this.embedSystem = sipUrlData.embedSystem;
			this.makeUrlTrusted()
		});
	}

	makeUrlTrusted(gestationId?: number) {
		let url = this.urlBaseSip + '?embedSystem=' + `${this.embedSystem}&embedToken=` + `${this.tokenSIP}$` + `${this.institucionId}$` + `${this.patientId}`;
		if (gestationId) {
			url += '$' + gestationId;
		}
		this.trustedUrlSIP = this.sanitizer.bypassSecurityTrustResourceUrl(url);
	}

	getPregnancies() {
		this.sipPlusPregnanciesService.getPregnancies(this.patientId).subscribe({
			next: data => {
				if (data.length) {
					this.pregnancies = data;
				} else {
					this.viewError = true;
				}

			},
			error: error => {
				this.messageError = error.text;
				if (error.code === 'NOT_FOUND') {
					this.dischargeMother = true;
					this.messageError = null;
					this.viewError = true;
				} else {
					this.dischargeMother = false;
				}
			}
		});
	}

	viewGestation(gestationId: number) {
		this.makeUrlTrusted(gestationId);
		this.viewSip = true;
		this.openNuevaConsulta();
	}

	backViewGestations() {
		this.viewSip = false;
	}

	newGestation() {
		const dialogRef = this.dialog.open(NewGestationPopupComponent, {
			disableClose: true,
			width: '30%',
			data: this.pregnancies,
		});

		dialogRef.afterClosed().subscribe(result => {
			if (result) {
				if (this.dischargeMother) {
					this.sipPlusMotherService.createMother(this.patientId, result).subscribe(res => {
						this.getPregnancies();
						this.viewGestation(result);
						this.viewError = false;
						this.messageError = null;
						this.dischargeMother = false;

					})
				} else {
					this.sipPlusPregnanciesService.createPregnancy(this.patientId, result).subscribe(res => {
						this.getPregnancies();
						this.viewError = false;
						this.viewGestation(result);
					})
				}
			}
		});
	}

	openNuevaConsulta() {
		this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(true);
		this.nuevaConsultaRef = this.dockPopupService.open(NuevaConsultaDockPopupComponent, {
			idPaciente: this.patientId,
			problem: PREGNANCY_PROBLEM,
			evolution: PREGNANCY_EVOLUTION
		});
		this.nuevaConsultaRef.minimize();
		this.nuevaConsultaRef.afterClosed().pipe(take(1)).subscribe(fieldsToUpdate => {
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
			this.ambulatoriaSummaryFacadeService.setIsNewConsultationOpen(false);
			delete this.nuevaConsultaRef;
		});
	}
}
