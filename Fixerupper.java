import java.io.IOException;
import java.lang.InterruptedException;
import java.io.File;
public class Fixerupper{
    public static String convertPPMtoJPG(String filename){
	String stem = filename.substring(0,filename.indexOf(".ppm"));
	String retstr = stem+".jpg";
	runCommand("convert "+filename+" "+stem+".jpg", true);
	return retstr;
    }
    
    //runCommand runs a command
    //wait specifies if the program should wait for it to finish
    public static void runCommand(String command, Boolean wait){
	try{
	    Runtime rt = Runtime.getRuntime();
	    Process pr = rt.exec(command);
	    if(wait){
		pr.waitFor();
	    }
	}catch(IOException e){
	    System.out.println(e);
	}catch(InterruptedException e){
	    System.out.println(e);
	}
    }
    
    public static void mkdir(String dir, Boolean wait){
	runCommand("mkdir "+dir, true);
    }
    //stash stores all of the ppm images in a directory given by the input
    public static void stash(String dirname){
	File thisdir = new File(".");
	File[] files = thisdir.listFiles();
	String name;
	System.out.println(dirname);
	runCommand("mkdir "+dirname, true);
	runCommand("echo "+dirname, true);
	for(int i = 0; i < files.length; i++){
	    if(files[i].getName().endsWith(".ppm")){
		name=convertPPMtoJPG(files[i].getName());
		runCommand("mv "+name+" "+dirname+"/", true);
		runCommand("rm "+name.substring(0,name.indexOf(".jpg"))+".ppm", true);
		runCommand("rm "+name, true);
	    }
	}
	System.out.println("Stashing images in directory complete");
    }
    public static void main(String[] args){
	stash("[345] ely");
	//convertPPMtoJPG("simpletest_xy_zoom_10.ppm");
    }
}