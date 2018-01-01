import java.lang.Math;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
public class EigenNewton{
    //These static functions serve to carry out the functions for the EigenNewton stuff described in the latex file.
    
    //f finds the value of the function we are attempting to evaluate. It takes as input the matrix which we are attempting to find the eigenvalue of, as well as a column matrix representing the current guess
    //matrix should be 2x2. Guess should be 3x1
    public static Mat f(Mat matrix, Mat guess){
	double a = matrix.getEntry(0,0);
	double b = matrix.getEntry(0,1);
	double c = matrix.getEntry(1,0);
	double d = matrix.getEntry(1,1);
	double v1 = guess.getEntry(0,0);
	double v2 = guess.getEntry(1,0);
	double lambda = guess.getEntry(2,0);
	Mat ret = new Mat(3,1);//the output vector
	ret.setEntry(0,0,(a-lambda)*v1+b*v2);
	ret.setEntry(1,0,c*v1+(d-lambda)*v2);
	ret.setEntry(2,0,Math.pow(v1,2)+Math.pow(v2,2)-1);
	return ret;
    }

    //Df finds the derivativeof the function evaluated for a certain matrix and guess
    public static Mat Df(Mat matrix, Mat guess){
	double a = matrix.getEntry(0,0);
	double b = matrix.getEntry(0,1);
	double c = matrix.getEntry(1,0);
	double d = matrix.getEntry(1,1);
	double v1 = guess.getEntry(0,0);
	double v2 = guess.getEntry(1,0);
	double lambda = guess.getEntry(2,0);
	Mat ret = new Mat(3);
	ret.setEntry(0,0,a-lambda);
	ret.setEntry(0,1,b);
	ret.setEntry(0,2,-v1);
	ret.setEntry(1,0,c);
	ret.setEntry(1,1,d-lambda);
	ret.setEntry(1,2,-v2);
	ret.setEntry(2,0,2*v1);
	ret.setEntry(2,1,2*v2);
	ret.setEntry(2,2,0);
	return ret;
    }    
    
    //kantorovich calculates the quantity used in kantorovich's theorem
    public static double kantorovich(Mat matrix, Mat guess){
	Mat fmatrix = new Mat();
	fmatrix = f(matrix,guess);
	Mat Dfmatrix = new Mat();
	Dfmatrix = Df(matrix,guess);
	return fmatrix.size()*Math.pow(Dfmatrix.inverse().size(),2)*Math.sqrt(12);
    }

    //newtonStep does one step of newton's method given the current guess
    public static Mat newtonStep(Mat matrix, Mat guess){
	Mat ret = new Mat();
	ret=Mat.add(guess,Mat.mult(Mat.mult(Df(matrix,guess).inverse(),f(matrix,guess)),-1));
	return ret;
    }

    //newtonsMethod does newton's method printing out guesses and kantorovich values along the way
    public static Mat newtonsMethod(Mat matrix, Mat initialguess){
        Mat guess = new Mat(3,1);
	guess = initialguess;
        while(f(matrix,guess).size()>.000000001){
	    if(Math.abs(guess.getEntry(0,0))+Math.abs(guess.getEntry(1,0))<.00001){
		return guess;//if we collapse to zero vector
	    }else{
		guess=newtonStep(matrix,guess);
	    }
        }
	return guess;
    }

    //newtonsMethod1 is the first test. It runs newton's method and returns a 500x500 matrix representing which initial guesses converged to which eigenvalues when the initial guess was changed in the range
    //matrix is the matrix to perform newton's method on
    //eigen1 and eigen2 are the two eigenvalues of matrix to check against
    //vars specifies what variables to vary in the initial guess to Newton's Method
    //   [1,1,0] means vary the two entries of the eigenvector
    //   [1,0,1] means vary the first entry of the eigenvector and the eigenvalue
    //   [0,1,1] means vary the second entry of the eigenvector and the eigenvalue
    //scale specifies what the scale is that the variations are taking place in. 0 means -1 to 1. 5 means -1*10^5 to 1*10^5 and etc. 
    public static int[][] newtonsMethod1(Mat matrix, double eigen1, double eigen2, int[] vars, double scale){
	Mat initialguess = new Mat(3,1);
	int[][] ret = new int[500][500];
	for(int i = -250; i < 250; i++){
	    for(int j = -250; j < 250; j++){
		if(vars[0]==1&&vars[1]==1){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(1,0,j/250.0*Math.pow(10,scale));
		}else if((vars[0]==1)&&(vars[2]==1)){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(2,0,j/250.0*Math.pow(10,scale));
		}else if((vars[1]==1)&&(vars[2]==1)){
		    initialguess.setEntry(1,0,i/100.0);
		    initialguess.setEntry(2,0,j/100.0);
		}else{
		    System.out.println("BAD PARAMETERS FOR GUESSES");
		}
		initialguess=newtonsMethod(matrix, initialguess);
		if((i==0)||(j==0)){
		    ret[i+250][j+250]=-1;//-1 corresponds to something else
		    //this draws the axis
		}
		else if(Math.abs(initialguess.getEntry(0,0))+Math.abs(initialguess.getEntry(1,0))<.00001){
		    ret[i+250][j+250]=0; //0 corresponds to the zero eigenvector
		}else if(Math.abs(initialguess.getEntry(2,0)-eigen1)<.001){
		    ret[i+250][j+250]=1; //1 corresponds to the first eigenvalue
		}else if(Math.abs(initialguess.getEntry(2,0)-eigen2)<.001){
		    ret[i+250][j+250]=2; //2 corresponds to the second eigenvalue
		}else{
		    initialguess.printMat();
		    ret[i+250][j+250]=-1;//-1 corresponds to something else
		}
	    }
	}
	return ret;
    }
    
    //image creation stuff
    public static String toPPM(int[][] data){
	String ret = "P3 "+data.length+" "+data[0].length+" 255\n";
	for(int i = 0; i < data.length; i++){
	    for(int j = 0; j < data[0].length; j++){
		if(data[i][j]==0){
		    ret+="0 0 0";
		}else if(data[i][j]==1){
		    ret+="255 0 0";
		}else if(data[i][j]==2){
		    ret+="0 0 255";
		}else{
		    ret+="0 255 0";
		}
		ret+="\n";
	    }
	    System.out.println(i);
	}
	return ret;
    }

    public static void write(String content, String name){
	try{
	    File f = new File(name);
	    f.delete();
	    f.createNewFile();
	    FileWriter w = new FileWriter(f, true);
	    w.write(content);
	    w.close();
	}catch(IOException e){
	    System.out.println(e);
	}
    }
    
    //does the process of writing an image file and stuff like that
    public static void wholeShabang(Mat matrix, double eigen1, double eigen2, int[] vars, double scale, String filename){
	write(toPPM(newtonsMethod1(matrix, eigen1, eigen2, vars, scale)),filename);
    }

    //tests every combination of variables, and goes from 10^-2 to 10^2
    public static void genImages(Mat matrix, double eigen1, double eigen2, String filebase){
	int[] vars = {1,1,0};
	for(int i = -2; i < 3; i++){
	    wholeShabang(matrix, eigen1, eigen2, vars, i, filebase+"_xy_zoom_"+i+".ppm");
	}
	vars[0]=0;
	vars[2]=1;
	for(int i = -2; i < 3; i++){
	    wholeShabang(matrix, eigen1, eigen2, vars, i, filebase+"_yz_zoom_"+i+".ppm");
	}
	vars[0]=1;
	vars[1]=0;
	for(int i = -2; i < 3; i++){
	    wholeShabang(matrix, eigen1, eigen2, vars, i, filebase+"_xz_zoom_"+i+".ppm");
	}
    }
}