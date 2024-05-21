import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField, 
    BooleanField,
    TopToolbar,
    ListButton,
    EditButton
} from 'react-admin';


const MedicineShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/medicinefinancingstatus" label="Listar Fármacos"/>
                <EditButton basePath="/medicinefinancingstatus" record={{ id: data.id }} /> 
            </TopToolbar>
        )
};

const MedicineFinancingStatusShow = props => (
    <Show {...props} actions={<MedicineShowActions/>}>
        <SimpleShowLayout>
            <TextField source="conceptPt"/>
            <BooleanField source="financed" label="Financiado por dominio" />
        </SimpleShowLayout>
    </Show>
);

export default MedicineFinancingStatusShow;