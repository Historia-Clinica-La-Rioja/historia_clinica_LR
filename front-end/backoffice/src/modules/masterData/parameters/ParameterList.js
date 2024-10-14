import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    SelectField,
    ReferenceArrayField,
    SingleFieldList,
    ChipField,
    ReferenceField,
    FunctionField,
    EditButton,
} from 'react-admin';
import { TYPE_CHOICES_IDS } from './ParameterTypes';

const LoincDescription = (props) => (
    <ReferenceField
        source="loincId"
        reference="loinc-codes"
        label="resources.parameters.fields.loincId"
        link={false}
    >
        <FunctionField
            render={(x) => x?.customDisplayName || x?.description || ''}
            label="resources.parameters.fields.loincId" />
    </ReferenceField>
);

const ParameterDescription = props => (
    props.record.description ? <TextField source="description" /> : <LoincDescription />
);

export const ParameterLoincCode = props => (
    <ReferenceField
        source="loincId"
        reference="loinc-codes"
        link={false}
        sortable={false}
    >
        <TextField source="code" />
    </ReferenceField>
)

const ParameterList = props => (
    <List
        {...props}
        hasCreate={true}
        bulkActionButtons={false}
    >
        <Datagrid rowClick="show">
            <ParameterDescription label="resources.parameters.fields.description" />
            <ParameterLoincCode  label="resources.parameters.fields.loincId"/>
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