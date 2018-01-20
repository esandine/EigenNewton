import java.io.IOException;
import java.io.File;
public class Fixerupper{
    public static String convertPPMtoJPG(String filename){
	try{
	    String stem = filename.substring(0,filename.indexOf("."));
	    Runtime rt = Runtime.getRuntime();
	    String retstr = stem+".jpg";
	    Process pr = rt.exec("convert "+filename+" "+stem+".jpg");
	    return retstr;
	}catch(IOException e){
	    System.out.println(e);
	    return "oops";
	}
    }
    
    //stash stores all of the ppm images in a directory given by the input
    public static void stash(String dirname){
	try{
	    File thisdir = new File(".");
	    File[] files = thisdir.listFiles();
	    String name;
	    Runtime rt = Runtime.getRuntime();
	    Process pr = rt.exec("mkdir "+dirname);
	    for(int i = 0; i < files.length; i++){
		if(files[i].getName().endsWith(".ppm")){
		    name=convertPPMtoJPG(files[i].getName());
		    pr = rt.exec("mv "+name+" "+dirname+"/");
		}
	    }
	    System.out.println("Stashing images in directory complete");
	}catch(IOException e){
	    System.out.println(e);
	}
    }
    public static void main(String[] args){
	stash("teststash");
	//convertPPMtoJPG("simpletest_xy_zoom_10.ppm");
    }
}