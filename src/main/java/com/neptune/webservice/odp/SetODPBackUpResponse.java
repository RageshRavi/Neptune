
package com.neptune.webservice.odp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SetODPBackUpResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "setODPBackUpResult"
})
@XmlRootElement(name = "SetODPBackUpResponse")
public class SetODPBackUpResponse {

    @XmlElement(name = "SetODPBackUpResult")
    protected String setODPBackUpResult;

    /**
     * Gets the value of the setODPBackUpResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSetODPBackUpResult() {
        return setODPBackUpResult;
    }

    /**
     * Sets the value of the setODPBackUpResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSetODPBackUpResult(String value) {
        this.setODPBackUpResult = value;
    }

}
