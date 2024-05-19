import React from 'react';
import {
    ArrayField,
    ChipField,
    FunctionField,
    ReferenceArrayField,
    ReferenceField,
    SelectField,
    Show,
    SimpleShowLayout,
    SingleFieldList,
    TextField
} from 'react-admin';
import { TYPE_CHOICES_IDS } from './ParameterTypes';

const TextChipField = (props) => {
    const value = props.record;
    return <ChipField record={{ value: value }} source="value" />
}

const ParameterShow = (props) => (
    <Show {...props} hasEdit={true}>
        <SimpleShowLayout>
            <ReferenceField
                source="loincId"
                label="resources.parameters.fields.loincId"
                reference="loinc-codes"
                link={false}
                sortable={false}
            >
                <TextField source="code" />
            </ReferenceField>

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

            <TextField source="description" label="resources.parameters.fields.description"></TextField>

            <SelectField source="typeId" label="Tipo" choices={TYPE_CHOICES_IDS} />

            <ArrayField source="textOptions">
                <SingleFieldList linkType={false}>
                    <TextChipField />
                </SingleFieldList>
            </ArrayField>

            <ReferenceArrayField
                label="resources.parameters.fields.units"
                reference="units-of-measure"
                source="unitsOfMeasureIds"
                sortable={false}
            >
                <SingleFieldList linkType={false}>
                    <ChipField source="code" />
                </SingleFieldList>
            </ReferenceArrayField>

            <ReferenceField
                reference="snomedgroups"
                source="snomedGroupId"
            >
                <FunctionField render={(x) => x.description} />
            </ReferenceField>

        </SimpleShowLayout>
    </Show>
);


export default ParameterShow;