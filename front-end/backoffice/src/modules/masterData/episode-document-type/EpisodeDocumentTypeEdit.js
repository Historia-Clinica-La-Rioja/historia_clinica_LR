import RichTextInput from 'ra-input-rich-text';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    usePermissions,
    FormDataConsumer,
    RichTextField,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import {
  ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
} from '../../roles';
import {
  ConsentTypes,
  RichTextStyles,
} from './EpisodeDocumentTypeCreate';

const EpisodeDocumentTypeEdit = props => {
    const {permissions} = usePermissions();
    const classes = RichTextStyles();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsAdminInstitutional}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>} className={classes.styles}>
            {
                <FormDataConsumer>
                {formDataProps => {
                  const isConsentType = formDataProps.formData.consentId !== ConsentTypes()[0].id;
                  
                  return (
                    <TextInput
                      source="description"
                      validate={[required()]}
                      disabled={isConsentType}
                    />
                  );
                }}
              </FormDataConsumer>
            }
            {userIsAdminInstitutional && <FormDataConsumer>
                {formDataProps => ((formDataProps.formData.consentId !== ConsentTypes()[0].id) ? <RichTextInput source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth/> : null)}
            </FormDataConsumer>}

            {!userIsAdminInstitutional && <FormDataConsumer>
                {formDataProps => ((formDataProps.formData.consentId !== ConsentTypes()[0].id) ? <RichTextField source="richTextBody" label="Detalles del documento" validate={[required()]} fullWidth/> : null)}
            </FormDataConsumer>}
        </SimpleForm>
    </Edit>)
};

export default EpisodeDocumentTypeEdit;