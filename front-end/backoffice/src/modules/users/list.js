import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    DateField,
    BooleanField, 
    Filter,
    ReferenceField,
    TextInput,
    ReferenceInput,
    AutocompleteInput,
} from 'react-admin';

const PersonFilter = (props) => (
    <Filter {...props}>
        <TextInput source="username" />

        <ReferenceInput source="id" reference="persons" 
            allowEmpty={false}
            filterToQuery={searchText => ({ completeName: searchText })}>
            <AutocompleteInput optionText="completeName"/>
        </ReferenceInput>
    </Filter>
);

const UserList = props => (
    <List {...props} filters={<PersonFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="edit">
            <TextField source="username" />
            <ReferenceField source="personId" reference="persons" link="show" sortable={false}>
                <TextField source="completeName" />
            </ReferenceField>
            <BooleanField source="enable" />
            <DateField source="lastLogin" />
        </Datagrid>
    </List>
);

export default UserList;