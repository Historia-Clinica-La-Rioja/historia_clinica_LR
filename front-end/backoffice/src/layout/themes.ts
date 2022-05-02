export const lightTheme = {
    palette: {
        background: {
            default: '#f6fafb',
        },
        type: 'light' as 'light',
    },
    sidebar: {
        width: 250,
    },
    overrides: {
        RaMenuItemLink: {
            active: {
                backgroundColor: '#e0eef7',
                color: '#2687c5',
            },
        },

        MuiAppBar: {
            colorSecondary: {
                color: '#3f3f53',
                backgroundColor: '#fff',
            },
        },
    },
    props: {
        MuiButtonBase: {
            // disable ripple for perf reasons
            disableRipple: true,
        },
    },
};
