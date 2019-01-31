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
public enum EndtimeRuleEnum {
    
    EXACTLY("Exactly"),
    LATE("Late"),
    EARLY("Early");
    
    private String endTimerule;

    private EndtimeRuleEnum(String endTimerule) {
        this.endTimerule = endTimerule;
    }

    public String getEndTimerule() {
        return endTimerule;
    }
    
}
