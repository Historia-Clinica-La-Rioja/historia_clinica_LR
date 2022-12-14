import React from 'react';
import {
    Button, EditButton,
    Show,
    SimpleShowLayout,
    TextField, TopToolbar,
} from 'react-admin';
import {makeStyles} from "@material-ui/core/styles";
import ArrowBackIcon from "@material-ui/icons/ArrowBack";

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
    const goBack = () => {
        window.history.back();
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

const ImageLvlPacShow = props => (
    <Show actions={<PacShowActions />} {...props}>
        <SimpleShowLayout>
            <TextField source="name" />
            <TextField source="aetitle" />
            <TextField source="domain" />
            <TextField source="port" />
        </SimpleShowLayout>
    </Show>
);

export default ImageLvlPacShow;
