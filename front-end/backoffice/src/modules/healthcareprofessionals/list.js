import React from 'react';
import {
    List,
    Datagrid,
    BooleanField,
    ReferenceField,
    FunctionField,
    TextField,
} from 'react-admin';

const renderPerson = (choice) => `${choice.identificationNumber} ${choice.lastName} ${choice.firstName}`;
const HealthcareProfessionalList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="licenseNumber" />
            <ReferenceField source="personId" reference="people">
                <FunctionField render={renderPerson}/>
            </ReferenceField>
            <BooleanField source="isMedicalDoctor" />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalList;

