import React from 'react';
import {Datagrid, DeleteButton, List, ReferenceField, TextField} from 'react-admin';

const ClinicalSpecialtyMandatoryMedicalPracticeList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name"/>
            </ReferenceField>
            <ReferenceField source="mandatoryMedicalPracticeId" reference="mandatorymedicalpractices">
                <TextField source="description"/>
            </ReferenceField>
            <TextField source="practiceRecommendations" />
            <DeleteButton />
        </Datagrid>
    </List>
);

export default ClinicalSpecialtyMandatoryMedicalPracticeList;