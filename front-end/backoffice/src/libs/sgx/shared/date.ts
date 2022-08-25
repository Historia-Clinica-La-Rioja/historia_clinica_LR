enum Month {
    January = 0,
    June = 5,
    July = 6,
};

const locales = 'es-AR';
const options = { timeZone: 'UTC' };

const dateFormat = new Intl.DateTimeFormat(locales, {
    ...options,
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
});

const dayFormat = new Intl.DateTimeFormat(locales, {
    ...options,
    day: 'numeric',
    month: 'long',
    year: 'numeric',
});

const dateFormatterParams = {locales, options};

const dateFormatter = (dateStr: string): string => dateStr ? dateFormat.format(new Date(dateStr)) : '';

const dayFormatter = (dateStr: string): string => dateStr ? dayFormat.format(new Date(dateStr)) : '';

export { 
    dateFormatterParams, 
    dateFormatter, 
    dayFormatter, 
    Month,
};
