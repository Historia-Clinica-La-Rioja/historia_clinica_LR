import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    useGetOne
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const HierarchicalUnitParentField = ({ record }) => {
    const hierarchicalUnitChild = useGetOne('hierarchicalunits',  record.hierarchicalUnitChildId);
    return (
        <ReferenceInput
            source="hierarchicalUnitParentId"
            reference="hierarchicalunits"
            sort={{ field: 'alias', order: 'ASC' }}
            filter={{institutionId: hierarchicalUnitChild.data?.institutionId}}
            label="resources.hierarchicalunitrelationships.fields.hierarchicalUnitParentId"
            perPage={100}
        >
            <AutocompleteInput optionText="alias" optionValue="id" />
        </ReferenceInput>
    );
}
const HierarchicalUnitParentRelationShipCreate = props => {
    const redirect = `/hierarchicalunits/${props?.location?.state?.record?.hierarchicalUnitChildId}/show`;
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <ReferenceInput
                    source="hierarchicalUnitChildId"
                    reference="hierarchicalunits"
                    label="resources.hierarchicalunitrelationships.fields.hierarchicalUnitChildId"
                >
                    <AutocompleteInput optionText="alias" optionValue="id" options={{ disabled: true }}/>
                </ReferenceInput>
                <HierarchicalUnitParentField/>
            </SimpleForm>
        </Create>
    );
}

export default HierarchicalUnitParentRelationShipCreate;
