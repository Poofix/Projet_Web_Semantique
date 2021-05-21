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
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>

    select * WHERE { ?s a :Lieu }`

    //data = list of n-uplet with the query variables
    var data = await CallerService.doSelect(query)
    console.log("ICI", data)
    response.render("index", templateVar);
});


app.get("/capitale", async(request, response) => {
    const queryGenre = `
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        
    select ?genre ?label ?lieu ?nomVille ?note   where {
        ?genre rdfs:label ?label ; rdf:type :Genre. 
        ?film :seDerouleDans ?lieu .
        ?film :aPourGenre  ?genre .
        ?film :aPourNote ?note.
        ?lieu :aPourVille ?nomVille .
     }`

    var dataGenre = await CallerService.doSelect(queryGenre)
    var dico = {};
    var dicotmp = {}
    var tmp = {};

    dataGenre.forEach(element => {
        if (tmp[element.label] == undefined || tmp[element.label] == null) {
            tmp[element.label] = [];
        }

        if (tmp[element.label][element.nomVille] == undefined || tmp[element.label][element.nomVille] == null) {
            tmp[element.label][element.nomVille] = []
        }
        tmp[element.label][element.nomVille].push({ nomVille: element.nomVille, note: parseFloat(element.note) })

    });

    for (var key in tmp) {
        var current = tmp[key];
        for (var k in current) {
            var y = current[k]
            var values = [];
            for (var x in y) {
                values.push(y[x].note)
            }
            if (dicotmp[key] == undefined || dicotmp[key] == null) {
                dicotmp[key] = [];
            }
            dicotmp[key].push({ nomVille: current[k][0].nomVille, values: values })
        }

    };

    for (var key in dicotmp) {
        var current = dicotmp[key];
        for (var k in current) {
            if (dico[key] == undefined || dico[key] == null) {
                dico[key] = [];
            }
            dico[key].push({ nomVille: current[k].nomVille, moyenne: avg(current[k].values) })
        }
    }
    console.log(dico)


    var formatedData = {
        dico: dico
    };
    response.render("capitale", formatedData);
});

app.get("/topVille", async(request, response) => {
    const queryFilter = `
    PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
    PREFIX : <http://www.semanticweb.org/adminetu/ontologies/2021/4/untitled-ontology-9#>
    PREFIX owl: <http://www.w3.org/2002/07/owl#>
    PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
    
    
    select ?nomVille (AVG(?note) as ?moyenne) where {
        {?film :aPourGenre :genre22} UNION {?film :aPourGenre :genre10} UNION {?film :aPourGenre :genre5}. 
          ?film :seDerouleDans ?lieu .
          ?film :aPourNote ?note.
         ?lieu :aPourVille ?nomVille .
    }
    group by ?nomVille
    order by desc (?moyenne) limit 10`

    var data = await CallerService.doSelect(queryFilter)


    var formatedData = {
        villeList: data
    };
    console.log(formatedData)
    response.render("topVille", formatedData);
});


function avg(tab) {
    var moy = 0;
    tab.forEach(el => {
        moy += el;
    })
    return tab.length ? moy / tab.length : 0;
}