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
import { TYPE_CHOICES_IDS } from './parameter-type';

const TextChipField = (props) => {
    const value = props.record;
    return <ChipField record={{value: value}} source="value"/>
}

const ProcedureTemplateParameterShow = props => (
    <Show {...props} hasEdit={false}>
        <SimpleShowLayout>
            
            <ReferenceField
                source="loincId"
                reference="loinc-codes"
                link={false}
                sortable={false}
            >
                <TextField source="code"/>
            </ReferenceField>

            <ReferenceField
                source="loincId"
                reference="loinc-codes"
                label="resources.proceduretemplateparameters.fields.loincId"
                link={false}
            >
                <FunctionField
                    render={(x) => x?.customDisplayName || x?.displayName || x?.description || ''} 
                    label="resources.proceduretemplateparameters.fields.loincId"/>
            </ReferenceField>

            <SelectField source="typeId" choices={TYPE_CHOICES_IDS}/>

            <ArrayField source="textOptions">
                <SingleFieldList linkType={false}>
                    <TextChipField/>
                </SingleFieldList>
            </ArrayField>

            <ReferenceArrayField
                label="resources.proceduretemplateparameters.fields.unitsOfMeasureIds"
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
                <FunctionField render={(x) => x.description}/>
            </ReferenceField>

        </SimpleShowLayout>
    </Show>
);

export default ProcedureTemplateParameterShow;