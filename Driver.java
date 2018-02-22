public class Driver{
    public static void main(String[] args){
	//Mat m1 = new Mat(1,2,3,4);
	//EigenNewton.genImagesNewtonMethod1Levels(2.0,1.0,m1,5);
	//EigenNewton.genImagesKantorovichArrayLevels(2.0,1.0,m1,5);


	Mat guess = new Mat(3,1);
	Mat m1 = new Mat(2,0,0,1);
	Mat basis = new Mat(1,2,3,4);
	NewtonRet[][] test = EigenNewton.NewtonMethodUnit(m1, 2.0, 1.0);
	/*double d1 = 0;
	double d2= 0;
	for(int i =-100; i < 100; i++){
	    m1 = new Mat(2,0,0,1);
	    guess.setEntry(0,0,0);
	    guess.setEntry(1,0,-i);
	    guess.setEntry(2,0,0);
	    //tests if Newton's method is independant of basis
	    
	    test = EigenNewton.newtonsMethod(m1, guess, 1);
	    d1=test.getEigenvalue();
	    
	    m1.lMult(basis);
	    m1.rMult(basis.inverse());
	    test = EigenNewton.newtonsMethod(m1, guess, 1);
	    d2=test.getEigenvalue();
	    if(d1-d2>.5){
		System.out.println("what up");
		System.out.println(i);
	    }
	    }*/
    }
}