import { Injectable, Renderer2 } from '@angular/core';
import { EventInfo, EventType } from '@core/custom-events';
import { AvailableToLogoutStorageService, PendingItem } from '@core/services/available-to-logout-storage.service';


@Injectable()
export class CustomEventsHandlerService {

  constructor(
    private readonly availableToLogoutStorageService: AvailableToLogoutStorageService,
  ) { }

  handleEveryEventFromElement(renderer2: Renderer2, extensionInfo?: ExtensionInfo) {
    this.handleAvailableToLogoutEvent(renderer2, extensionInfo);
    // To be completed with other events
  }

  private handleAvailableToLogoutEvent(renderer2: Renderer2, extensionInfo: ExtensionInfo) {
    renderer2.listen('document', EventType.AVAILABLE_TO_LOGOUT, (message: CustomEvent<EventInfo>) => {
      const eventEmittedFrom = getHtmlTagNameFrom(message);

      if (availableToLogout(message.detail)) {
        this.availableToLogoutStorageService.clearPending(eventEmittedFrom);
        return;
      }

      const newPendingItem = toPendingItem(extensionInfo, message.detail.message);
      this.availableToLogoutStorageService.addPending(eventEmittedFrom, newPendingItem)
    })
  }
}

const availableToLogout = (messageDetail: EventInfo) => !messageDetail;

const getHtmlTagNameFrom = (message: CustomEvent<EventInfo>): string => {
  return (message.target as HTMLElement).tagName.toLocaleLowerCase();
}

const toPendingItem = (redirectInfo: ExtensionInfo, message: string): PendingItem => {
  return {
    extensionName: redirectInfo.extensionName,
    absolutUrl: redirectInfo.absolutUrl,
    extensionMessage: message
  }
}

export interface ExtensionInfo {
  extensionName: string;
  absolutUrl: string;
}