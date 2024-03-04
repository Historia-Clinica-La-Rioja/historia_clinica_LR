import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    ReferenceInput,
    SelectInput,
    required,
    number,
    maxLength,
    minLength,
    usePermissions,
} from 'react-admin';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import SectionTitle from '../../components/SectionTitle';
import CustomToolbar from '../../components/CustomToolbar';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';
import {
    CreateHierarchicalUnit,
    HierarchicalUnitTabs,
    ShowSectors
} from './InstitutionShow';

const Dependency = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="dependencies"
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" validate={[required()]} />
        </ReferenceInput>);

};
const InstitutionEdit = props => {
    const {permissions} = usePermissions();
    const userIsRootOrAdmin = permissions?.hasAnyAssignment(...BASIC_BO_ROLES);
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={userIsRootOrAdmin}/>}>
                <TextInput source="name" validate={[required()]}/>
                <TextInput source="website"/>
                <TextInput source="phone" validate={[
                    required(),
                    maxLength(20)]}/>
                <TextInput source="email" type="email" validate={[required()]}/>
                <TextInput source="cuit"
                           validate={[
                               required(),
                               number(),
                               maxLength(20)]}/>
                <TextInput source="sisaCode" validate={[
                    required(),
                    number(),
                    minLength(14),
                    maxLength(14)]}/>
                <Dependency source="dependencyId"/>
                <TextInput source="provinceCode"/>
                <SectionTitle label="resources.institutions.fields.sectors"/>
                <CreateRelatedButton
                    reference="sectors"
                    refFieldName="institutionId"
                    label="resources.sectors.createRelated"
                />
                <ShowSectors/>
                <SectionTitle label="resources.institutions.fields.hierarchicalUnits"/>
                <CreateHierarchicalUnit/>
                <HierarchicalUnitTabs {...props}/>
            </SimpleForm>
        </Edit>
    );
}

export default InstitutionEdit;
