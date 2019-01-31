/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.neptune.common;

/**
 *
 * @author ragesh.raveendran
 */
public enum OdpEditEnum {

    REMOVE("remove"), BUILD("build"), REPLACE("replace"), EDIT("edit"), LIST("list"), OFFLINE("offline");

    private String odpEditEnum;

    private OdpEditEnum(String odpEditEnum) {
        this.odpEditEnum = odpEditEnum;
    }

    public String getOdpEditEnum() {
        return odpEditEnum;
    }
}
