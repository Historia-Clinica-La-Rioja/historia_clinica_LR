import { TestBed } from '@angular/core/testing';
import { ToothSurfaceId } from '../utils/Surface';

import { ActionsService, ActionType, CurrentDraw } from './actions.service';

describe('ActionsService', () => {
	let service: ActionsService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = new ActionsService();
	});

	it('should draw first finding', (done: DoneFn) => {

		service.setActions([]);
		service.setFinding('1', []);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].type === ActionType.PROCEDURE).toBeFalse();
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			done();
		})
	});
});
