import { Component, OnInit } from '@angular/core';
import { EquipmentDto } from '@api-rest/api-model';
import { EquipmentService } from '@api-rest/services/equipment.service';

@Component({
    selector: 'app-worklist',
    templateUrl: './worklist.component.html',
    styleUrls: ['./worklist.component.scss']
})
export class WorklistComponent implements OnInit {
    equipments: EquipmentDto[] = [];

    constructor(private readonly equipmentService: EquipmentService) { }

    ngOnInit(): void {
        this.equipmentService.getAll().subscribe((equipments: EquipmentDto[]) => {
            this.equipments = equipments;
        });
    }

}
