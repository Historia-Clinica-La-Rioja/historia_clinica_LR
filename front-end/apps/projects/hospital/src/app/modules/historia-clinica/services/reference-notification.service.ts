import { Inject, Injectable } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ClinicalSpecialtyDto, ReferenceDto } from '@api-rest/api-model';
import { ClinicalSpecialtyService } from '@api-rest/services/clinical-specialty.service';
import { ReferenceService } from '@api-rest/services/reference.service';
import { REFERENCE_CONSULTATION_TYPE } from '@historia-clinica/modules/ambulatoria/constants/reference-masterdata';
import { CounterreferenceDockPopupComponent } from '@historia-clinica/modules/ambulatoria/dialogs/counterreference-dock-popup/counterreference-dock-popup.component';
import { ReferenceNotificationComponent } from '@historia-clinica/modules/ambulatoria/dialogs/reference-notification/reference-notification.component';
import { AmbulatoriaSummaryFacadeService } from '@historia-clinica/modules/ambulatoria/services/ambulatoria-summary-facade.service';
import { MedicacionesService } from '@historia-clinica/modules/ambulatoria/services/medicaciones.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
	providedIn: 'root'
})

export class ReferenceNotificationService {

	private openConsultation = new BehaviorSubject(0);

	specialtiesId: number[] = [];

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		private readonly referenceService: ReferenceService,
		private readonly dialog: MatDialog,
		private readonly clinicalSpecialtyService: ClinicalSpecialtyService,
		private readonly medicacionesService: MedicacionesService,
		private readonly ambulatoriaSummaryFacadeService: AmbulatoriaSummaryFacadeService,
		private readonly dockPopupService: DockPopupService,
	) { }

	hasReferenceNotification() {
		this.clinicalSpecialtyService.getLoggedInProfessionalClinicalSpecialties().subscribe((specialties: ClinicalSpecialtyDto[]) => {
			specialties.forEach((specialty: ClinicalSpecialtyDto) => {
				this.specialtiesId.push(specialty.id);
			});
			this.referenceService.getReferences(this.data.patientId, this.specialtiesId).subscribe((references: ReferenceDto[]) => {
				if (references.length) {
					this.openReferenceNotification(references);
				}
				else {
					this.openConsultation.next(this.data.consultationType);
				}
			});
		});
	}

	openReferenceNotification(references: ReferenceDto[]) {
		const dialogRef = this.dialog.open(ReferenceNotificationComponent, {
			data: references,
			autoFocus: false,
			disableClose: true,
		})
		dialogRef.afterClosed().subscribe(counterreference => {
			if (counterreference === null) {
				return;
			}
			
			if (counterreference === false) {
				this.openConsultation.next(this.data.consultationType);
			}
			
			if (counterreference.isACountisACounterrefer === true) {
				this.openCounterreference(counterreference.reference);
			}
		});
	}

	openCounterreference(reference: ReferenceDto) {
		const dialogRef = this.dockPopupService.open(CounterreferenceDockPopupComponent,
			{
				reference: reference,
				patientId: this.data.patientId
			}
		);
		dialogRef.afterClosed().subscribe(fieldsToUpdate => {
			this.medicacionesService.updateMedication();
			if (fieldsToUpdate) {
				this.ambulatoriaSummaryFacadeService.setFieldsToUpdate(fieldsToUpdate);
			}
		});
	}

	getOpenConsultation(): Observable<any> {
		return this.openConsultation
	}
}

export interface ReferenceNotificationInfo {
	patientId: number,
	consultationType: REFERENCE_CONSULTATION_TYPE
}
