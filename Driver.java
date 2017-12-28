public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(4,6);
	m1.niceRandomize();
	m1.printMat();
	m1.rowReduce();
	m1.printMat();
    }
}