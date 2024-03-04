import { Fragment } from 'react'
import { useForm } from 'react-final-form';
import { 
    Create, 
    SimpleForm, 
    AutocompleteInput, 
    ReferenceInput, 
    required,
    FormDataConsumer
 } from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

const Province = (sourceId) => {
    const form = useForm();

    return (
    <ReferenceInput
        {...sourceId}
        label='Provincia (Opcional)'
        reference="provinces" 
        perPage={100} //Así traemos todas las provincias de una
        sort={{ field: 'description', order: 'ASC' }}
        onChange={value => {
            form.change('departmentId', null);
            form.change('institutionId', null);
        }}
    >
        <AutocompleteInput optionText="description" optionValue="id" />
    </ReferenceInput>);
};

const Department = ({ formData, ...rest }) => {
    const form = useForm();
    // Wait for the province to be selected
    if (!formData.provinceId) return null;

    return (
        <Fragment>
            <ReferenceInput
                {...rest}
                label='Partido (Opcional)'
                reference="departments"
                filter={{ provinceId: formData ? formData.provinceId : '' }}
                sort={{ field: 'description', order: 'ASC' }}
                perPage={1000} //Así traemos todos los departamentos
                onChange={value => form.change('institutionId', null)}
            >
                <AutocompleteInput optionText="description" optionValue="id"/>
            </ReferenceInput>
        </Fragment>
    );
};

const Institution = ({ formData, ...rest }) => {
    return <ReferenceInput
        {...rest}
        reference="departmentinstitutions"
        filter={{ departmentId: formData.departmentId ? formData.departmentId : '' }}
        sort={{ field: 'name', order: 'ASC' }}
        filterToQuery={searchText => ({name: searchText})}
    >
        <AutocompleteInput optionText="name" optionValue="id" validate={[required()]} />
    </ReferenceInput>
};



const InstitutionalGroupInstitutionCreate = (props) => {
    const redirect=`/institutionalgroups/${props?.location?.state?.record?.institutionalGroupId}/show`;
    return (
        <Create {...props}> 
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <Province source="provinceId" />
                <FormDataConsumer>
                    {formDataProps => ( <Department {...formDataProps} source="departmentId"/>)}
                </FormDataConsumer>
                <FormDataConsumer>
                    {formDataProps => ( <Institution {...formDataProps} source="institutionId"/>)}
                </FormDataConsumer>
            </SimpleForm>
        </Create>
    );
};

export default InstitutionalGroupInstitutionCreate;
