import React, { isValidElement, useState, useEffect } from 'react';
import {
    Error as ErrorView,
    Loading,
    useDataProvider,
    Labeled,
} from 'react-admin';

import classnames from 'classnames';

const ReferenceFields = ({
    basePath,
    children,
    reference,
    record: { id },
}) => {
    const dataProvider = useDataProvider();

    const [record, setRecord] = useState();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState();

    useEffect(() => {
        dataProvider.getOne(reference, { id })
            .then(({ data }) => {
                setRecord(data);
                setLoading(false);
            })
            .catch(error => {
                setError(error);
                setLoading(false);
            })
    }, [id, reference, dataProvider]);

    if (loading) {
        return <Loading />;
    }
    if (error) {
        return <ErrorView />
    }
    if (!record) {
        return null;
    }

    return (
        <span>
            {React.Children.map(children, field =>
                field && isValidElement(field) ? (
                    <div
                        key={field.props.source}
                        className={classnames(
                            'ra-field',
                            `ra-field-${field.props.source}`,
                            field.props.className
                        )}
                    >
                        {field.props.addLabel ? (
                            <Labeled
                                label={field.props.label}
                                source={field.props.source}
                                basePath={basePath}
                                record={record}
                                resource={reference}
                            >
                                {field}
                            </Labeled>
                        ) : typeof field.type === 'string' ? (
                            field
                        ) : (
                            React.cloneElement(field, {
                                basePath,
                                record,
                                resource: reference,
                            })
                        )}
                    </div>
                ) : null
            )}
        </span>
    );
};

export default ReferenceFields;
