import $ from 'jquery';
import * as model from "../model/Model";

export const LOAD_DATA = 'data:loadData';
export const FILL_DRILLDOWN = 'data:fillDrilldown';

export function fillDrilldown(data, id) {
    return {
        type: FILL_DRILLDOWN,
        payload: {data: data, drilldownId: id}
    }
}

export function loadData(dataQuery) {
    return dispatch => {
        $.ajax({
            url: model.backend + "/data",
            type: "POST",
            data: JSON.stringify({
                datasource: dataQuery.datasource,
                kpis: dataQuery.kpis,
                dimensions: dataQuery.dimensions
            }),
            contentType:"application/json; charset=utf-8",
            success(result) {
                dispatch(fillDrilldown(result, dataQuery.id));
            },
            error() {

            }
        })
    }
}
