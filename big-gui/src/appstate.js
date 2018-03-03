import {GoogleCharts} from 'google-charts';
var ID = 0;
var backend = "http://localhost:8080/big"

GoogleCharts.load(drawChart);
export var dataStoreToKpis = {};
export var dataStoreToDimensions = {};
export var measurements = [];
export var measurementFilters = [];

export function loadState() {
    if(localStorage['measurements'] !== undefined) {
        measurements = JSON.parse(localStorage['measurements']);
        notify();
    }
}

export function saveState() {
    localStorage['measurements'] = JSON.stringify(measurements);
}

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
    xmlHttp.open( "GET", backend + "/metadata/datasources", true ); // false for synchronous request
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
        updateDimensionSelector(selectedValue, "dimensionsSelector");
        updateDimensionSelector(selectedValue, "dimensionFilterAdderSelector");
        return;
    }

    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200) {
            dataStoreToKpis[selectedValue] = JSON.parse(xmlHttp.responseText);
            updateKpiSelector(selectedValue);
        }
    }
    xmlHttp.open( "GET", backend + "/metadata/datasource/" + selectedValue + "/kpi/list", true );
    xmlHttp.send( null );

    var xmlHttpD = new XMLHttpRequest();
    xmlHttpD.onreadystatechange = function() {
        if (xmlHttpD.readyState == 4 && xmlHttpD.status == 200) {
            dataStoreToDimensions[selectedValue] = JSON.parse(xmlHttpD.responseText);
            updateDimensionSelector(selectedValue, "dimensionsSelector");
            updateDimensionSelector(selectedValue, "dimensionFilterAdderSelector");
        }
    }
    xmlHttpD.open( "GET", backend + "/metadata/datasource/" + selectedValue + "/dimension/list", true );
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

export function updateDimensionSelector(datasource, dimensionSelectorClass) {
    var myNode = document.getElementsByClassName(dimensionSelectorClass)[0];
    while (myNode.firstChild) {
        myNode.removeChild(myNode.firstChild);
    }

    for (var i in dataStoreToDimensions[datasource]) {
        var dimension = dataStoreToDimensions[datasource][i];
        var opt = document.createElement("option");
        opt.value = dimension.name;
        opt.text = dimension.displayName;
        document.getElementsByClassName(dimensionSelectorClass)[0].appendChild(opt);
    }
}

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

            notify();
            drawChart(measurement.id, drilldown.id);
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
    return xmlHttp.responseText;
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
        title: header.slice(1).join(",") + " by " + header[0]
    };

    var chart = createChart(document.getElementById("drilldown_" + mea + "_" + dr), drilldown.chartType);
    drilldown.chart = chart;
    chart.draw(data, options);

    GoogleCharts.api.visualization.events.addListener(chart, 'select', function() {
        var selection = drilldownByMeasurementIdId(mea, dr).chart.getSelection();
        if(selection === undefined || selection.length == 0) {

            return;
        }
        var r = drilldownByMeasurementIdId(mea, dr).dataMatrix[selection[0].row];
        // Get the modal
        var modal = document.getElementById('myModal'+ mea);

        // Get the <span> element that closes the modal
       // var span = document.getElementsByClassName("close)[0];

        modal.style.display = "block";

        // When the user clicks on <span> (x), close the modal
       /* span.onclick = function() {
            modal.style.display = "none";
        }*/

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        var fixedDimensionAndValue = document.getElementsByClassName("fixedDimensionAndValue" + mea)[0];
        fixedDimensionAndValue.innerHTML = header[0] + "=" + r[0];

    });
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

export function datasourceByMeasurement(measurementId) {
    return measurementById(measurementId).datasource;
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

            notify();
            drawChart(measurement.id, drilldown.id);
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

export function deleteDrilldownsFrom(drilldownId) {
    for (var i in measurements) {
        var m = measurements[i];
        var deleteFrom = -1;
        for(var j in m.drilldowns){
            var d = m.drilldowns[j];
            if(d.id === drilldownId) {
                deleteFrom = j;
                break;
            }
        }
        if(deleteFrom != -1) {
            m.drilldowns = m.drilldowns.slice(0, deleteFrom);
            break;
        }
    }
    notify();
}

function createChart(div, chartType) {
    if(chartType === "column") {
        return new GoogleCharts.api.visualization.ColumnChart(div);
    }

    if(chartType === "pie") {
        return new GoogleCharts.api.visualization.PieChart(div);
    }

    if(chartType === "timeseries") {
        return new GoogleCharts.api.visualization.LineChart(div);
    }
}