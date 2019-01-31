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
public enum CampaignPlayOrder {
    
    RANDOM("random"),
    SEQUENTIAL("sequential");
    
    private String playOrder;

    private CampaignPlayOrder(String playOrder) {
        this.playOrder = playOrder;
    }

    public String getPlayOrder() {
        return playOrder;
    }
}
