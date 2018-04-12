import {UPDATE_DATASOURCES} from '../actions/metadata-actions'

export default function metadataReducer(state={}, {type, payload}) {
    switch (type) {
        case (UPDATE_DATASOURCES):
            let newState = {};
            newState.datasources = payload;
            return newState;

        default:
            return state;
    }
}
