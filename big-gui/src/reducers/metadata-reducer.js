import {
    UPDATE_DATASOURCES, UPDATE_DIMENSION_LIST, UPDATE_KPI_LIST, UPDATE_SELECTED_DATASOURCE, UPDATE_SELECTED_DIMENSIONS,
    UPDATE_SELECTED_KPIS
} from '../actions/metadata-actions'

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
        case(UPDATE_SELECTED_KPIS):
            let newState4 = {};
            Object.assign(newState4, state);
            newState4.selectedKpis = payload;
            return newState4;
        case(UPDATE_SELECTED_DIMENSIONS):
            let newState5 = {};
            Object.assign(newState5, state);
            newState5.selectedDimensions = payload;
            return newState5;
        case(UPDATE_SELECTED_DATASOURCE):
            let newState6 = {};
            Object.assign(newState6, state);
            newState6.activeDataSource = payload;
            return newState6;
        default:
            return state;
    }
}
