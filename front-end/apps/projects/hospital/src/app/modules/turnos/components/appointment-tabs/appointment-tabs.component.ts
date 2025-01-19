import { Component, Input } from '@angular/core';
import { TabsLabel } from '@turnos/constants/tabs';

@Component({
  selector: 'app-appointment-tabs',
  templateUrl: './appointment-tabs.component.html',
  styleUrls: ['./appointment-tabs.component.scss']
})
export class AppointmentTabsComponent {

  @Input() tabLabel: TabsLabel;
 
  TabsLabel = TabsLabel;

  constructor() { }

}
