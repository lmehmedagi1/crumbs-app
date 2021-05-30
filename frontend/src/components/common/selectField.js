import React, { useState } from "react";
import { Form } from "react-bootstrap";
import AsyncSelect from "react-select/async";
import axios from "axios"
import { env } from "configs/env"

export default function SelectField(props) {

    const [search, setSearch] = useState()

    const getOptions = (searchTerm) => {
        return axios(env.BASE_PATH + props.apiPath, {
            method: "POST",
            data: {
                type: props.type,
                searchTerm,
            },
            headers: { "Content-Type": "application/json" },
        });
    };

    const loadOptions = (inputValue, callback) => {
        getOptions(inputValue).then((response) => {
            const data = response.data.map(x => {
                return { value: x.id, label: x.name.trim() };
            });
            callback(data);
        });
    };

    return (
        <Form.Group
            as={props.as}
            className={props.class}
            style={props.style}
            onBlur={props.onBlur ? props.onBlur : null}
        >
            {props.label && <Form.Label>{props.label}</Form.Label>}
            {props.viewMode && !props.isMulti ? (
                <Form.Control
                    size="sm"
                    type="text"
                    name={props.name}
                    placeholder={props.placeholder}
                    value={props.value ? props.value.label : ""}
                    disabled={props.viewMode}
                />
            ) : (
                <AsyncSelect
                    size={props.size ? props.size : "sm"}
                    cacheOptions
                    isMulti={props.isMulti}
                    closeMenuOnSelect={props.closeMenuOnSelect}
                    className={props.isMulti ? "basic-multi-select" : "basic-single"}
                    classNamePrefix="select"
                    isSearchable={true}
                    value={props.value}
                    name={props.name}
                    defaultOptions
                    options={props.options}
                    onChange={props.onChange}
                    placeholder={props.placeholder ? props.placeholder : "Select..."}
                    onInputChange={props.search ? props.search : search}
                    search={props.search}
                    loadOptions={props.loadOptions ? props.loadOptions : loadOptions}
                    isDisabled={props.viewMode}
                    isOptionDisabled={props.isOptionDisabled}
                    ignore={props.ignore}
                    isValid={props.isValid}
                    isClearable={props.isClearable}
                />
            )}
        </Form.Group>
    );
}
