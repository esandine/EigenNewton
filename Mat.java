import java.lang.Math;
public class Mat{
    //Variables
    private int rows;
    private int cols;
    private float[][] data;
    
    //Mutators
    public void setRows(int r){
	rows = r;
    }

    public void setCols(int c){
	cols = c;
    }
    public void resetData(){
	data = new float[rows][cols];
    }
    public void setEntry(int r,int c,float dat){
	data[r][c]=dat;
    }
    public void addEntry(int r, int c, float dat){
	setEntry(r, c, getEntry(r,c)+dat);
    }
    public void multEntry(int r, int c, float s){
	setEntry(r, c, getEntry(r,c)*c);
    }
    public void copyMat(Mat A){
	setRows(A.getRows());
	setCols(A.getCols());
	resetData();
	for(int i = 0; i < A.getRows(); i++){
	    for(int j =0; j < A.getCols(); j++){
		setEntry(i,j,A.getEntry(i,j));
	    }
	}
    }
    //Accessors
    public int getRows(){
	return rows;
    }
    
    public int getCols(){
	return cols;
    }
    public float getEntry(int r,int c){
	return data[r][c];
    }
    
    //Constructors
    public Mat(int rows, int cols){
	setRows(rows);
	setCols(cols);
	resetData();
    }
   
    public Mat(int rows){
	this(rows, rows);
    }

    public Mat(Mat m){
	copyMat(m);
    }

    public Mat(Mat m1, Mat m2){
	this(mult(m1,m2));
    }

    //print function
    public String toString(){
	String printstr="";
	for(int i = 0; i<getRows(); i++){
	    for(int j = 0; j<getCols(); j++){
		printstr+=Float.toString(getEntry(i,j));
		printstr+=" ";
	    }
	    printstr+="\n";
	}
	return printstr;
    }
    public void printMat(){
	System.out.println(this);
    }

    //randomize randomizes the matrix
    public void randomize(){
	for(int i = 0; i<rows; i++){
	    for(int j=0; j<cols; j++){
		setEntry(i,j,(float)Math.random());
	    }
	}
    }

    //randomize makes matrices that are easily computed by hand
    public void niceRandomize(){
	randomize();
	for(int i = 0; i<rows; i++){
	    for(int j=0; j<cols; j++){
		setEntry(i,j,Math.round(getEntry(i,j)*10));
	    }
	}
    }

    //mult multiplied two matrices together
    public static Mat mult(Mat A, Mat B){
	Mat ret = new Mat(A.getRows(), B.getCols());
	for(int i = 0; i < A.getRows(); i++){
	    for(int j = 0; j < B.getCols(); j++){
		for(int k = 0; k < A.getCols(); k++){
		    ret.addEntry(i,j,A.getEntry(i,k)*B.getEntry(k,j));
		}
	    }
	}
	return ret;
    }

    public void lMult(Mat A){
	copyMat(mult(A, this));
    }

    public void rMult(Mat A){
	copyMat(mult(this, A));
    }

}