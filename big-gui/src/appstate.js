export var dataStoreToKpis = {};
export var dataStoreToDimensions = {};
export var measurements = [];

export function loadDataSources() {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            var datasourceList = JSON.parse(xmlHttp.responseText);
            for (var i in datasourceList) {
                var ds = datasourceList[i];
                var opt = document.createElement("option");
                opt.value = ds.name;
                opt.text = ds.displayName;
                document.getElementById("datasourceSelector").appendChild(opt);
                datasourceSelected();
            }
        }
    }
    xmlHttp.open( "GET", "http://localhost:8080/big/metadata/datasources", true ); // false for synchronous request
    xmlHttp.send( null );
    return xmlHttp.responseText;
}

export function datasourceSelected() {
    var selectBox = document.getElementById("datasourceSelector");
    var selectedValue = selectBox.options[selectBox.selectedIndex].value;

    if(selectedValue in dataStoreToKpis) {
        updateKpiSelector(selectedValue);
        updateDimensionSelector(selectedValue);
        return;
    }

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            dataStoreToKpis[selectedValue] = JSON.parse(xmlHttp.responseText);
            updateKpiSelector(selectedValue);
        }
    }
    xmlHttp.open( "GET", "http://localhost:8080/big/metadata/datasource/" + selectedValue + "/kpi/list", true );
    xmlHttp.send( null );

    var xmlHttpD = new XMLHttpRequest();
    xmlHttpD.onreadystatechange = function() {
        if (xmlHttpD.readyState == 4 && xmlHttpD.status == 200) {
            dataStoreToKpis[selectedValue] = JSON.parse(xmlHttpD.responseText);
            dataStoreToDimensions[selectedValue] = JSON.parse(xmlHttpD.responseText);
            updateDimensionSelector(selectedValue);
        }
    }
    xmlHttpD.open( "GET", "http://localhost:8080/big/metadata/datasource/" + selectedValue + "/dimension/list", true );
    xmlHttpD.send( null );
    return xmlHttp.responseText;
}

export function updateKpiSelector(datasource) {
    var myNode = document.getElementById("kpisSelector");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }

    for (var i in dataStoreToKpis[datasource]) {
        var kpi = dataStoreToKpis[datasource][i];
        var opt = document.createElement("option");
        opt.value = kpi.name + "###" + kpi.offeredMetric;
        opt.text = kpi.displayName;
        document.getElementById("kpisSelector").appendChild(opt);
    }
}

export function updateDimensionSelector(datasource) {
    var myNode = document.getElementById("dimensionsSelector");
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }

    for (var i in dataStoreToDimensions[datasource]) {
        var dimension = dataStoreToDimensions[datasource][i];
        var opt = document.createElement("option");
        opt.value = dimension.name;
        opt.text = dimension.displayName;
        document.getElementById("dimensionsSelector").appendChild(opt);
    }
}