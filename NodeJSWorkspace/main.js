const path = require('path');
var express = require("express");
var app = express();
var port = 8080;


var templateVar = {
    PageTitle: "My server",
    PageH1: "Voici le contenu du fichier"
}


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

app.get("/test", function(request, response) {

    response.render("testPage");
});