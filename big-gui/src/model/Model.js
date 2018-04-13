

export var backend = "http://192.168.0.52:8080/big";

export var dataStoreToKpis = {};
export var dataStoreToDimensions = {};
export var datasourceList = [];
export var selectedDatasource;
export function selectDatasource(dataSource) {
    selectedDatasource = dataSource;
}
export function setDatasourceList(list) {
    datasourceList = list;
}
export var measurements = [];
export function setMeasurements(list) {
    measurements = list;
}
export var measurementFilters = [];

export function makeHttpRequest(method, url, callback, payload) {
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = callback;
    xmlHttp.open( method, url, true );
    if(method === "POST") {
        xmlHttp.setRequestHeader("Content-type", "application/json");
        xmlHttp.send(JSON.stringify(payload));
    } else {
        xmlHttp.send(null);
    }
}







export function loadState() {
    if(localStorage['measurements'] !== undefined) {
        setMeasurements(JSON.parse(localStorage['measurements']));
        notify();
    }
}

export function saveState() {
    localStorage['measurements'] = JSON.stringify(measurements);
}

export var callbacks = [];

function createMeasurement(selectedDatasource, kpisToQuery, filters) {
    var measurement = {};
    measurement.id = nextID();
    measurement.datasource = selectedDatasource;
    measurement.kpis = kpisToQuery;
    measurement.drilldowns = [];
    if(filters === undefined) {
        filters = [];
    }
    measurement.filters = filters;
    measurements.push(measurement);
    return measurement;
}

function createDrilldown(measurement, dimensionsToQuery, filters) {
    var drilldown = {};
    drilldown.id = nextID();
    drilldown.measurementId = measurement.id;
    drilldown.dimensions = dimensionsToQuery;
    drilldown.chartType = "column";
    measurement.drilldowns.push(drilldown);
    if(filters === undefined) {
        filters = [];
    }
    drilldown.filters = filters;

    return drilldown;
}

export function addNewMeasurement() {
    var selectBox = document.getElementById("datasourceSelector");
    var selectedDatasource = selectBox.options[selectBox.selectedIndex].value;

    var selectedKpis = getSelectValues(document.getElementById("kpisSelector"));
    var selectedDimensions = getSelectValues(document.getElementsByClassName("dimensionsSelector")[0]);

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

    var measurement = createMeasurement(selectedDatasource, kpisToQuery, generateMeasurementFilters());

    var drilldown = createDrilldown(measurement, dimensionsToQuery);

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            var dataResponse = JSON.parse(xmlHttp.responseText);

            fillDrilldownWithDataResponse(drilldown, dataResponse);
            if (drilldown.callback !== null) {
                drilldown.callback();
            }
        }
    }
    xmlHttp.open( "POST", backend + "/data", true );
    xmlHttp.setRequestHeader("Content-type", "application/json");
    var params = {"datasource": selectedDatasource,
        "kpis": kpisToQuery,
        "dimensions": dimensionsToQuery,
        "filters": measurement.filters
    };
    xmlHttp.send( JSON.stringify(params));
    notify();
}

function generateMeasurementFilters() {
    var dimNames = document.getElementsByClassName("dimensionName");
    var dimValues = document.getElementsByClassName("dimensionValue");
    var fils = [];
    for(var i in dimNames){
        if( i === "length") {
            break;
        }
        var fil = {"dimension": dimNames[i].innerHTML};
        fil.values = String(dimValues[i].value).split(",");
        if (fil.values.length == 1 && fil.values[0] === '') {
            continue;
        }
        fil.isNegative= "false"
        fils.push(fil);
    }
    return fils;
}

export function getSelectValues(select) {
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
var ID = 0;
export function nextID(){
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

export function newDrilldown(measurementId) {
    var select = document.getElementsByClassName("drilldownDimensionsSelector" + measurementId)[0];
    if (select === undefined) {
        return;
    }
    var measurement = measurementById(measurementId);

    var drilldown = createDrilldown(measurement, getSelectValues(select));
    var fixedDimensionAndValue = document.getElementsByClassName("fixedDimensionAndValue" + measurementId)[0];
    var split = fixedDimensionAndValue.innerHTML.split("=");
    if(split[0] !== "") {
        var ds = split[0].split(",");
        var vs = split[1].split(",");
        for (var i in ds) {
            var dim = ds[i];
            var valu = vs[i];
            drilldown.filters.push(
                {
                    "dimension": dim,
                    "values": [valu],
                    "isNegative": "false"
                }
            );
        }
    }

    var selectedChartType = getSelectValues(document.getElementsByClassName("charTypeSelector"+measurementId)[0])[0];
    drilldown.chartType=selectedChartType;
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            var dataResponse = JSON.parse(xmlHttp.responseText);

            fillDrilldownWithDataResponse(drilldown, dataResponse);
            if (drilldown.callback !== null) {
                drilldown.callback();
            }
        }
    }
    xmlHttp.open( "POST", backend + "/data", true );
    xmlHttp.setRequestHeader("Content-type", "application/json");
    var filters = [];
    filters = filters.concat(measurement.filters);
    for(var i in measurement.drilldowns) {
        var d = measurement.drilldowns[i];
        filters = filters.concat(d.filters);
    }

    var dimensionsToQuery = [];
    for(var i in drilldown.dimensions) {
        var d = drilldown.dimensions[i];
        var d2 = {};
        d2.name = d;
        dimensionsToQuery.push(d2);
    }

    var params = {"datasource": measurement.datasource,
        "kpis": measurement.kpis,
        "dimensions": dimensionsToQuery,
        "filters": filters
    };
    xmlHttp.send( JSON.stringify(params));
    document.getElementById('myModal' + measurementId).style.display = "none";
    notify();
}
function fillDrilldownWithDataResponse(drilldown, dataResponse) {
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
}

export function notify() {
    for (var i in callbacks) {
        callbacks[i].update();
    }
    //saveState();
}

export function deleteDrilldownsFrom(drilldown) {
    var deletedFromMeasurement = -1;
    for (var i in measurements) {
        var m = measurements[i];
        if(m.id !== drilldown.measurementId) {
            continue;
        }
        var deleteFrom = -1;
        for(var j in m.drilldowns){
            var d = m.drilldowns[j];
            if(d.id === drilldown.id) {
                deleteFrom = j;
                break;
            }
        }
        if(deleteFrom != -1) {
            m.drilldowns = m.drilldowns.slice(0, deleteFrom);
            deletedFromMeasurement = i;

            break;
        }
    }
    if(deletedFromMeasurement != -1) {
        if(measurements[deletedFromMeasurement].drilldowns.length < 1) {
            var newMeasurements = [];
            for(var i in measurements) {
                if(i != deletedFromMeasurement) {
                    newMeasurements.push(measurements[i]);
                }
            }
            setMeasurements(newMeasurements);
        }
    }

    notify();
}

export function removeFilterFromNewMeasurement(dimension) {
    var newMeasurementFilters = [];
    for(var i in measurementFilters) {
        if(measurementFilters[i].dimension != dimension) {
            newMeasurementFilters.push(measurementFilters[i]);
        }
    }
    measurementFilters = newMeasurementFilters;
    notify();
}
