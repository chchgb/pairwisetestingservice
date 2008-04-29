package pairwisetesting.test.mock;

import java.io.File;
import java.util.ArrayList;

public class MockSourceFileList {
	ArrayList<String> fileList;
	
	public MockSourceFileList(){
		fileList = new ArrayList<String>();
	}
	
	public void ListOfFile(String fileName) {
		File myDir = new File(fileName); 
		// String[] FileNames = myDir.list();
		File[] file = myDir.listFiles();
		for(File filename:file){
			if(filename.isDirectory()){
				ListOfFile(filename.toString());
			}else{
				String temp = filename.toString();
				String extName = temp.substring(temp.lastIndexOf("."), temp.length());
				
				
				if(!extName.isEmpty() && extName.equals(".java")){
					this.fileList.add(filename.toString());
				}
			}
		}
	}
	
	
	public ArrayList<String> getFileList(String path){
		ListOfFile(path);
		
		return this.fileList;
	}

}
