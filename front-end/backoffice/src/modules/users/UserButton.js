import React from 'react';
import {
    EditButton,
    useGetList,
} from 'react-admin';

import AccountCircleIcon from '@material-ui/icons/AccountCircle';

import CreateUserButton from './CreateUserButton';


const UserButton = ({ record }) => {

    const { data: users, loading: loadingUsers, ids: userIds } = useGetList(
        'users',
        { page: 1, perPage: 100 },
        { field: 'lastLogin', order: 'DESC' },
        { personId: record && record.id }
    );

    if (loadingUsers) {
        return (<span>...</span>);
    }
    const user = userIds.length > 0 ? users[userIds[0]] : null;

    return (user) ? (
        <EditButton
            style={{ textTransform: "none" }}
            basePath="/users"
            label={user.username}
            record={user}
            icon={<AccountCircleIcon />}
        >
        </EditButton>
    ) : (
        <CreateUserButton record={record} />
    );
};

export default UserButton;
