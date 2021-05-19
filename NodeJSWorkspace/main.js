const path = require('path');
var express = require("express");
var app = express();
var port = 8080;
//require("./services/SparqlCallerService.js");

var templateVar = {
    PageTitle: "My server",
    PageH1: "Voici le contenu du fichier"
}
var gdata;

app.use(express.static("public"));

app.set("view engine", "pug");
app.use(express.static(path.join(__dirname, "/static")));
app.set("views", "./views");

app.listen(port, () => {
    console.log(`Listenning port: ${port}`)
});

app.get("/", function(request, response) {
    response.render("index", templateVar);
});

app.get("/test", async(request, response) => {
    const SparqlClient = require('sparql-http-client')
    const endpointUrl = 'http://localhost:3030/lieuFrance'
    const query = `
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>

        select * WHERE { ?s a :Lieu }`

    const client = new SparqlClient({ endpointUrl })
    let data = [];
    var stream = await client.query.select(query);
    stream.on('data', row => {
        var r = {};
        Object.entries(row).forEach(([key, value]) => {
            // console.log(`${key}: ${value.value} (${value.termType})`)
            r[key] = value;
        })
        data.push(r);
    })
    const prom = new Promise((resolve, reject) => {
        stream.on('end', () => {
            resolve(data);
        });
    });
    prom.then((row) => {
        console.log("TEST")
        console.log(data);
    })
    response.render("index", templateVar);
});