import React from 'react';
import {BooleanField, Datagrid, List, TextField, TopToolbar} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";


const CustomTopToolbar = () => {
    return (
        <TopToolbar></TopToolbar>
    );
};

const AdminList = props => (
    <List {...props} filter={{username: "admin@example.com"}} actions={<CustomTopToolbar />} bulkActionButtons={false}>
        <Datagrid rowClick="edit">
            <TextField source="username" />
            <BooleanField source="enable" />
            <SgxDateField source="lastLogin" />
        </Datagrid>
    </List>
);

export default AdminList;
