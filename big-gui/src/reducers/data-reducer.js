import {CREATE_MEASUREMENT} from "../actions/metadata-actions";
import {FILL_DRILLDOWN, ADD_FILTER, REMOVE_FILTER} from "../actions/data-actions";

export default function dataReducer(state={}, {type, payload}) {
    switch (type) {
        case (CREATE_MEASUREMENT):
            let newState = {};
            Object.assign(newState, state);
            if (newState.measurements == null) {
                newState.measurements = [];
            }
            newState.measurements.push(payload);
            return newState;
        case (FILL_DRILLDOWN):
            let newState2 = {};
            Object.assign(newState2, state);
            let ddId = payload.drilldownId;
            for(var i = 0; i < newState2.measurements.length; ++i) {
                let m = newState2.measurements[i];
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
            return newState2;
        case(ADD_FILTER):
            let newState7 = {};
            Object.assign(newState7, state);
            for(var i = 0; i < newState7.measurements.length; ++i) {
                let m = newState7.measurements[i];
                if(m.id === payload.measurement) {
                    m.filters.push({dimension: payload.dimension, values: payload.value.split(","), isNegative: false});
                    m.upToDate = false;
                    break;
                }
            }
            return newState7;
        case(REMOVE_FILTER):
            let newState8 = {};
            Object.assign(newState8, state);
            for(var i8 = 0; i8 < newState8.measurements.length; ++i8) {
                let m = newState8.measurements[i8];
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
            return newState8;
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
