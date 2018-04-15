import {BACKEND_ADDRESS} from "../config/config";
import $ from 'jquery';

export const FILL_DRILLDOWN = 'data:fillDrilldown';
export const ADD_FILTER = 'data:addFilter';
export const REMOVE_FILTER = 'data:removeFilter';
export const DELETE_DRILLDOWN = 'data:deleteDrilldown';
export const UPDATE_DRILLDOWN_LIMIT = 'data:updateDrilldownLimit';

export function fillDrilldown(data, id) {
    return {
        type: FILL_DRILLDOWN,
        payload: {data: data, drilldownId: id}
    }
}

export function deleteDrilldown(drilldownId) {
    return {
        type: DELETE_DRILLDOWN,
        payload: drilldownId
    }
}

export function loadData(dataQuery) {
    return dispatch => {
        $.ajax({
            url: BACKEND_ADDRESS + "/data",
            type: "POST",
            data: JSON.stringify({
                datasource: dataQuery.datasource,
                kpis: dataQuery.kpis,
                dimensions: dataQuery.dimensions,
                filters: dataQuery.filters,
                orders: dataQuery.orders,
                limit: dataQuery.limit
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

export function addFilter(measurement, dimension, value) {
    return {
        type: ADD_FILTER,
        payload: {measurement: measurement, dimension: dimension, value: value}
    }
}

export function removeFilter(measurement, dimension) {
    return {
        type: REMOVE_FILTER,
        payload: {measurement: measurement, dimension: dimension}
    }
}

export function changeDrilldownLimit(drilldownId, limit) {
    return {
        type: UPDATE_DRILLDOWN_LIMIT,
        payload: {drilldownId: drilldownId, limit: limit}
    }
}
