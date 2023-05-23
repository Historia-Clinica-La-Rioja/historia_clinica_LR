import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    ReferenceManyField,
    Datagrid,
    TextField,
    EditButton,
    ReferenceInput,
    SelectInput,
    required,
    number,
    maxLength,
    minLength,
    ReferenceField,
    usePermissions,
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR, ROOT} from "../roles";
import { CreateHierarchicalUnit, UserIsInstitutionalAdmin } from './InstitutionShow';

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
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
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
                {/*TODO: Aislar esto en un componente. También se usa en show.js*/}
                <ReferenceManyField
                    addLabel={false}
                    reference="sectors"
                    target="institutionId"
                    sort={{field: 'description', order: 'DESC'}}
                    filter={{ deleted: false }}
                >
                    <Datagrid rowClick="show">
                        <TextField source="description"/>
                        <ReferenceField source="sectorTypeId" link={false} reference="sectortypes">
                            <TextField source="description"/>
                        </ReferenceField>
                        <EditButton/>
                    </Datagrid>
                </ReferenceManyField>
                <SectionTitle label="resources.institutions.fields.hierarchicalUnits"/>

                <CreateHierarchicalUnit/>
                <ReferenceManyField
                    id='hierarchicalunits'
                    addLabel={false}
                    reference="hierarchicalunits"
                    target="institutionId"
                    sort={{ field: 'alias', order: 'DESC' }}
                >
                    <Datagrid rowClick="show"
                              empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin unidades jerárquicas definidas</p>}>
                        <TextField source="id" />
                        <TextField source="alias"/>
                        <EditButton disabled={!UserIsInstitutionalAdmin()}/>
                    </Datagrid>
                </ReferenceManyField>
            </SimpleForm>
        </Edit>
    );
}

export default InstitutionEdit;
