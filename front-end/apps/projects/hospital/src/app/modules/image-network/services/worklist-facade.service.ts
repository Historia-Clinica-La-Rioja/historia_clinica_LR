import { Injectable } from '@angular/core';
import { AppFeature, EquipmentAppointmentListDto } from '@api-rest/api-model';
import { AppointmentsService } from '@api-rest/services/appointments.service';
import { FeatureFlagService } from '@core/services/feature-flag.service';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable()

export class WorklistFacadeService {

    private appointmentsEmitter = new BehaviorSubject<EquipmentAppointmentListDto[]>([]);
	appointments$: Observable<EquipmentAppointmentListDto[]> = this.appointmentsEmitter.asObservable();

    appointments: EquipmentAppointmentListDto[] = [];
    
    equipmentId: number = null;
    startDate: string = null;
    endDate: string = null;

    intervalo: NodeJS.Timer;

    nameSelfDeterminationFF = false;

    constructor(
        private readonly appointmentsService: AppointmentsService,
		private readonly featureFlagService: FeatureFlagService
        ) {
            this.featureFlagService.isActive(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS).subscribe(isOn => {
                this.nameSelfDeterminationFF = isOn
            });
         }

    changeFilters = (equipmentId?: number, startDate?: string, endDate?: string) => {
        this.restartInterval();
        this.equipmentId = equipmentId ? equipmentId : this.equipmentId;
        this.startDate = startDate ? startDate : this.startDate;
        this.endDate = endDate ? endDate : this.endDate;
        this.getAppointments();
    }

    private getAppointments(){
        if (this.equipmentId){
            this.appointmentsService.getAppointmentsByEquipment(this.equipmentId, this.startDate, this.endDate).subscribe(appointments => {
                this.appointments = appointments;
                this.appointmentsEmitter.next(appointments)
            })
        }
    }

    clearEquipmentId(){
        this.equipmentId = null;
    }

    private setInterval(){
        this.intervalo = setInterval(this.changeFilters,  5 * 60 * 1000);
    }

    private restartInterval(){
        clearInterval(this.intervalo);
        this.setInterval();
    }
}
