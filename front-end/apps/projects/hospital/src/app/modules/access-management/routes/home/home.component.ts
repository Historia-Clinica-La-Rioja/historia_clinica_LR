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

  ngOnChanges(): void {
    this.currentTab = this.tabsService.getTabActive();
  }

  ngOnDestroy(): void {
    this.tabsService.clearInfo();
  }

  tabChanged(tabChangeIndex: number): void {
    this.tabsService.setTabActive(tabChangeIndex);
  }

}

export enum Tabs {
  REGULATION_OFFER = "OFERTA POR REGULACION",
  REQUESTS = 'SOLICITUDES'
}
