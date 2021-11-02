import { TestBed } from '@angular/core/testing';
import { ToothSurfaceId } from '../utils/Surface';

import { ActionsService, ActionType, CurrentDraw, ProcedureOrder } from './actions.service';

describe('ActionsService', () => {
	let service: ActionsService;

	beforeEach(() => {
		TestBed.configureTestingModule({});
		service = new ActionsService();
	});

	it('should draw first finding', (done: DoneFn) => {

		service.setActions([]);
		service.addFinding('1', []);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].type === ActionType.PROCEDURE).toBeFalse();
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			done();
		})
	});

	it('should draw tooth procedure and remove any set action before', (done: DoneFn) => {

		service.setActions([]);
		service.addFinding('1', []);
		service.addFinding('2', [ToothSurfaceId.CENTRAL]);
		service.addProcedure('3', [ToothSurfaceId.LEFT], null);
		service.addProcedure('4', [], ProcedureOrder.FIRST);
		service.addProcedure('5', [], ProcedureOrder.SECOND);


		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('5');
			expect(newDraw[ToothSurfaceId.LEFT]).toBeNull();
			expect(newDraw[ToothSurfaceId.CENTRAL]).toBeNull();
			done();
		})
	});

	it(`shouldn't draw whole tooth diagnostic if whole tooth procedure is set`, (done: DoneFn) => {

		service.setActions([]);
		service.addProcedure('1', [], ProcedureOrder.FIRST);
		service.addFinding('2', []);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			done();
		})
	});

	it('should draw tooth diagnostic and keep set surface action', (done: DoneFn) => {

		service.setActions([]);
		service.addProcedure('1', [ToothSurfaceId.CENTRAL], null);
		service.addFinding('2', [ToothSurfaceId.LEFT]);
		service.addFinding('3', []);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.CENTRAL].sctid).toEqual('1');
			expect(newDraw[ToothSurfaceId.LEFT].sctid).toEqual('2');
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('3');
			done();
		})
	});

	it('should draw surface procedure and keep set whole tooth procedure', (done: DoneFn) => {

		service.setActions([]);
		service.addProcedure('1', [], ProcedureOrder.FIRST);
		service.addProcedure('2', [ToothSurfaceId.CENTRAL], null);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			expect(newDraw[ToothSurfaceId.CENTRAL].sctid).toEqual('2');
			done();
		})
	});

	it('should draw surface procedure and keep set whole tooth diagnostic', (done: DoneFn) => {

		service.setActions([]);
		service.addFinding('1', []);
		service.addProcedure('2', [ToothSurfaceId.CENTRAL], null);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			expect(newDraw[ToothSurfaceId.CENTRAL].sctid).toEqual('2');
			done();
		})
	});

	it('should draw surface procedure and remove set surface diagnostic', (done: DoneFn) => {

		service.setActions([]);
		service.addFinding('1', [ToothSurfaceId.RIGHT]);
		service.addProcedure('2', [ToothSurfaceId.RIGHT], ProcedureOrder.FIRST);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.RIGHT].sctid).toEqual('2');
			done();
		})
	});


	it(`shouldn't draw surface diagnostic if a whole tooth procedure is set`, (done: DoneFn) => {

		service.setActions([]);
		service.addProcedure('1', [], ProcedureOrder.FIRST);
		service.addFinding('3', [ToothSurfaceId.RIGHT]);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.RIGHT]).toBeNull();
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			done();
		});

	});


	it(`shouldn't draw surface diagnostic if same surface has a procedure set`, (done: DoneFn) => {

		service.setActions([]);
		service.addProcedure('1', [ToothSurfaceId.RIGHT], null);
		service.addFinding('3', [ToothSurfaceId.RIGHT]);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.RIGHT].sctid).toEqual('1');
			done();
		});

	});



	it('should draw surface diagnostic and keep set whole tooth diagnostic', (done: DoneFn) => {

		service.setActions([]);
		service.addFinding('1', []);
		service.addFinding('2', [ToothSurfaceId.CENTRAL]);

		service.currentDraw$.subscribe((newDraw: CurrentDraw) => {
			expect(newDraw[ToothSurfaceId.WHOLE].sctid).toEqual('1');
			expect(newDraw[ToothSurfaceId.CENTRAL].sctid).toEqual('2');
			done();
		})
	});


});
