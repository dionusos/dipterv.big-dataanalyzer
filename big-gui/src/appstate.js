import {GoogleCharts} from 'google-charts';
var ID = 0;

GoogleCharts.load(drawChart);
export var dataStoreToKpis = {};
export var dataStoreToDimensions = {};
export var measurements = [];

export var callbacks = [];

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
    if(selectBox === null) {
        return;
    }
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

export function addNewMeasurement() {
    var selectBox = document.getElementById("datasourceSelector");
    var selectedDatasource = selectBox.options[selectBox.selectedIndex].value;

    var selectedKpis = getSelectValues(document.getElementById("kpisSelector"));
    var selectedDimensions = getSelectValues(document.getElementById("dimensionsSelector"));

    var kpisToQuery = [];
    var dimensionsToQuery = [];
    for(var i in selectedKpis) {
        var k = selectedKpis[i].split("###");
        var k2 = {};
        k2.name = k[0];
        k2.offeredMetric = k[1];
        kpisToQuery.push(k2);
    }

    for(var i in selectedDimensions) {
        var d = selectedDimensions[i];
        var d2 = {};
        d2.name = d;
        dimensionsToQuery.push(d2);
    }

    var measurement = {};
    measurement.id = nextID();
    measurement.datasource = selectedDatasource;
    measurement.kpis = kpisToQuery;
    measurement.drilldowns = [];

    var drilldown = {};
    drilldown.id = nextID();
    drilldown.dimensions = dimensionsToQuery;

    measurement.drilldowns.push(drilldown);

    measurements.push(measurement);

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            var dataResponse = JSON.parse(xmlHttp.responseText);
            var header = []

            var dimensionIndexes = [];
            for (var i in dataResponse.header) {
                var h = dataResponse.header[i]
                if (h.kpi === null) {
                    dimensionIndexes.push(i);
                }
            }

            var dimHead = [];
            var x = dataResponse.header.slice(0,dimensionIndexes.length);
            for(var i in x) {
                dimHead.push(x[i].dimension);
            }
            header.push(dimHead.join(','));
            for (var i in dataResponse.header) {
                var h = dataResponse.header[i]
                if (!dimensionIndexes.includes(i)) {
                    header.push(h.kpi);
                }
            }

            var dataMatrix = [];
            for (var i in dataResponse.dataMatrix) {
                var row = dataResponse.dataMatrix[i]
                var dataRow = []
                dataRow.push(row.slice(0,dimensionIndexes.length).join(','))
                for (var j in row) {
                    if (!dimensionIndexes.includes(j)) {
                        var val = parseFloat(row[j]);
                        if(val == parseInt(row[j])) {
                            val = parseInt(row[j]);
                        }

                        dataRow.push(val)
                    }
                }
                dataMatrix.push(dataRow);
            }

            drilldown.dataMatrix = dataMatrix;
            drilldown.header = header;

            for(var i in callbacks) {
                callbacks[i].update();
            }
            drawChart(measurement.id, drilldown.id);
        }
    }
    xmlHttp.open( "POST", "http://localhost:8080/big/data", true );
    xmlHttp.setRequestHeader("Content-type", "application/json");
    var params = {"datasource": "example1",
        "kpis": kpisToQuery,
        "dimensions": dimensionsToQuery
    };
    xmlHttp.send( JSON.stringify(params));
    return xmlHttp.responseText;
}

function getSelectValues(select) {
    var result = [];
    var options = select && select.options;
    var opt;

    for (var i=0, iLen=options.length; i<iLen; i++) {
        opt = options[i];

        if (opt.selected) {
            result.push(opt.value || opt.text);
        }
    }
    return result;
}

export function drawChart(mea, dr) {
    if(mea === null || dr === null) {
        return;
    }

    var drilldown = drilldownByMeasurementIdId(mea, dr);
    if(drilldown === null) {
        return;
    }
    var header = drilldown.header;
    var dataMatrix = drilldown.dataMatrix;
    var mix = [];
    mix.push(header);
    for(var i in dataMatrix) {
        mix.push(dataMatrix[i]);
    }
    var data = GoogleCharts.api.visualization.arrayToDataTable(mix);

    var options = {
        title: 'My Daily Activities'
    };

    var chart = new GoogleCharts.api.visualization.ColumnChart(document.getElementById("drilldown_" + mea + "_" + dr));
    chart.draw(data, options);
}

function nextID(){
    let id = ID;
    ID = ID + 1;
    return id;
}

export function measurementById(id) {
    for (var i in measurements) {
        var m = measurements[i];
        if (m.id === id) {
            return m;
        }
    }
    return null;
}

export function drilldownsByMeasurementId(id) {
    var m = measurementById(id);
    if (m === null) {
        return null;
    }
    return m.drilldowns;
}

export function drilldownByMeasurementIdId(meas, drill) {
    var m = measurementById(meas);
    if (m === null) {
        return null;
    }
    for(var i in m.drilldowns){
        var d = m.drilldowns[i];
        if(d.id === drill) {
            return d;
        }
    }
    return null;
}