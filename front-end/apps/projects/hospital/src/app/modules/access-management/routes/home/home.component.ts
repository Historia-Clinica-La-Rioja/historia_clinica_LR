import { TabsService } from '@access-management/services/tabs.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent {

  Tabs = Tabs;
  currentTab: Tabs;

  constructor( readonly tabsService: TabsService ) { }

  ngOnInit() {
    this.tabsService.getTabActive().subscribe(
			currentTab => { this.currentTab = currentTab }
    );
  }

  tabChanged(tabChangeIndex: number) {
    this.tabsService.setTabActive(tabChangeIndex);
  }

}

export enum Tabs {
  REGULATION_OFFER = "OFERTA POR REGULACION",
  REQUESTS = 'SOLICITUDES'
}
