const path = require('path');
var express = require("express");
var app = express();
var port = 8080;
var CallerService = require("./services/SparqlCallerService.js");

var templateVar = {
    PageTitle: "My server",
    PageH1: "Voici le contenu du fichier"
}
var gdata;

app.use(express.static("public"));

app.set("view engine", "ejs");
app.use(express.static(path.join(__dirname, "/static")));
app.set("views", "./views");

app.listen(port, () => {
    console.log(`Listenning port: ${port}`)
});

// home page
app.get("/", function(request, response) {
    response.render("index", templateVar);
});


app.get("/test", async(request, response) => {
    const query = `
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>

    select * WHERE { ?s a :Lieu }`

    //data = list of n-uplet with the query variables
    var data = await CallerService.doSelect(query)
    console.log("ICI", data)
    response.render("index", templateVar);
});


app.get("/capitale", async(request, response) => {
    const query = `
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>

    select * WHERE { ?s a :Lieu }`

    var data = await CallerService.doSelect(query)


    var formatedData = data;
    response.render("capitale", formatedData);
});

app.get("/topVille", async(request, response) => {
    const query = `
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>

    select * WHERE { ?s a :Lieu }`

    var data = await CallerService.doSelect(query)


    var formatedData = data;
    response.render("topVille", formatedData);
});