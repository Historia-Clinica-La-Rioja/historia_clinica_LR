import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    BooleanField,
    Filter,
    TextInput,
    BooleanInput
} from 'react-admin';

const MedicineFilter = (props) => (
    <Filter {...props}>
        <TextInput source="conceptSctid"/>
        <TextInput source="conceptPt"/>
        <BooleanInput source="financed"/>
    </Filter>
);

const MedicineFinancingStatusList = props => (
    <List {...props} perPage={25} filters={<MedicineFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="conceptSctid"/>
            <TextField source="conceptPt"/>
            <BooleanField source="financed" sortable={false}/>
        </Datagrid>
    </List>
)

export default MedicineFinancingStatusList;
