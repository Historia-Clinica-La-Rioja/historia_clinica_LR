import React from 'react';
import { 
    List, 
    Datagrid, 
    DeleteButton, 
    TextField, 
    TextInput,
    Filter
} from 'react-admin';

const MedicineGroupFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name"/>
    </Filter>
)

const MedicineGroupList = props => (
    <List {...props} filters={<MedicineGroupFilter/>} sort={{ field:'name', order:'ASC' }}>
        <Datagrid rowClick="show">
            <TextField source="name" label="Nombre"/>
            <DeleteButton/>
        </Datagrid>
    </List>
);

export default MedicineGroupList;