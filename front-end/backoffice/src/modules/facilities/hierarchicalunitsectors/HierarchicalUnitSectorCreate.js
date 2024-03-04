import React from 'react';
import { AutocompleteInput, Create, ReferenceInput, SimpleForm, SelectInput } from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const HierarchicalUnitSectorCreate = props => {
    const redirect = `/hierarchicalunits/${props?.location?.state?.record?.hierarchicalUnitId}/show`;
    return(
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <ReferenceInput
                    source="hierarchicalUnitId"
                    reference="hierarchicalunits"
                    label="resources.hierarchicalunitsectors.fields.hierarchicalUnitId"
                >
                    <AutocompleteInput optionText="alias" optionValue="id" options={{ disabled: true }}/>
                </ReferenceInput>
                <ReferenceInput
                    source="sectorId"
                    reference="sectors"
                    sort={{ field: 'description', order: 'ASC' }}
                    label="resources.hierarchicalunitsectors.fields.sectorId"
                    filter={{ institutionId: props?.location?.state?.record.institutionId }}
                >
                    <SelectInput optionText="description" optionValue="id"/>
                </ReferenceInput>
            </SimpleForm>

        </Create>
    );
}

export default HierarchicalUnitSectorCreate;
