public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(5);
	m1.niceRandomize();
	m1.printMat();
	Mat m2= new Mat(5);
	m2=m1.inverse();
	m2.printMat();
	Mat m3=new Mat(m1,m2);
	m3.printMat();
    }
}