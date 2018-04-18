import {CREATE_DRILLDOWN, CREATE_MEASUREMENT} from "../actions/metadata-actions";
import {
    FILL_DRILLDOWN, ADD_FILTER, REMOVE_FILTER, DELETE_DRILLDOWN,
    UPDATE_DRILLDOWN_LIMIT, UPDATE_DRILLDOWN_CHART_TYPE
} from "../actions/data-actions";

export default function dataReducer(state={}, {type, payload}) {
    let newState = {};
    Object.assign(newState, state);
    switch (type) {
        case (CREATE_MEASUREMENT):
            if (newState.measurements == null) {
                newState.measurements = [];
            }
            newState.measurements.push(payload);
            return newState;
        case (CREATE_DRILLDOWN):
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id === payload.measurement.id) {
                   m.drilldowns.push(payload);
                    break;
                }
            }
            return newState;
        case (FILL_DRILLDOWN):
            let ddId = payload.drilldownId;
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id === ddId.measurementId) {
                    for (var j = 0; j < m.drilldowns.length; ++j) {
                        let d = m.drilldowns[j];
                        if(d.id.id === ddId.id) {
                            fillDrilldownWithDataResponse(d, payload.data);
                            break;
                        }
                    }
                    break;
                }
            }
            return newState;
        case(ADD_FILTER):
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id === payload.measurement) {
                    m.filters.push({dimension: payload.dimension, values: payload.value.split(","), isNegative: false});
                    m.upToDate = false;
                    break;
                }
            }
            return newState;
        case(REMOVE_FILTER):
            for(var i8 = 0; i8 < newState.measurements.length; ++i8) {
                let m = newState.measurements[i8];
                if(m.id === payload.measurement) {
                    let newFilters = [];
                    for(var j8 = 0; j8 < m.filters.length; ++j8) {
                        let f = m.filters[j8];
                        if(payload.dimension != f.dimension) {
                            newFilters.push(f);
                        }
                    }
                    m.filters = newFilters;
                    m.upToDate = false;
                    break;
                }
            }
            return newState;
        case (DELETE_DRILLDOWN):
            var deletedFromMeasurement = -1;
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id !== payload.measurementId) {
                    continue;
                }
                var deleteFrom = -1;
                for (var j = 0; j < m.drilldowns.length; ++j) {
                    let d = m.drilldowns[j];
                    if(d.id.id === payload.id) {
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
                if(newState.measurements[deletedFromMeasurement].drilldowns.length < 1) {
                    var newMeasurements = [];
                    for(var i in newState.measurements) {
                        if(i != deletedFromMeasurement) {
                            newMeasurements.push(newState.measurements[i]);
                        }
                    }
                    newState.measurements = newMeasurements;
                }
            }
            return newState;
        case (UPDATE_DRILLDOWN_LIMIT):
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id !== payload.drilldownId.measurementId) {
                    continue;
                }
                for (var j = 0; j < m.drilldowns.length; ++j) {
                    let d = m.drilldowns[j];
                    if(d.id.id === payload.drilldownId.id) {
                        d.limit = payload.limit;
                        break;
                    }
                }
                break;
            }
            return newState;
        case(UPDATE_DRILLDOWN_CHART_TYPE):
            for(var i = 0; i < newState.measurements.length; ++i) {
                let m = newState.measurements[i];
                if(m.id !== payload.drilldownId.measurementId) {
                    continue;
                }
                for (var j = 0; j < m.drilldowns.length; ++j) {
                    let d = m.drilldowns[j];
                    if(d.id.id === payload.drilldownId.id) {
                        d.chartType = payload.type;
                        break;
                    }
                }
                break;
            }
            return newState;
        default:
            return state;
    }
}

//##########################################################
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
