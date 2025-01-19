import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    BooleanField,
    EditButton,
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';

const UnitOfMeasureList = props => (
    <>
        <SectionTitle label="resources.units-of-measure.title" />
        <List 
            {...props}
            exporter={false}
            bulkActionButtons={false}
            actions={false}
            sort={{ field: 'code', order: 'ASC' }}
        >
            <Datagrid rowClick="show">
                <TextField source="code"/>
                <BooleanField source="enabled" />
                <EditButton/>
            </Datagrid>
        </List>
    </>
);

export default UnitOfMeasureList;