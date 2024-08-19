import React from 'react';
import {
    Show, 
    SimpleShowLayout,
    TextField,
    BooleanField,
    TopToolbar,
    ListButton,
    EditButton
 }from 'react-admin';

 const MedicineGroupShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/medicinegroups" label="Listar grupos de fármacos"/>
                <EditButton basePath="/medicinegroups" record={{ id: data.id }} /> 
            </TopToolbar>
        )
};


const MedicineGroupShow = props => (
    <Show {...props} actions={<MedicineGroupShowActions/>}>
        <SimpleShowLayout>
            <TextField source="name"/>
            <br/>
            <span>Cobertura pública exclusiva</span>
            <BooleanField source="requiresAudit"/>
            <br/>
            <span>Ámbito</span>
            <BooleanField source="outpatient"/>
            <BooleanField source="emergencyCare"/>
            <BooleanField source="internment"/>
            <br/>
            <span>Mensaje para indicaciones</span>
            <TextField source="message" label=""/>
        </SimpleShowLayout>
    </Show>
)

export default MedicineGroupShow;