import SGXPermissions from './SGXPermissions';

test('empty hasAnyAssignment should be always true', () => {
    expect(
        (new SGXPermissions([], [])
    ).hasAnyAssignment()).toBeTruthy();

    expect(
        (new SGXPermissions([{role: 'ADMIN'}], [])
    ).hasAnyAssignment()).toBeTruthy();

    expect(
        (new SGXPermissions([{role: 'ADMIN'}, {role: ''}], [])
    ).hasAnyAssignment()).toBeTruthy();
});

test('hasAnyAssignment should be true if at least one exists', () => {
    expect(
        (new SGXPermissions([{role: 'ADMIN'}], [])
    ).hasAnyAssignment({role: 'ADMIN'})).toBeTruthy();

    expect(
        (new SGXPermissions([{role: 'ADMIN'}], [])
    ).hasAnyAssignment({role: 'ADMIN'}, {role: 'F'})).toBeTruthy();

    expect(
        (new SGXPermissions([{role: 'A'}, {role: 'ADMIN'}, {role: 'B'}], [])
    ).hasAnyAssignment({role: 'C'}, {role: 'D'}, {role: 'E'}, {role: 'ADMIN'})).toBeTruthy();
});
