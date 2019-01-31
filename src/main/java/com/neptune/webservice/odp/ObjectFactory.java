
package com.neptune.webservice.odp;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the _92._194._10._52.neptune.sync package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: _92._194._10._52.neptune.sync
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetODPBackUp }
     * 
     */
    public GetODPBackUp createGetODPBackUp() {
        return new GetODPBackUp();
    }

    /**
     * Create an instance of {@link SetODPBackUp }
     * 
     */
    public SetODPBackUp createSetODPBackUp() {
        return new SetODPBackUp();
    }

    /**
     * Create an instance of {@link ODP }
     * 
     */
    public ODP createODP() {
        return new ODP();
    }

    /**
     * Create an instance of {@link GetODPBackUpResponse }
     * 
     */
    public GetODPBackUpResponse createGetODPBackUpResponse() {
        return new GetODPBackUpResponse();
    }

    /**
     * Create an instance of {@link SetODPBackUpResponse }
     * 
     */
    public SetODPBackUpResponse createSetODPBackUpResponse() {
        return new SetODPBackUpResponse();
    }

}
