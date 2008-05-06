package testingngservices.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pairwisetesting.client.Dependence;



public class LibManager {
	private Set<String> partLibNameOfNotFound;
	private Set<String> fullPathOfFoundedLib;
	//private Set<String> serviceLibList;
	private ArrayList<String> neededLib;
	
	public LibManager(){
		this.partLibNameOfNotFound = new HashSet<String>();
		this.fullPathOfFoundedLib = new HashSet<String>();
		//this.serviceLibList =  new HashSet<String>();
		this.neededLib = new ArrayList<String>();
	}
	
	public void addNotFoundLibFromArrayList(ArrayList<String> libList){
		this.partLibNameOfNotFound.addAll(libList);
	}
	
	public void addPartLibNameOfNotFound(String libName){
		this.partLibNameOfNotFound.add(libName);
	}
	
	public void addFoundedLibFromArrayList(ArrayList<String> libList){
		this.fullPathOfFoundedLib.addAll(libList);
	}
	
	public void addFoundedLibFromClasspath(String classpathFullName){
		//System.out.println("depedence : " +Dependence.getAllLibList(classpathFullName));
		this.fullPathOfFoundedLib.addAll(Dependence.getAllLibList(classpathFullName));
	}
	
	public void addFoundedLibFromSingle(String libName){
		this.fullPathOfFoundedLib.add(libName);
	}
	/*
	public void setServiceLibList(Set<String> libSet){
		this.serviceLibList = libSet;
	}
	*/
	
	private void matchLib(){
		String libName = "";
		
		for(String notfoundLibName:this.partLibNameOfNotFound){
			boolean found = false;
			
			for(String fileFullName: this.fullPathOfFoundedLib){
				libName = fileFullName.substring(fileFullName.lastIndexOf("/")+1, fileFullName.length()-3);
				
				if(libName.toLowerCase().contains(notfoundLibName.toLowerCase())){
					File libFile = new File(fileFullName);
					if(libFile.exists()){
						found = true;
						this.neededLib.add(fileFullName);
					}
					
				}
				
				if(found){
					this.partLibNameOfNotFound.remove(notfoundLibName);
				}
			}
			
		}
		
	}
	
	public Set<String> getNotFoundLib(){
		matchLib();
		return this.partLibNameOfNotFound;
	}
	
	public boolean isFoundAllLib(){
		matchLib();
		return this.partLibNameOfNotFound.isEmpty();
	}
	
	public ArrayList<String> getNeededLib(){
		matchLib();
		if(this.partLibNameOfNotFound.isEmpty()){
			return this.neededLib;
		}else{
			System.out.println("These lib cannot found :\n"+this.partLibNameOfNotFound);
			return null;
		}
		
	}
	
}
