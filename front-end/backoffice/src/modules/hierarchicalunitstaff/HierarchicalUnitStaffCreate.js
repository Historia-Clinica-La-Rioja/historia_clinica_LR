import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    BooleanInput,
    ReferenceManyField,
    Datagrid,
    ReferenceField,
    FunctionField,
    TextField,
    BooleanField
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import { renderPersonData } from "../hierarchicalunits/HierarchicalUnitShow";

const personInputData = (person) => {
    return person ? `${person.completeName ? person.completeName : "" } ${person.completeLastName ? person.completeLastName : "" } ${person.identificationNumber ? "- " + person.identificationNumber : "" }` : null;
};

const HierarchicalUnitStaffCreate = props => {
    const redirect = `/hierarchicalunits/${props.location?.state?.record?.hierarchicalUnitId}/show`;
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <ReferenceInput
                    source="hierarchicalUnitId"
                    reference="hierarchicalunits"
                >
                    <AutocompleteInput optionText="alias" optionValue="id" options={{ disabled: true }}/>
                </ReferenceInput>
                <ReferenceInput
                    source="userId"
                    reference="institutionuserpersons"
                    filter={{institutionId: props?.location?.state?.record.institutionId}}
                    label="resources.hierarchicalunitstaff.fields.userId"
                >
                    <AutocompleteInput optionText={personInputData} optionValue="id" />
                </ReferenceInput>
                <BooleanInput source="responsible" disabled={false} initialValue={false}/>
                <ReferenceManyField
                    addLabel={false}
                    reference="hierarchicalunitstaff"
                    target="hirerarchicalUnitId"
                    filter={{hierarchicalUnitId: props.location?.state?.record?.hierarchicalUnitId}}
                >
                    <Datagrid empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin usuarios definidos</p>}>
                        <ReferenceField source="userId" reference="institutionuserpersons" label="resources.person.fields.firstName" link={false}>
                            <FunctionField render={renderPersonData}/>
                        </ReferenceField>
                        <ReferenceField source="userId" reference="institutionuserpersons" label="resources.person.fields.identificationNumber" link={false}>
                            <ReferenceField source="personId" reference="person" link={false}>
                                <TextField source="identificationNumber"/>
                            </ReferenceField>
                        </ReferenceField>
                        <BooleanField source="responsible"/>
                    </Datagrid>
                </ReferenceManyField>
            </SimpleForm>
        </Create>
    );
}

export default HierarchicalUnitStaffCreate;

export { personInputData }