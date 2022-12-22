import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
    Button,
    useRedirect
} from 'react-admin';
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import {makeStyles} from "@material-ui/core/styles";

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
            <ReferenceField source="pacServerProtocol" reference="pacserverprotocols" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="username" />
            <TextField source="urlStow" />
            <TextField source="urlAuth" />
        </SimpleShowLayout>
    </Show>
);

export default GlobalPacsShow;
