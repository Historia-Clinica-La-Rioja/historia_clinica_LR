import React from 'react';
import { SaveButton, DeleteButton, Toolbar, Button } from 'react-admin';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles({
    button: {
        marginRight: '1%',
    },
    deleteButton: {
        marginLeft: 'auto',
    }
});

const CustomToolbar = ({isEdit, ...props}) => {
    const classes = useStyles();

    const goBack = () => {
        window.history.back();
    }

    const deleteButton = () => {
        return <DeleteButton className={classes.deleteButton}/>
    }

    return (
        <Toolbar {...props}>
            <Button
                variant="outlined"
                color="primary"
                size="medium"
                className={classes.button}
                label="sgh.components.customtoolbar.backButton"
                onClick={goBack}>
                    <ArrowBackIcon />
            </Button>
            <SaveButton />
            {isEdit ? deleteButton() : null}
        </Toolbar>
    )

}

export default CustomToolbar;