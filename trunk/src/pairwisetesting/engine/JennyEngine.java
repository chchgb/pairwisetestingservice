package pairwisetesting.engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import pairwisetesting.coredomain.Engine;
import pairwisetesting.coredomain.Factor;
import pairwisetesting.coredomain.MetaParameter;
import pairwisetesting.exception.EngineException;

public class JennyEngine extends Engine {

	public JennyEngine() {
		this.transformer = new JennyTestDataTransformer();
	}

	@Override
	protected String[][] generateRawTestData(MetaParameter mp)
			throws EngineException {
		
		StringBuilder command = new StringBuilder();
		command.append("jenny -n").append(mp.getStrength()).append(" ");
		
		for (Factor factor : mp.getFactors()) {
			command.append(factor.getNumOfLevels()).append(" ");
		}
		// System.out.println(command.toString());
		
		try {
			Process p = Runtime.getRuntime().exec(command.toString());
			
			ArrayList<String[]> testDataList = new ArrayList<String[]>();
			BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String strProc;
			
			while ((strProc = in.readLine()) != null) {
				String[] testDataRow = strProc.trim().split(" ");
				testDataList.add(testDataRow);
			}
			in.close();
			
			return testDataList.toArray(new String[0][0]);
			
		} catch (IOException e) {
			e.printStackTrace();
			throw new EngineException("jenny.exe error!");
		}
	}

}
