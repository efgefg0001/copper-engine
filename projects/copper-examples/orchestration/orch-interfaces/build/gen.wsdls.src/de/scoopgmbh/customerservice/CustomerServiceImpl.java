
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package de.scoopgmbh.customerservice;

import java.util.logging.Logger;

/**
 * This class was generated by Apache CXF 2.6.0
 * 2013-04-08T12:41:16.298+02:00
 * Generated source version: 2.6.0
 * 
 */

@javax.jws.WebService(
                      serviceName = "CustomerServiceService",
                      portName = "CustomerServicePort",
                      targetNamespace = "http://customerservice.scoopgmbh.de/",
                      wsdlLocation = "classpath:wsdl/CustomerService.wsdl",
                      endpointInterface = "de.scoopgmbh.customerservice.CustomerService")
                      
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOG = Logger.getLogger(CustomerServiceImpl.class.getName());

    /* (non-Javadoc)
     * @see de.scoopgmbh.customerservice.CustomerService#getCustomersByMsisdn(de.scoopgmbh.customerservice.GetCustomersByMsisdnRequest  parameters )*
     */
    @Override
	public de.scoopgmbh.customerservice.GetCustomersByMsisdnResponse getCustomersByMsisdn(GetCustomersByMsisdnRequest parameters) { 
        LOG.info("Executing operation getCustomersByMsisdn");
        System.out.println(parameters);
        try {
            de.scoopgmbh.customerservice.GetCustomersByMsisdnResponse _return = null;
            return _return;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

}