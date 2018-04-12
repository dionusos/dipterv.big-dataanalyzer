import $ from 'jquery';
import * as model from "../model/Model";

export const UPDATE_DATASOURCES = 'metadata:updateDataSources';
export const RELOAD_DATASOURCES = 'metadata:updateDataSources';

export function updateDataSources(datasources) {
    return {
        type: UPDATE_DATASOURCES,
        payload: datasources
    }
}

export function reloadDataSources() {
    return dispatch => {
        $.ajax({
            url: model.backend + "/metadata/datasources",
            success(result) {
                dispatch(updateDataSources(result));
            },
            error() {

            }
        })
    }
}
