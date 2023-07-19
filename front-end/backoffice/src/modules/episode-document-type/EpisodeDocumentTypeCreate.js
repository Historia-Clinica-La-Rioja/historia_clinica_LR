import {
    Create,
    required,
    SimpleForm,
    TextInput,
    usePermissions,
    SelectInput,
    FormDataConsumer,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";

const EpisodeDocumentTypeCreate = (props) => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Create {...props} hasCreate={userIsAdminInstitutional}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list">
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
            <SelectInput source="consentId" label="Tipo de consentimiento" defaultValue={ConsentTypes()[0].id} choices={ConsentTypes()} />

            <FormDataConsumer>
                {formDataProps => (<RichTextBody {...formDataProps}/>)}
            </FormDataConsumer>
        </SimpleForm>
    </Create>)
};

export const ConsentTypes = () => {
    return [
        {
            id: 1, name: 'Documento regular'
        },
        {
            id: 2, name: 'Consentimiento informado de ingreso',
        },
        {
            id: 3, name: 'Consentimiento informado quirúrgico'
        }
    ]
}

const RichTextBody = ({formData}) => {
    return (formData.consentId !== ConsentTypes()[0].id) ? <TextInput source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth multiline/> : null
}

export default EpisodeDocumentTypeCreate;