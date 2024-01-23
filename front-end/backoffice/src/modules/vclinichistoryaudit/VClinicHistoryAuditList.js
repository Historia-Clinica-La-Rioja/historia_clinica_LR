import React from 'react';
import {
    Datagrid,
    List,
    TextField,
    DateField,
    Filter,
    TextInput,
    DateInput
} from 'react-admin';

const AuditFilter = (props) => (
    <Filter {...props}>
        <TextInput source="identificationNumber" />
        <DateInput source="date" />
        <TextInput source="firstName" />
        <TextInput source="lastName" />
        <TextInput source="description" />
        <TextInput source="username" />
        <TextInput source="reasonId" />
        <TextInput source="institutionName" />

    </Filter>
);

const ClinicHistoryAuditList = props => {

    return (
        <List {...props} filters={<AuditFilter />}bulkActionButtons={false} hasCreate={false}>
            <Datagrid rowClick={"show"}>
                <TextField source="firstName" />
                <TextField source="lastName" />
                <TextField source="description" />
                <TextField source="identificationNumber" />
                <TextField source="username" />
                <DateField source="date"/>
                <TextField source="reasonId" />
                <TextField source="institutionName" />
            </Datagrid>
        </List>
    );
};

export default ClinicHistoryAuditList;
