import React from 'react';
import {
    Create,
    SimpleForm,
    usePermissions,
    ReferenceInput,
    AutocompleteInput,
    FormDataConsumer,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

const CareLineSelect = ({ formData, ...rest }) => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions.hasAnyAssignment(...BASIC_BO_ROLES);
    return (
        <ReferenceInput
            {...rest}
            reference="carelines"
            sort={{ field: 'description', order: 'ASC' }}
            filterToQuery={searchText => ({description: searchText})}
        >
            <AutocompleteInput optionText="description" optionValue="id" disabled={userIsRootOrAdmin} resettable isRequired={true} />
        </ReferenceInput>);
};

const InstitutionSelect = ({ formData, ...rest }) => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions.hasAnyAssignment(...BASIC_BO_ROLES);
    return (
        <ReferenceInput
            {...rest}
            reference="institutions"
            sort={{ field: 'name', order: 'ASC' }}
            filterToQuery={searchText => ({name: searchText})}
            filter={{ institutionId: formData.institutionId }}
        >
            <AutocompleteInput optionText="name" optionValue="id" disabled={userIsRootOrAdmin} resettable isRequired={true} />
        </ReferenceInput>);
};

const CareLineInstitutionCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>

            <FormDataConsumer>
                {formDataProps => ( <InstitutionSelect {...formDataProps} source="institutionId" />)}
            </FormDataConsumer>

            <FormDataConsumer>
                {formDataProps => ( <CareLineSelect {...formDataProps} source="careLineId" />)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default CareLineInstitutionCreate;
