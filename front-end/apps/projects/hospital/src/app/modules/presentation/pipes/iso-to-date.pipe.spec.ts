import { IsoToDatePipe } from './iso-to-date.pipe';

describe('IsoToDatePipe', () => {
  it('create an instance', () => {
    const pipe = new IsoToDatePipe();
    expect(pipe).toBeTruthy();
  });
});
