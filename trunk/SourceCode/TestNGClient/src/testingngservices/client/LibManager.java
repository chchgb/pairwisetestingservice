package testingngservices.client;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import pairwisetesting.client.Dependence;

public class LibManager {
	private Set<String> partLibNameOfNotFound;
	private Set<String> fullPathOfFoundedLib;
	private Set<String> notFoundLib;
	private Set<String> foundedSet;

	public LibManager() {
		this.partLibNameOfNotFound = new HashSet<String>();
		this.fullPathOfFoundedLib = new HashSet<String>();
		// this.serviceLibList = new HashSet<String>();
		this.foundedSet = new HashSet<String>();
		this.notFoundLib = new HashSet<String>();
	}

	public void addNotFoundLibFromArrayList(ArrayList<String> libList) {
		this.partLibNameOfNotFound.addAll(libList);
	}

	public void addPartLibNameOfNotFound(String libName) {
		this.partLibNameOfNotFound.add(libName);
	}

	public void addFoundedLibFromArrayList(ArrayList<String> libList) {
		this.fullPathOfFoundedLib.addAll(libList);
	}

	public void addFoundedLibFromClasspath(String classpathFullName) {
		// System.out.println("depedence : "
		// +Dependence.getAllLibList(classpathFullName));
		this.fullPathOfFoundedLib.addAll(Dependence
				.getAllLibList(classpathFullName));
	}

	public void addFoundedLibFromSingle(String libName) {
		this.fullPathOfFoundedLib.add(libName);
	}

	/*
	 * public void setServiceLibList(Set<String> libSet){ this.serviceLibList =
	 * libSet; }
	 */

	private void matchLib() {
		String libName = "";

		for (String notfoundLibName : this.partLibNameOfNotFound) {
			boolean found = false;

			for (String fileFullName : this.fullPathOfFoundedLib) {
				libName = fileFullName.substring(
						fileFullName.lastIndexOf("/") + 1, fileFullName
								.length() - 3);

				if (libName.toLowerCase().contains(
						notfoundLibName.toLowerCase())) {
					File libFile = new File(fileFullName);
					if (libFile.exists()) {
						found = true;
						this.foundedSet.add(fileFullName);
					}

				}

				if (!found) {
					this.notFoundLib.add(notfoundLibName);
				}
			}

		}

	}

	public Set<String> getNeededFoundLib() {
		return this.partLibNameOfNotFound;
	}

	public Set<String> getNotFoundLib() {
		matchLib();
		return this.notFoundLib;
	}

	public boolean isFoundAllLib() {
		matchLib();
		return this.notFoundLib.isEmpty();
	}

	public void setFoundedSet(Set<String> libSet) {
		this.foundedSet = libSet;
	}

	public Set<String> getFoundedLibSet() {
		matchLib();
		return this.foundedSet;

	}

	public ArrayList<String> getAllLocalLib() {
		return new ArrayList<String>(this.fullPathOfFoundedLib);
	}

}
