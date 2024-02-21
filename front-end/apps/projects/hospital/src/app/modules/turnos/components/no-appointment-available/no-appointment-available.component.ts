
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { TabsLabel } from '@turnos/constants/tabs';
import { TabsService } from '@turnos/services/tabs.service';
@Component({
  selector: 'app-no-appointment-available',
  templateUrl: './no-appointment-available.component.html',
  styleUrls: ['./no-appointment-available.component.scss']
})
export class NoAppointmentAvailableComponent implements OnInit {
  @Output() preloadData = new EventEmitter<boolean>();
  constructor(
    private readonly tabsService: TabsService) { }

  ngOnInit(): void {
  }

  redirectToSearchInCareNetwork() {
    this.preloadData.emit(true);
    this.tabsService.setTab(TabsLabel.CARE_NETWORK);
  }
}
