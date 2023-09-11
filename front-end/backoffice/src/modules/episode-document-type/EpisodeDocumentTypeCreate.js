import {
    Create,
    required,
    SimpleForm,
    TextInput,
    usePermissions,
    SelectInput,
    FormDataConsumer,
    useTranslate
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";
import RichTextInput from 'ra-input-rich-text';
import {makeStyles} from "@material-ui/core/styles";

const EpisodeDocumentTypeCreate = (props) => {
    const {permissions} = usePermissions();
    const classes = RichTextStyles();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Create {...props} hasCreate={userIsAdminInstitutional}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list" className={classes.styles}>
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
            {userIsAdminInstitutional && <SelectInput source="consentId" label="Tipo de consentimiento" defaultValue={ConsentTypes()[0].id} choices={ConsentTypes()} />}

            {userIsAdminInstitutional && (
                <FormDataConsumer>
                    {formDataProps => (<RichTextBody {...formDataProps}/>)}
                </FormDataConsumer>)
            }
        </SimpleForm>
    </Create>)
};

export const RichTextStyles = makeStyles({
    styles: {
        width: '45%',
    },
});

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
    const translate = useTranslate();
    return (formData.consentId !== ConsentTypes()[0].id) ? <RichTextInput source="richTextBody" label="Detalles del documento" defaultValue={translate('resources.episodedocumenttypes.fields.body')} validate={[required()]} fullWidth/> : null
}

export default EpisodeDocumentTypeCreate;