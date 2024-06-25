import { Injectable } from '@angular/core';
import { AppFeature, EquipmentAppointmentListDto, WorklistDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { WorklistService } from '@api-rest/services/worklist.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { BehaviorSubject, Observable, Subscription } from 'rxjs';

const REFRESH_TIME = 5 * 60 * 1000;

@Injectable()
export class WorklistFacadeService {

    private appointmentsEmitter = new BehaviorSubject<EquipmentAppointmentListDto[]>([]);
	appointments$: Observable<EquipmentAppointmentListDto[]> = this.appointmentsEmitter.asObservable();

    private reportsEmitter = new BehaviorSubject<WorklistDto[]>([]);
	reports$: Observable<WorklistDto[]> = this.reportsEmitter.asObservable();
    
    equipmentId: number = null;
    modalityId: number = null;
    startDate: string = null;
    endDate: string = null;

    intervalo: NodeJS.Timeout;

    nameSelfDeterminationFF = false;

    subscription: Subscription;

    constructor(
        private readonly appointmentsService: AppointmentsService,
		private readonly featureFlagService: FeatureFlagService,
        private readonly worklistService: WorklistService,
        ) {
            this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
                this.nameSelfDeterminationFF = isOn
            });
         }


    ngOnDestroy() {
        clearInterval(this.intervalo);
        this.subscription?.unsubscribe();
    }

    changeTechnicalFilters = (equipmentId?: number, startDate?: string, endDate?: string) => {
        this.restartInterval(this.changeTechnicalFilters, REFRESH_TIME); // Refresh cada 5 minutos
        this.equipmentId = equipmentId ? equipmentId : this.equipmentId;
        this.startDate = startDate ? startDate : this.startDate;
        this.endDate = endDate ? endDate : this.endDate;
        this.getAppointments();
    }

    changeInformerFilters = (modalityId?: number, startDate?: string, endDate?: string) => {
        this.restartInterval(this.changeInformerFilters, REFRESH_TIME); // Refresh cada 5 minutos
        this.modalityId = modalityId;
        this.startDate = startDate ? startDate : this.startDate;
        this.endDate = endDate ? endDate : this.endDate;
        this.getReports();
    }

    private getAppointments(){
        if (this.equipmentId){
            this.subscription = this.appointmentsService.getAppointmentsByEquipment(this.equipmentId, this.startDate, this.endDate).subscribe(appointments => {
                this.appointmentsEmitter.next(appointments)
            })
        }
    }

    private getReports() {
        this.subscription = this.worklistService.getByModalityAndInstitution(this.modalityId, this.startDate, this.endDate).subscribe((worklist: WorklistDto[]) => {
			this.reportsEmitter.next(worklist);
		});
    }

    clearEquipmentId(){
        this.equipmentId = null;
    }

    private setInterval(functionToExecute, refreshTime: number){
        this.intervalo = setInterval(functionToExecute,  refreshTime);
    }

    private restartInterval(functionToExecute, refreshTime: number){
        clearInterval(this.intervalo);
        this.setInterval(functionToExecute, refreshTime);
    }
}
