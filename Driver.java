public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(1,2);
	m1.niceRandomize();
	m1.printMat();
	Mat m2 = new Mat(2,1);
	m2.niceRandomize();
	m2.printMat();
	m1.lMult(m2);
	System.out.println("m2 times m1");
	m1.printMat();
    }
}