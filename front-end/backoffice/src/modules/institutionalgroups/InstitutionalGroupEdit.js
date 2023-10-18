import React, { Fragment } from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    ReferenceInput,
    TabbedShowLayout,
    Tab,
    SelectInput,
    required,
    maxLength
} from 'react-admin';

import {AddInstitutionToGroup, AddUserToGroup, ShowInstitutions, ShowUsers} from './InstitutionalGroupShow';

const InstitutionalGroupEdit = props => {
    return (
        <Edit {...props}>
            <SimpleForm>
                <ReferenceInput
                    reference="institutionalgrouptypes"
                    source="typeId">
                    <SelectInput optionText="value" optionValue="id" validate={[required()]}/>
                </ReferenceInput>
                <TextInput source="name" validate={[required(), maxLength(100)]} />
                <Fragment>
                    <TabbedShowLayout>
                        <Tab label="Instituciones" id="instituciones">
                            <AddInstitutionToGroup />
                            <ShowInstitutions />
                        </Tab>
                        <Tab label="Usuarios" id="usuarios">
                            <AddUserToGroup />
                            <ShowUsers />
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
            </SimpleForm>
        </Edit>
    )
};

export default InstitutionalGroupEdit;
