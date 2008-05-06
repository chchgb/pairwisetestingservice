
package pairwisetesting.client;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

@WebService(name = "PairwiseTestingServicePortType", targetNamespace = "http://pairwisetesting")
@SOAPBinding(use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.WRAPPED)
public interface PairwiseTestingServicePortType {

	@WebMethod(operationName = "PariwiseTesting", action = "")
	@WebResult(name = "out", targetNamespace = "http://pairwisetesting")
	public String pariwiseTesting(
			@WebParam(name = "in0", targetNamespace = "http://pairwisetesting")
			String in0,
			@WebParam(name = "in1", targetNamespace = "http://pairwisetesting")
			String in1);

}
