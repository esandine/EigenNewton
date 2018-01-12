import java.lang.Math;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class Tester{
    public static void main(String[] args){
	try{
            File f = new File("Testfile.txt");
            f.delete();
            f.createNewFile();
            FileWriter w = new FileWriter(f, true);
	    for(int i = 0; i < 10; i++){
		w.append("DO THIS WORK"+i);
	    }
	    w.close();
        }catch(IOException e){
            System.out.println(e);
        }
    }
}