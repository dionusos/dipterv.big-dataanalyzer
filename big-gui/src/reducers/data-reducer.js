import {CREATE_MEASUREMENT} from "../actions/metadata-actions";
import {FILL_DRILLDOWN} from "../actions/data-actions";

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
