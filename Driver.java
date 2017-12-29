public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(1,2,3,4);
	Mat m2 = new Mat(2);
	m2 = m1.inverse();
	Mat m3 = new Mat(2,0,0,1);
	m1.rMult(m3);
	m1.rMult(m2);
	System.out.println("This is the current matrix to find eigenvectors of");
	m1.printMat();
	Mat guess = new Mat(3,1);
	guess.setEntry(0,0,1);
	guess.setEntry(1,0,0);	
	guess.setEntry(2,0,1);
	System.out.println("This is the current guess");
	guess.printMat();
	System.out.println("This is the value of the function");
	System.out.println(EigenNewton.f(m1,guess));
	System.out.println("This is the value of the derivative");
	System.out.println(EigenNewton.Df(m1,guess));
	System.out.println("This is the value generated by Kantorovich's theorem");
	System.out.println(EigenNewton.kantorovich(m1,guess));
    }
}