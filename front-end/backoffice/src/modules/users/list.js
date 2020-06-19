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

import PeopleReferenceField from '../people/PeopleReferenceField';

const PersonFilter = (props) => (
    <Filter {...props}>
        <TextInput source="username" />
    </Filter>
);

const UserList = props => (
    <List {...props} filters={<PersonFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="edit">
            <TextField source="username" />
            <PeopleReferenceField source="personId" sortable={false}/>
            <BooleanField source="enable" />
            <DateField source="lastLogin" />
        </Datagrid>
    </List>
);

export default UserList;
