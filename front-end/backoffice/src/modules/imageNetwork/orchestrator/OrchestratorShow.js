import React from 'react';
import {
    Show,
    TextField,
    TopToolbar,
    EditButton,
    Button,
    ReferenceField,
    SimpleForm,
    useRedirect
} from 'react-admin';
import ArrowBackIcon from "@material-ui/icons/ArrowBack";
import {makeStyles} from "@material-ui/core/styles";

const OrchestratorActions = ({ data }) => {
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
        redirect('/orchestrator');
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
                    basePath="/orchestrator"
                    record={{ id: data.id }}
                    variant="outlined"
                    color="primary"
                    size="medium"
                    className={classes.button}
                />
            </TopToolbar>
        )
};
const OrchestratorShow = props => (
    <Show actions={<OrchestratorActions />} {...props}>
        <SimpleForm>
            <TextField source="name" />
            <TextField source="baseTopic" />
            <ReferenceField link={false}  source="sectorId" reference="sectors">
                <TextField source="description"/>
            </ReferenceField>
        </SimpleForm>
    </Show>
);


export default OrchestratorShow;
