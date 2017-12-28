public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(4,6);
	m1.niceRandomize();
	m1.printMat();
	System.out.println("scale row 3 by 2");
	m1.scaleRow(2,2);
	m1.printMat();
	System.out.println("swap row 2 and 4");
	m1.swapRows(1,3);
	m1.printMat();
	m1.addRow(0,3,-1);
	System.out.println("subtract row 4 from row 1");
	m1.printMat();
    }
}