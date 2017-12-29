import java.lang.Math;
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
	System.out.println("started one Newton's method");
        while(f(matrix,guess).size()>.000000001){
	    if(Math.abs(guess.getEntry(0,0))+Math.abs(guess.getEntry(1,0))<.00001){
		return guess;//if we collapse to zero vector
	    }else{
		guess=newtonStep(matrix,guess);
	    }
        }
	return guess;
    }

    //newtonsMethod1 is the first test. It runs the guess from 0,0,0 to 1,0,1, iterating every tenth, and seeing what it comes up with in Newton's method
    public static void newtonsMethod1(Mat matrix, double eigen1, double eigen2){
	Mat initialguess = new Mat(3,1);
	String retstr = "";
	for(int i = -10; i < 10; i++){
	    for(int j = -10; j < 10; j++){
		initialguess.setEntry(0,0,i/10.0);
		initialguess.setEntry(1,0,0);
		initialguess.setEntry(2,0,j/10.0);
		initialguess=newtonsMethod(matrix, initialguess);
		if(Math.abs(initialguess.getEntry(0,0))+Math.abs(initialguess.getEntry(1,0))<.00001){
		    retstr+=" ";
		}else if(Math.abs(initialguess.getEntry(2,0)-eigen1)<.001){
		    retstr+="O";
		}else if(Math.abs(initialguess.getEntry(2,0)-eigen2)<.001){
		    retstr+="X";
		}else{
		    initialguess.printMat();
		    retstr+="X";
		}
		retstr+=" ";
	    }
	    retstr+="\n";
	}
	System.out.println(retstr);
    }
    
}