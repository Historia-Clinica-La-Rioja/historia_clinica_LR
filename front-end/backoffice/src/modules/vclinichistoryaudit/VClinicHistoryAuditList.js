import React from 'react';
import {
    Datagrid,
    List,
    TextField,
    DateField,
    Filter,
    TextInput,
    DateInput,
    SelectField,
    SelectInput
} from 'react-admin';

const ReasonSelectInput = (props) => {
    return (
    <SelectInput {...props} choices= {[
        { id: 1, name: 'Urgencia médica' },
        { id: 2, name: 'Consulta profesional' },
        { id: 3, name: 'Consulta de paciente' },
        { id: 4, name: 'Auditoría' },
    ]
    }
    />)
}

const AuditFilter = (props) => (
    <Filter {...props}>
        <TextInput source="identificationNumber" />
        <DateInput source="date" />
        <TextInput source="firstName" />
        <TextInput source="lastName" />
        <TextInput source="description" />
        <TextInput source="username" />
        <ReasonSelectInput source="reasonId" />
        <TextInput source="institutionName" />

    </Filter>
);

const ClinicHistoryAuditList = props => {

    return (
        <List {...props} filters={<AuditFilter />} bulkActionButtons={false} hasCreate={false} exporter={false}>
            <Datagrid rowClick={"show"}>
                <TextField source="firstName" />
                <TextField source="lastName" />
                <TextField source="description" />
                <TextField source="identificationNumber" />
                <TextField source="username" />
                <DateField source="date"/>
                <SelectField source="reasonId" choices={[
                    { id: 1, name: 'resources.vclinichistoryaudit.medicalEmergency' },
                    { id: 2, name: 'resources.vclinichistoryaudit.professinalConsultation' },
                    { id: 3, name: 'resources.vclinichistoryaudit.patientConsultation' },
                    { id: 4, name: 'resources.vclinichistoryaudit.audit' },
                ]} />
                <TextField source="institutionName" />
            </Datagrid>
        </List>
    );
};

export default ClinicHistoryAuditList;