import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    SelectField,
    ReferenceArrayField,
    SingleFieldList,
    ChipField,
    EditButton,
    Filter,
    TextInput
} from 'react-admin';
import { TYPE_CHOICES_IDS } from './ParameterTypes';

const ParameterFilter = props =>(
    <Filter {...props}>
        <TextInput source="loincCode" label="resources.parameters.fields.loincId" />
        <TextInput source="description" />
    </Filter>
);

const ParameterList = props => (
    <List
        {...props}
        hasCreate={true}
        bulkActionButtons={false}
        filters={<ParameterFilter/>}
    >
        <Datagrid rowClick="show">
            <TextField source="description" label="resources.parameters.fields.description" />
            <TextField source="loincCode" sortable={false} label="resources.parameters.fields.loincId"/>
            <SelectField source='typeId' label="resources.parameters.fields.type" choices={TYPE_CHOICES_IDS} sortable={false} />
            <ReferenceArrayField
                label="resources.parameters.fields.units"
                reference="units-of-measure"
                source="unitsOfMeasureIds"
                sortable={false}
            >
                <SingleFieldList>
                    <ChipField source="code" />
                </SingleFieldList>
            </ReferenceArrayField>
            <EditButton />
        </Datagrid>
    </List>
);

export default ParameterList;