module.exports = {
    queryTransformer: (query, { securityContext }) => {
        console.log(`query: ${JSON.stringify(query)}`);
        if (securityContext) {
            const { roles, userId } = securityContext;
            console.log(`userId ${userId} with roles ${roles.map(function(item) { return JSON.stringify(item);}).toString()}`);
        }
        else console.log(`securityContext is undefined => ${securityContext}`);
        return query;
    },
};