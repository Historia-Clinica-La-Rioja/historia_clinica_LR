import React from 'react';
import { EditButton, ListButton, ReferenceField, Show, SimpleShowLayout, TextField, TopToolbar } from 'react-admin';

const MedicalCoverageShowActions = ({data}) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/medicalcoverages" label="Listar Coberturas médicas"/>
                <EditButton basePath="/medicalcoverages" record={{id: data.id}}/>
            </TopToolbar>
        )
};

const MedicalCoverageShow = props => (
    <Show actions={<MedicalCoverageShowActions/>} {...props}>
        <SimpleShowLayout>
            <TextField source="name"/>
            <TextField source="cuit"/>
            <ReferenceField source="type" reference="medicalcoveragetypes" link={false}>
                <TextField source="value"/>
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default MedicalCoverageShow;
