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

    //newtonsMethod does newton's method returning an object containing the guess, and how long it took to get there
    public static NewtonRet newtonsMethod(Mat matrix, Mat initialguess, double scale){
        Mat guess = new Mat(3,1);
	guess = initialguess;
	int guesses = 0;
        while(f(matrix,guess).size()>Math.pow(10,scale-3)){
	    if(guesses>50){
		NewtonRet ret = new NewtonRet(guess.getEntry(0,0), guess.getEntry(1,0), guess.getEntry(2,0), guesses);
		return ret;
	    }else{
		guess=newtonStep(matrix,guess);
		guesses++;
	    }
	}
	System.out.println(guesses);
	NewtonRet ret = new NewtonRet(guess.getEntry(0,0), guess.getEntry(1,0), guess.getEntry(2,0), guesses);
	return ret;
    }

    //newtonsMethod1 is the first test. It runs newton's method and returns a 500x500 matrix representing which initial guesses converged to which eigenvalues when the initial guess was changed in the range
    //matrix is the matrix to perform newton's method on
    //eigen1 and eigen2 are the two eigenvalues of matrix to check against
    //vars specifies what variables to vary in the initial guess to Newton's Method
    //   [1,1,0] means vary the two entries of the eigenvector
    //   [1,0,1] means vary the first entry of the eigenvector and the eigenvalue
    //   [0,1,1] means vary the second entry of the eigenvector and the eigenvalue
    //scale specifies what the scale is that the variations are taking place in. 0 means -1 to 1. 5 means -1*10^5 to 1*10^5 and etc. 
    public static NewtonRet[][] newtonsMethod1(Mat matrix, double eigen1, double eigen2, int[] vars, double scale){
	Mat initialguess = new Mat(3,1);
	NewtonRet[][] ret = new NewtonRet[500][500];
	if(vars[0]==1&&vars[1]==1){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(1,0,j/250.0*Math.pow(10,scale));
		    ret[i+250][j+250]=new NewtonRet(newtonsMethod(matrix, initialguess, scale));
		    /*System.out.print(i);
		    System.out.print(" ");
		    System.out.println(j);*/
		}
	    }
	
	}else if((vars[0]==1)&&(vars[2]==1)){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(2,0,j/250.0*Math.pow(10,scale));
		    ret[i+250][j+250]=new NewtonRet(newtonsMethod(matrix, initialguess, scale));
		    /*System.out.print(i);
		    System.out.print(" ");
		    System.out.println(j);*/
		}
	    }
	}else if((vars[1]==1)&&(vars[2]==1)){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(1,0,i/100.0);
		    initialguess.setEntry(2,0,j/100.0);
		    ret[i+250][j+250]=new NewtonRet(newtonsMethod(matrix, initialguess, scale));
		    /*System.out.print(i);
		    System.out.print(" ");
		    System.out.println(j);*/
		}
	    }
	}else{
	    System.out.println("BAD PARAMETERS FOR GUESSES");
	}
	return ret;
    }
    
    //This colors in points based on the value of kantorovich's theorem for given points

    public static double[][] kantorovichArray(Mat matrix, int[] vars, double scale){
	Mat initialguess = new Mat(3,1);
	double[][] ret = new double[500][500];
	if(vars[0]==1&&vars[1]==1){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(1,0,j/250.0*Math.pow(10,scale));
		    ret[i+250][j+250]= kantorovich(matrix, initialguess);
		}
	    }
	}else if((vars[0]==1)&&(vars[2]==1)){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(0,0,i/250.0*Math.pow(10,scale));
		    initialguess.setEntry(2,0,j/250.0*Math.pow(10,scale));
		    ret[i+250][j+250]= kantorovich(matrix, initialguess);
		}
	    }
	}else if((vars[1]==1)&&(vars[2]==1)){
	    for(int i = -250; i < 250; i++){
		for(int j = -250; j < 250; j++){
		    initialguess.setEntry(1,0,i/100.0);
		    initialguess.setEntry(2,0,j/100.0);
		    ret[i+250][j+250]= kantorovich(matrix, initialguess);
		}
	    }
	}else{
	    System.out.println("BAD PARAMETERS FOR GUESSES");
	}
	return ret;
    }

    //NewtonRetUnit does Newton's method for every point on a parameterized unit circle in the xy plane, and varies z along the z axis
    public static NewtonRet[][] NewtonMethodUnit(Mat matrix, double eigen1, double eigen2){
	Mat initialguess = new Mat(3,1);
	NewtonRet[][] ret = new NewtonRet[250][250];
	for(int i = 0; i < 500; i++){
	    for(int j = 0; j < 250; j++){
		initialguess.setEntry(0,0,Math.cos(i/125.0*Math.PI));
		initialguess.setEntry(1,0,Math.sin(i/125.0*Math.PI));
		initialguess.setEntry(2,0,j/25.0-5);
		ret[i][j] = newtonsMethod(matrix, initialguess, 1.0);
		if(kantorovich(matrix, initialguess)<0.5){
		    System.out.println("YOO" + i + j);
		}
		//System.out.println(ret[i][j].getNumSteps());
	    }
	}
	return ret;
    }

    public static double[][] KantorovichUnit(Mat matrix){
	Mat initialguess = new Mat(3,1);
	double[][] ret = new double[500][500];
	for(int i = 0; i < 500; i++){
	    for(int j = 0; j < 500; j++){
		initialguess.setEntry(0,0,Math.cos(i/250.0*Math.PI));
		initialguess.setEntry(1,0,Math.sin(i/250.0*Math.PI));
		initialguess.setEntry(2,0,j/50.0-5);
		ret[i][j]=kantorovich(matrix, initialguess);
	    }
	}
	return ret;
    }



    
    //colors in a ppm file for NewtonMethod1
    public static void writeToPPMNewtonMethod1(NewtonRet[][] data, String name, double eigen1, double eigen2, double scale){
	try{
	    File f = new File(name);
	    f.delete();
	    f.createNewFile();
	    FileWriter w = new FileWriter(f, true);
	    w.write("P3 "+data.length+" "+data[0].length+" 255\n");
	    for(int i = 0; i < data.length; i++){
		for(int j = 0; j < data[0].length; j++){
		    int color = 255 - 10*data[i][j].getNumSteps();
		    if(color<25){
			w.append("0 0 0");//corresponds to newton's method collapsing to zero eigenvector
		    }else if(Math.abs(data[i][j].getEigenvalue()-eigen1)/Math.abs(eigen1)<.1){
			w.append(Integer.toString(color)+" 0 0");//percent difference from first eigenvalue is small
		    }else if(Math.abs(data[i][j].getEigenvalue()-eigen2)/Math.abs(eigen2)<.1){
			w.append("0 0 "+Integer.toString(color));//percent difference from second eigenvalue is small
		    }else{
			w.append("0 "+Integer.toString(color)+" 0");//some thing else happened
		    }
		    w.append("\n");
		}
		System.out.println(i);
	    }
	    w.close();
	}catch(IOException e){
	    System.out.println(e);
	}
    }
    //colors in a ppm file for NewtonMethod1
    public static void writeToPPMKantorovichArray(double[][] data, String name){
	try{
	    File f = new File(name);
	    f.delete();
	    f.createNewFile();
	    FileWriter w = new FileWriter(f, true);
	    w.write("P3 "+data.length+" "+data[0].length+" 255\n");
	    for(int i = 0; i < data.length; i++){
		for(int j = 0; j < data[0].length; j++){
		    if(data[i][j]<=0.5){
			w.append("0 0 128");//corresponds to kantorovich's theorem being active and guaranteeing a root
		    }else if(data[i][j]<256){
			//a shade of red represents kantorovich being less than 256, darker the smaller
			w.append(Long.toString(Math.round(data[i][j]))+" 0 0");
		    }else{
			//green represents kantorovich's theorem being extremely large
			w.append("0 128 0");//some thing else happened
		    }
		    w.append("\n");
		}
		System.out.println(i);
	    }
	    w.close();
	}catch(IOException e){
	    System.out.println(e);
	}
    }
   
    //does the process of writing an image file and stuff like that
    public static void wholeNewtonMethod1(Mat matrix, double eigen1, double eigen2, int[] vars, double scale, String filename){
	writeToPPMNewtonMethod1(newtonsMethod1(matrix, eigen1, eigen2, vars, scale),filename,eigen1, eigen2, scale);
    }

    //does the process of writing an image file and stuff like that
    public static void wholeKantorovichArray(Mat matrix, int[] vars, double scale, String filename){
	writeToPPMKantorovichArray(kantorovichArray(matrix, vars, scale),filename);
    }

    //does the process of writing an image file and stuff like that
    public static void wholeKantorovichUnit(Mat matrix, String filename){
	writeToPPMKantorovichArray(KantorovichUnit(matrix),filename);
    }

    //tests every combination of variables, and goes from 10^-2 to 10^2
    public static void genImagesNewtonMethod1(Mat matrix, double eigen1, double eigen2, String filebase){
	int[] vars = {1,1,0};
	for(int i = -2; i < 3; i++){
	    wholeNewtonMethod1(matrix, eigen1, eigen2, vars, i, filebase+"_xy_zoom_"+i+".ppm");
	}
	vars[0]=0;
	vars[2]=1;
	for(int i = -2; i < 3; i++){
	    wholeNewtonMethod1(matrix, eigen1, eigen2, vars, i, filebase+"_yz_zoom_"+i+".ppm");
	}
	vars[0]=1;
	vars[1]=0;
	for(int i = -2; i < 3; i++){
	    wholeNewtonMethod1(matrix, eigen1, eigen2, vars, i, filebase+"_xz_zoom_"+i+".ppm");
	}
	String dirname = filebase;
	System.out.println(dirname);
	Fixerupper.stash(dirname);
    }

    //tests every combination of variables, and goes from 10^-2 to 10^2
    public static void genImagesKantorovichArray(Mat matrix, double eigen1, double eigen2, String filebase){
	int[] vars = {1,1,0};
	for(int i = -2; i < 3; i++){
	    wholeKantorovichArray(matrix, vars, i, filebase+"_xy_zoom_"+i+".ppm");
	}
	vars[0]=0;
	vars[2]=1;
	for(int i = -2; i < 3; i++){
	    wholeKantorovichArray(matrix, vars, i, filebase+"_yz_zoom_"+i+".ppm");
	}
	vars[0]=1;
	vars[1]=0;
	for(int i = -2; i < 3; i++){
	    wholeKantorovichArray(matrix, vars, i, filebase+"_xz_zoom_"+i+".ppm");
	}
	String dirname = filebase;
	System.out.println(dirname);
	Fixerupper.stash(dirname);
    }

    //this tests multiple changes of base by multiplying by a shuffling matrix on the right and on the left each iteration and stores them in seperate folders
    public static void genImagesNewtonMethod1Levels(double eigen1, double eigen2, Mat shuffler, int levels){
	String dirbase = "Eigens_"+Double.toString(eigen1)+"_"+Double.toString(eigen2)+
	    "Shuffler_"+shuffler.toLineString();
	//Fixerupper.mkdir(dirbase+"test", true);
	Mat base = new Mat(eigen1, 0, 0, eigen2);
	Mat inv = new Mat(2,2);
	inv = shuffler.inverse();
	for(int i = 0; i < levels; i++){
	    if(i>0){
		base.lMult(shuffler);
		base.rMult(inv);
	    }
	    genImagesNewtonMethod1(base, eigen1, eigen2, dirbase+"_level_"+Integer.toString(i));
	}
    }

    //this tests multiple changes of base by multiplying by a shuffling matrix on the right and on the left each iteration and stores them in seperate folders
    public static void genImagesKantorovichArrayLevels(double eigen1, double eigen2, Mat shuffler, int levels){
	String dirbase = "Kant_Eigens_"+Double.toString(eigen1)+"_"+Double.toString(eigen2)+
	    "Shuffler_"+shuffler.toLineString();
	//Fixerupper.mkdir(dirbase+"test", true);
	Mat base = new Mat(eigen1, 0, 0, eigen2);
	Mat inv = new Mat(2,2);
	inv = shuffler.inverse();
	for(int i = 0; i < levels; i++){
	    if(i>0){
		base.lMult(shuffler);
		base.rMult(inv);
	    }
	    genImagesKantorovichArray(base, eigen1, eigen2, dirbase+"_level_"+Integer.toString(i));
	}
    }


    //this tests multiple changes of base by multiplying by a shuffling matrix on the right and on the left each iteration and stores them in seperate folders
    public static void genImagesKantorovichUnitLevels(double eigen1, double eigen2, Mat shuffler, int levels){
	String dirbase = "CylKant_Eigens_"+Double.toString(eigen1)+"_"+Double.toString(eigen2)+
	    "Shuffler_"+shuffler.toLineString();
	//Fixerupper.mkdir(dirbase+"test", true);
	Mat base = new Mat(eigen1, 0, 0, eigen2);
	Mat inv = new Mat(2,2);
	inv = shuffler.inverse();
	for(int i = 0; i < levels; i++){
	    if(i>0){
		base.lMult(shuffler);
		base.rMult(inv);
	    }
	    wholeKantorovichUnit(base, dirbase+"_level_"+Integer.toString(i)+".ppm");
	}
	Fixerupper.stash(dirbase);
    }
}