package AoC2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Day07 extends Day {

	public Day07() {
		super();
		pathValues = new HashMap<>();
	}
	
	private HashMap<String, Long> pathValues;
	
	public void processText() {
		String commands=sb.toString();
		int fromIndex = 0;
		int posCD = 0;
		String currentFolder="";
		while ((posCD=sb.indexOf("$ cd ",fromIndex))>=0) {
			fromIndex=posCD+1;
			int nextPos=sb.indexOf("$ cd ",fromIndex);
			
			String part = nextPos>=0?sb.substring(posCD,nextPos):sb.substring(posCD);
//						System.out.println("new part: \n"+part);
			
			String folder = part.substring(5,part.indexOf("\n"));
			currentFolder+=folder;
			if(!currentFolder.endsWith("/"))currentFolder+="/";
			part = part.substring(part.indexOf("$ ls")+5);
			
//			log("processing folderName:"+folder+"\n");
//			log(part+"\n");
			
			if(folder.equals("..")) {
				currentFolder = currentFolder.substring(0,currentFolder.lastIndexOf("/")-3);
				currentFolder = currentFolder.substring(0,currentFolder.lastIndexOf("/")+1);
			} else {
				String[] files = part.split("\n");
				long dirSum=0;
				for (String string : files) {
					if(string.startsWith("dir")) continue;
//					System.out.println(string);
					dirSum+=Long.parseLong(string.split(" ")[0]);	
				}
				System.out.println("foldername:"+currentFolder+" size:"+dirSum);
				pathValues.put(currentFolder, dirSum);
			}
		}
		Set<String> paths = pathValues.keySet();
		HashMap<String,Long> modified = new HashMap<>();
		
		while(paths.size()>1) {
			String longestFolder = getLongest(paths);
			Long longestFolderSize = pathValues.get(longestFolder);
			modified.put(longestFolder, longestFolderSize);
			paths.remove(longestFolder);
			
			//longestbol leszedjuk a /xy/ reszt
			String parent = longestFolder.substring(0, longestFolder.lastIndexOf("/"));
			parent = longestFolder.substring(0, parent.lastIndexOf("/")+1);
//			System.out.println("parent:"+parent);
			long parentSize = pathValues.get(parent);
			pathValues.remove(parent);
			pathValues.put(parent,Long.valueOf(parentSize+longestFolderSize));
			
		}

		modified.put("/", pathValues.get("/"));
		
		System.out.println(".............");
		long solution=0;
		List<Long> ordered = new ArrayList<>(); 
		for (String key : modified.keySet()) {
			ordered.add(modified.get(key));
//			System.out.println(key+" "+modified.get(key));
			if(modified.get(key)<=100000){
				solution+=modified.get(key);
			}
		}
		
		long free = 70000000-pathValues.get("/");
		long needed= 30000000-free;
		Collections.sort(ordered);
		for (Long long1 : ordered) {
			System.out.println(long1+" ");
		}
		for (Long long1 : ordered) {
			if(long1>=needed) {
				System.out.println("Needed:"+needed);
				System.out.println("To be deleted: "+long1);
				break; // 32283214 too high
			}
				
		}
		
		System.out.println("Solution:"+solution);
	}
	
	public String getLongest(Set<String> paths) {
		String longest="";
		int depth=0;
		for (String path : paths) {
			if(path.split("/").length>depth) {
				longest=path;
				depth=path.split("/").length;
			}
		}
		return longest;
	}

	public static void main(String[] args) {
		Day07 d07 = new Day07();
		d07.readFile();
		d07.processText();
	}

}
