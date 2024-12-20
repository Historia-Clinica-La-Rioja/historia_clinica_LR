import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AvailableToLogoutStorageService {

  constructor() { }

  private pendings: Map<string, PendingItem> = new Map;

  addPending(component: string, info: PendingItem) {
    this.pendings.set(component, info)
  }

  clearPending(component: string) {
    this.pendings.delete(component);
  }

  getPendings(): Map<string, PendingItem> {
    return this.pendings;
  }

  restart(){
    this.pendings = new Map;
  }
}

export interface PendingItem {
  extensionName: string,
  extensionMessage: string;
  absolutUrl: string;
}
