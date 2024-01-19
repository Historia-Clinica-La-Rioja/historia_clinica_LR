import { Tabs } from '@access-management/routes/home/home.component';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TabsService {

  tabs: Tabs[] = [Tabs.REQUESTS, Tabs.REGULATION_OFFER];
  selectedIndex = 0;
  tabActive = Tabs.REQUESTS;

  constructor() { }

  getTabActive(): Tabs {
    return this.tabActive;
  }

  setTabActive(index: number): void {
    this.tabActive = this.tabs[index];
    this.selectedIndex = index;
  }

  clearInfo(): void {
    this.selectedIndex = 0;
    this.tabActive = Tabs.REQUESTS;
  }
}
