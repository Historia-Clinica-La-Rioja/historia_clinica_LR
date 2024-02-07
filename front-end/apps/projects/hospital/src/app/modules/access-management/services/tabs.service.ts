import { Tabs } from '@access-management/routes/home/home.component';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable()
export class TabsService {

  tabs = [Tabs.REQUESTS, Tabs.REGULATION_OFFER];
  selectedIndex = 0;
  tabActive$: Observable<Tabs> = of(Tabs.REQUESTS);

  constructor() { }

  getTabActive(): Observable<Tabs> {
    return this.tabActive$;
  }

  setTabActive(index: number) {
    this.tabActive$ = of(this.tabs[index]);
    this.selectedIndex = index;
  }

  clearInfo() {
    this.selectedIndex = 0;
    this.tabActive$ = of(Tabs.REQUESTS);
  }
}
