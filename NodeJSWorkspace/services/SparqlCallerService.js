async function doSelect(query) {
    const SparqlClient = require('sparql-http-client')
    const endpointUrl = 'http://localhost:3030/lieuFrance'
    const client = new SparqlClient({ endpointUrl })
    let data = [];
    var stream = await client.query.select(query);
    stream.on('data', row => {
        var r = {};
        Object.entries(row).forEach(([key, value]) => {
            r[key] = value.value;
        })
        data.push(r);
    })
    return new Promise((resolve, reject) => {
        stream.on('end', () => {
            resolve(data);
        });
    });
}

module.exports.doSelect = doSelect;