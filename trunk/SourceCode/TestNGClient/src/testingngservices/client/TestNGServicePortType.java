
package testingngservices.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "TestNGServicePortType", targetNamespace = "http://testingngservices")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface TestNGServicePortType {

	@WebMethod(operationName = "testExecute", action = "")
	@WebResult(name = "out", targetNamespace = "http://testingngservices")
	public String testExecute(
			@WebParam(name = "in0", targetNamespace = "http://testingngservices")
			String in0);

	@WebMethod(operationName = "getTestCase", action = "")
	@WebResult(name = "out", targetNamespace = "http://testingngservices")
	public String getTestCase(
			@WebParam(name = "in0", targetNamespace = "http://testingngservices")
			String in0,
			@WebParam(name = "in1", targetNamespace = "http://testingngservices")
			String in1);

}
