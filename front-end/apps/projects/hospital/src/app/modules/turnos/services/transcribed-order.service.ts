import { Injectable } from '@angular/core';
import { InfoTranscribeOrderPopup } from '@turnos/dialogs/equipment-transcribe-order-popup/equipment-transcribe-order-popup.component';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class TranscribedOrderService {
    
    transcribedOrderSubject = new BehaviorSubject<InfoTranscribeOrderPopup>(null);
    transcribedOrder$ = this.transcribedOrderSubject.asObservable();

    constructor() {}

    setTranscribedOrder(transcribedOrder: InfoTranscribeOrderPopup) {
        this.transcribedOrderSubject.next(transcribedOrder);
    }

    resetTranscribedOrder(){
        this.transcribedOrderSubject.next(null);
    }
}
