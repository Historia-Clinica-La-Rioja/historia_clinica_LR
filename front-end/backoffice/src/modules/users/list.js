import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
    BooleanField, 
    Filter,
    TextInput,
} from 'react-admin';

const PersonFilter = (props) => (
    <Filter {...props}>
        <TextInput source="username" />
    </Filter>
);

const UserList = props => (
    <List {...props} filter={{personId: -1}} filters={<PersonFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="edit">
            <TextField source="username" />
            <BooleanField source="enable" />
            <DateField source="lastLogin" />
        </Datagrid>
    </List>
);

export default UserList;
