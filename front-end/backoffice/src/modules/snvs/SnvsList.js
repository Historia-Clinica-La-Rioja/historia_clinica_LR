import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput, 
    ReferenceField
} from 'react-admin';
import SgxDateField from "../../dateComponents/sgxDateField";

const SnvsFilter = (props) => (
    <Filter {...props}>
        <TextInput source="id" />
        <TextInput source="groupEventId" />
        <TextInput source="eventId" />
        <TextInput source="patientId" />
        <TextInput source="snomedSctid" />
        <TextInput source="status" />
        <TextInput source="responseCode" />
        <TextInput source="professionalId" />
        <TextInput source="institutionId" />
        <TextInput source="sisaRegisteredId" />
    </Filter>
);

const SnvsList = props => (
    <List {...props} filters={<SnvsFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="id" />
            <TextField source="groupEventId" />
            <TextField source="eventId" />
            <TextField source="manualClassificationId" />
            <TextField source="patientId" />
            <TextField source="snomedSctid" />
            <TextField source="snomedPt" />
            <TextField source="status" />
            <TextField source="responseCode" />
            <TextField source="professionalId" />
            <ReferenceField source="institutionId" reference="institutions" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <TextField source="sisaRegisteredId" />
            <SgxDateField source="lastUpdate" />
        </Datagrid>
    </List>
);

export default SnvsList;

