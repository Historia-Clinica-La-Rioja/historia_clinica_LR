import React from 'react';
import {
    NumberField,
    Show,
    SimpleShowLayout,
    TextField,
    RichTextField
} from 'react-admin';
import {
    RichTextStyles,
} from './EpisodeDocumentTypeCreate';

const EpisodeDocumentTypeShow = props => {
    const classes = RichTextStyles();
    return (<Show {...props}>
        <SimpleShowLayout className={classes.styles}>
            <NumberField source="id" />
            <TextField source="description"/>
            <RichTextField  source="richTextBody" label=""/>
        </SimpleShowLayout>
    </Show>)
};

export default EpisodeDocumentTypeShow;