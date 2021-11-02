import React from 'react';
import {BooleanField, Datagrid, Filter, List, TextField, TextInput, TopToolbar} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

const PersonFilter = (props) => (
    <Filter {...props}>
        <TextInput source="username" />
    </Filter>
);

const CustomTopToolbar = () => {
    return (
        <TopToolbar></TopToolbar>
    );
};

const UserList = props => (
    <List {...props} filter={{username: "admin@example.com"}} actions={<CustomTopToolbar />} filters={<PersonFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="edit">
            <TextField source="username" />
            <BooleanField source="enable" />
            <SgxDateField source="lastLogin" />
        </Datagrid>
    </List>
);

export default UserList;
