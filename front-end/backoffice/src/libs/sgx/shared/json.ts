
const safeParseJson = (text: string): any => {
    try {
        return text ? JSON.parse(text) : undefined;
    } catch(e) {
        console.warn('Please check JSON response', e);
    }
    return undefined;
};

const safeStringifyJson = (object: any): string | undefined => {
    try {
        return object ? JSON.stringify(object) : undefined;
    } catch(e) {
        console.warn('Please check Object response', e);
    }
    return undefined;
};

export { safeParseJson, safeStringifyJson };
