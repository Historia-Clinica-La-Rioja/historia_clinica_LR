import { cold } from 'jasmine-marbles';
import { UserKeysService } from './user-keys.service';

describe('UserKeysService', () => {
	let userKeysService: UserKeysService;
	let apiKeyServiceMock;
	let snackBarServiceMock;

	beforeEach(() => {
		apiKeyServiceMock = jasmine.createSpy('ApiKeyService');
		snackBarServiceMock = jasmine.createSpy('SnackBarService');
		userKeysService = new UserKeysService(apiKeyServiceMock, snackBarServiceMock);
	});

	it('Should emit undefined then the list', () => {
		apiKeyServiceMock.list = () => cold('-a', { a: [] });

		userKeysService.fetch();

		expect(userKeysService.list$).toBeObservable(cold('ab', { a: undefined, b: [] }));

	});


});


