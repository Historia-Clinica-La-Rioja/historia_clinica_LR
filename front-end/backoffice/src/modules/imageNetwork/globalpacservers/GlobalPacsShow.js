import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    Labeled,
    TopToolbar,
    EditButton,
    Button,
    useRecordContext,
    useRedirect,
    BooleanField

} from 'react-admin';
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import {makeStyles} from "@material-ui/core/styles";

const CENTRO_DE_DIAGNOSTICO = 2;

const PacShowActions = ({ data }) => {
    const useStyles = makeStyles({
        button: {
            marginRight: '1%',
        },
        deleteButton: {
            marginLeft: 'auto',
        }
    });
    const classes = useStyles();
    const redirect = useRedirect();
    const goBack = () => {
        redirect('/pacservers');
    }
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <Button
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                    label="sgh.components.customtoolbar.backButton"
                    onClick={goBack}>
                    <ArrowBackIcon />
                </Button>
                <EditButton
                    basePath="/pacservers"
                    record={{ id: data.id }}
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                />
            </TopToolbar>
        )
};
const InstitutionField = ({formData}) => {
    const record = useRecordContext(formData);
    const label = "resources.pacservers.fields.institutionId" ;
    return record.pacServerType === CENTRO_DE_DIAGNOSTICO ? (
        <Labeled label={label}>
         <ReferenceField source="institutionId" reference="institutions" link={false}>
            <TextField source="name" />
         </ReferenceField>
         </Labeled>
    ): null
}
const GlobalPacsShow = props => (
    <Show actions={<PacShowActions />} {...props}>
        <SimpleShowLayout>
            <TextField source="id" />
            <TextField source="name" />
            <TextField source="aetitle" />
            <TextField source="domain" />
            <ReferenceField source="pacServerType" reference="pacservertypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <InstitutionField {...props}  />)
            <ReferenceField source="pacServerProtocol" reference="pacserverprotocols" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <Labeled label="resources.pacservers.fields.active">
               <BooleanField source="active" />
            </Labeled>
        </SimpleShowLayout>
    </Show>
);

export default GlobalPacsShow;
