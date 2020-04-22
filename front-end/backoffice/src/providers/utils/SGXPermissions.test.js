import SGXPermissions from './SGXPermissions';

test('empty hasAnyAuthority should be always true', () => {
    expect(
        (new SGXPermissions({ authorities: [] })
    ).hasAnyAuthority()).toBeTruthy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}] })
    ).hasAnyAuthority()).toBeTruthy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}, {authority: ''}] })
    ).hasAnyAuthority()).toBeTruthy();
});

test('hasAnyAuthority of empty should be always false', () => {
    expect(
        (new SGXPermissions({ authorities: [] })
    ).hasAnyAuthority('')).toBeFalsy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}] })
    ).hasAnyAuthority('')).toBeFalsy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}, {authority: 'B'}] })
    ).hasAnyAuthority('')).toBeFalsy();
});

test('hasAnyAuthority should be true if at least one exists', () => {
    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}] })
    ).hasAnyAuthority('ADMIN')).toBeTruthy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'ADMIN'}] })
    ).hasAnyAuthority('ADMIN', 'F')).toBeTruthy();

    expect(
        (new SGXPermissions({ authorities: [{authority: 'A'}, {authority: 'ADMIN'}, {authority: 'B'}] })
    ).hasAnyAuthority('C', 'D', 'E', 'ADMIN')).toBeTruthy();
});