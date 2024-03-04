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
    BooleanField,
    required,
    FormDataConsumer
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { renderPersonData } from '../hierarchicalunits/HierarchicalUnitShow';

const personInputData = (person) => {
    return person ? `${person.completeName ? person.completeName : "" } ${person.completeLastName ? person.completeLastName : "" } ${person.identificationNumber ? "- " + person.identificationNumber : "" }` : null;
};

const HierarchicalUnitStaffCreate = (props) => {

    return props.location?.state?.record?.userId !== undefined ? (
        <FormFromUser {...props}/>
    ) : (
        <FormFromHierarchicalUnit {...props}/>
    );
}

const HierarchicalUnitsByInstitutionId = ({formData, ...rest}) => {
    return (
        <ReferenceInput
            source="hierarchicalUnitId"
            reference="hierarchicalunits"
            label="resources.hierarchicalunitstaff.fields.hierarchicalUnitId"
            filter={{institutionId: formData.institutionId}}
        >
            <AutocompleteInput optionText="alias" optionValue="id" validate={required()}/>
        </ReferenceInput>
    )
}

const FormFromUser = (props) => {
    const userRedirect = `/users/${props.location?.state?.record?.userId}/show`
    return (
        <Create {...props}>
            <SimpleForm redirect={userRedirect} toolbar={<CustomToolbar />}>
                <ReferenceField
                    label="resources.userroles.fields.userId"
                    source="userId" reference="users" link={false}>
                    <TextField source="username" />
                </ReferenceField>
                <ReferenceInput
                    source="institutionId"
                    reference="institutions"
                    label="resources.hierarchicalunits.fields.institutionId"
                    sort={{ field: 'name', order: 'ASC' }}
                    filterToQuery={searchText => ({name: searchText})}
                >
                    <AutocompleteInput optionText="name" optionValue="id" validate={required()}/>
                </ReferenceInput>
                <FormDataConsumer>
                    {formDataProps => (
                        <HierarchicalUnitsByInstitutionId {...formDataProps}/>)}
                </FormDataConsumer>
                <BooleanInput source="responsible" disabled={false} initialValue={false}/>
            </SimpleForm>
        </Create>
    )
}

const FormFromHierarchicalUnit = (props) => {
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
                    <AutocompleteInput optionText={personInputData} optionValue="id" validate={[required()]} />
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
    )
}

export default HierarchicalUnitStaffCreate;

export { personInputData }