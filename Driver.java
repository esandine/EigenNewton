public class Driver{
    public static void main(String[] args){
	Mat m1 = new Mat(1,2,3,4);
	EigenNewton.genImagesNewtonMethod1Levels(2.0,1.0,m1,5);
	EigenNewton.genImagesKantorovichArrayLevels(2.0,1.0,m1,5);
    }
}