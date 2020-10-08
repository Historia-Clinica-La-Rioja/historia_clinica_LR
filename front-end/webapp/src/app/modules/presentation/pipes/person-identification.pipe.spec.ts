import { PersonIdentificationPipe } from './person-identification.pipe';

describe('PersonIdentificationPipe', () => {
	it('create an instance', () => {
		const pipe = new PersonIdentificationPipe();
		expect(pipe).toBeTruthy();
	});
});
