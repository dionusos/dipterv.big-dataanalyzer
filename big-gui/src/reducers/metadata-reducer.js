import {UPDATE_DATASOURCES, UPDATE_DIMENSION_LIST, UPDATE_KPI_LIST} from '../actions/metadata-actions'

export default function metadataReducer(state={}, {type, payload}) {
    switch (type) {
        case (UPDATE_DATASOURCES):
            let newState = {};
            Object.assign(newState, state);
            if (payload && payload[0]) {
                newState.activeDataSource=payload[0];
            }
            newState.datasources = payload;
            return newState;
        case(UPDATE_KPI_LIST):
            let newState2 = {};
            Object.assign(newState2, state);
            newState2.kpis = payload;
            return newState2;
        case(UPDATE_DIMENSION_LIST):
            let newState3 = {};
            Object.assign(newState3, state);
            newState3.dimensions = payload;
            return newState3;
        default:
            return state;
    }
}
