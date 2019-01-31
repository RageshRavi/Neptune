
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
 *         &lt;element name="GetODPBackUpResult" type="{http://52.10.194.92/neptune/sync/}ODP" minOccurs="0"/>
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
    "getODPBackUpResult"
})
@XmlRootElement(name = "GetODPBackUpResponse")
public class GetODPBackUpResponse {

    @XmlElement(name = "GetODPBackUpResult")
    protected ODP getODPBackUpResult;

    /**
     * Gets the value of the getODPBackUpResult property.
     * 
     * @return
     *     possible object is
     *     {@link ODP }
     *     
     */
    public ODP getGetODPBackUpResult() {
        return getODPBackUpResult;
    }

    /**
     * Sets the value of the getODPBackUpResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ODP }
     *     
     */
    public void setGetODPBackUpResult(ODP value) {
        this.getODPBackUpResult = value;
    }

}
