import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
    ReferenceField,
    FunctionField
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
            <ReferenceField source="patientId" reference="patient" link={false}>
                <ReferenceField source="personId" reference="person" link={false}>
                    <FunctionField render={p => `${p.firstName} ${p.lastName}`} />
                </ReferenceField>
            </ReferenceField>
            <TextField source="snomedSctid" />
            <TextField source="snomedPt" />
            <TextField source="status" />
            <TextField source="responseCode" />
            <ReferenceField source="professionalId" reference="healthcareprofessionals" link={false}>
                <ReferenceField source="personId" reference="person" link={false}>
                    <FunctionField render={professional => `${professional.firstName} ${professional.lastName}`} />
                </ReferenceField>
            </ReferenceField>
            <ReferenceField source="institutionId" reference="institutions" link={false}>
                <TextField source="name" />
            </ReferenceField>
            <TextField source="sisaRegisteredId" />
            <SgxDateField source="lastUpdate" />
        </Datagrid>
    </List>
);

export default SnvsList;

