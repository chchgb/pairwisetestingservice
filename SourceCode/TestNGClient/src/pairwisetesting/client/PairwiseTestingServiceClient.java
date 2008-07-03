
package pairwisetesting.client;

import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import javax.xml.namespace.QName;
import org.codehaus.xfire.XFireRuntimeException;
import org.codehaus.xfire.aegis.AegisBindingProvider;
import org.codehaus.xfire.annotations.AnnotationServiceFactory;
import org.codehaus.xfire.annotations.jsr181.Jsr181WebAnnotations;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.jaxb2.JaxbTypeRegistry;
import org.codehaus.xfire.service.Endpoint;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.soap.AbstractSoapBinding;
import org.codehaus.xfire.transport.TransportManager;

public class PairwiseTestingServiceClient {

    private static XFireProxyFactory proxyFactory = new XFireProxyFactory();
    private HashMap endpoints = new HashMap();
    private Service service0;

    public PairwiseTestingServiceClient() {
        create0();
        Endpoint PairwiseTestingServiceHttpPortEP = service0 .addEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpPort"), new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpBinding"), "http://192.168.201.126:8080/PariwiseTesting/services/PairwiseTestingService");
        endpoints.put(new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpPort"), PairwiseTestingServiceHttpPortEP);
        Endpoint PairwiseTestingServicePortTypeLocalEndpointEP = service0 .addEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalEndpoint"), new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalBinding"), "xfire.local://PairwiseTestingService");
        endpoints.put(new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalEndpoint"), PairwiseTestingServicePortTypeLocalEndpointEP);
    }
    
    public PairwiseTestingServiceClient(String url) {
        create0();
        Endpoint PairwiseTestingServiceHttpPortEP = service0 .addEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpPort"), new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpBinding"), url);
        endpoints.put(new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpPort"), PairwiseTestingServiceHttpPortEP);
        Endpoint PairwiseTestingServicePortTypeLocalEndpointEP = service0 .addEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalEndpoint"), new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalBinding"), "xfire.local://PairwiseTestingService");
        endpoints.put(new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalEndpoint"), PairwiseTestingServicePortTypeLocalEndpointEP);
    }

    public Object getEndpoint(Endpoint endpoint) {
        try {
            return proxyFactory.create((endpoint).getBinding(), (endpoint).getUrl());
        } catch (MalformedURLException e) {
            throw new XFireRuntimeException("Invalid URL", e);
        }
    }

    public Object getEndpoint(QName name) {
        Endpoint endpoint = ((Endpoint) endpoints.get((name)));
        if ((endpoint) == null) {
            throw new IllegalStateException("No such endpoint!");
        }
        return getEndpoint((endpoint));
    }

    public Collection getEndpoints() {
        return endpoints.values();
    }

    private void create0() {
        TransportManager tm = (org.codehaus.xfire.XFireFactory.newInstance().getXFire().getTransportManager());
        HashMap props = new HashMap();
        props.put("annotations.allow.interface", true);
        AnnotationServiceFactory asf = new AnnotationServiceFactory(new Jsr181WebAnnotations(), tm, new AegisBindingProvider(new JaxbTypeRegistry()));
        asf.setBindingCreationEnabled(false);
        service0 = asf.create((pairwisetesting.client.PairwiseTestingServicePortType.class), props);
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalBinding"), "urn:xfire:transport:local");
        }
        {
            AbstractSoapBinding soapBinding = asf.createSoap11Binding(service0, new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpBinding"), "http://schemas.xmlsoap.org/soap/http");
        }
    }

    public PairwiseTestingServicePortType getPairwiseTestingServiceHttpPort() {
        return ((PairwiseTestingServicePortType)(this).getEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServiceHttpPort")));
    }

    public PairwiseTestingServicePortType getPairwiseTestingServiceHttpPort(String url) {
        PairwiseTestingServicePortType var = getPairwiseTestingServiceHttpPort();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public PairwiseTestingServicePortType getPairwiseTestingServicePortTypeLocalEndpoint() {
        return ((PairwiseTestingServicePortType)(this).getEndpoint(new QName("http://service.pairwisetesting", "PairwiseTestingServicePortTypeLocalEndpoint")));
    }

    public PairwiseTestingServicePortType getPairwiseTestingServicePortTypeLocalEndpoint(String url) {
        PairwiseTestingServicePortType var = getPairwiseTestingServicePortTypeLocalEndpoint();
        org.codehaus.xfire.client.Client.getInstance(var).setUrl(url);
        return var;
    }

    public static void main(String[] args) {
        

        PairwiseTestingServiceClient client = new PairwiseTestingServiceClient();
        
		//create a default service endpoint
        PairwiseTestingServicePortType service = client.getPairwiseTestingServiceHttpPort();
        
		//TODO: Add custom client code here
        		//
        		//service.yourServiceOperationHere();
        
		System.out.println("test client completed");
        		System.exit(0);
    }

}
