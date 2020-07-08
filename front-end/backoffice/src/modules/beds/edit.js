import React from 'react';
import {
    Edit,
    TextInput,
    ReferenceInput,
    SelectInput,
    SimpleForm,
    required,
    BooleanInput,
    DateField,
    ReferenceManyField,
    Datagrid,
    Pagination
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";

const BedEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="bedNumber" validate={[required()]} />

            <SgxSelectInput source="roomId" element="rooms" optionText="description" alwaysOn allowEmpty={false}/>

            <ReferenceInput
                source="bedCategoryId"
                reference="bedcategories"
                sort={{ field: 'description', order: 'ASC' }}
                validate={[required()]}
            >
                <SelectInput optionText="description" optionValue="id"/>
            </ReferenceInput>

            <BooleanInput source="enabled" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="available" validate={[required()]} disabled={false} initialValue={true}/>
            <BooleanInput source="free" validate={[required()]} disabled={false} initialValue={true}/>

            <ReferenceManyField
                addLabel={true}
                label="resources.beds.fields.internmentepisodes"
                reference="internmentepisodes"
                target="bedId"
                sort={{ field: 'entryDate', order: 'DESC' }}
                filter={{ statusId: 1 }}
                pagination={<Pagination />}
            >
                <Datagrid>
                    <DateField source="entryDate" />
                </Datagrid>
            </ReferenceManyField>

        </SimpleForm>
    </Edit>
);

export default BedEdit;
