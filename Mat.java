import java.lang.Math;
public class Mat{
    //Variables
    private int rows;
    private int cols;
    private double[][] data;
    
    //Mutators
    public void setRows(int r){
	rows = r;
    }

    public void setCols(int c){
	cols = c;
    }
    public void resetData(){
	data = new double[rows][cols];
    }
    public void setEntry(int r,int c,double dat){
	data[r][c]=dat;
    }
    public void addEntry(int r, int c, double dat){
	setEntry(r, c, getEntry(r,c)+dat);
    }
    public void multEntry(int r, int c, double s){
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
    public double getEntry(int r,int c){
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

    public Mat(){
	this(0);
    }
    public Mat(Mat m){
	copyMat(m);
    }

    public Mat(Mat m1, Mat m2){
	this(mult(m1,m2));
    }

    public Mat(double a, double b, double c, double d){
	setRows(2);
	setCols(2);
	resetData();
	setEntry(0,0,a);
	setEntry(0,1,b);
	setEntry(1,0,c);
	setEntry(1,1,d);
    }

    //print function
    public String toString(){
	String printstr="";
	for(int i = 0; i<getRows(); i++){
	    for(int j = 0; j<getCols(); j++){
		printstr+=Double.toString(getEntry(i,j));
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
		setEntry(i,j,(double)Math.random());
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

    public void makeIdentity(){
	for(int i = 0; i < getRows(); i++){
	    setEntry(i,i,1);
	}
    }

    //multiply a vector by a scalar
    public static Mat mult(Mat A, double c){
	Mat ret = new Mat(A.getRows(), A.getCols());
	for(int i = 0; i < A.getRows(); i++){
	    for(int j = 0; j < A.getCols(); j++){
		ret.setEntry(i,j,c*A.getEntry(i,j));
	    }
	}
	return ret;
    }
    
    //multiply this vector by a scalar
    public void mult(double c){
	copyMat(mult(this,c));
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

    //multiply the given matrix my left and right respectively
    public void lMult(Mat A){
	copyMat(mult(A, this));
    }

    public void rMult(Mat A){
	copyMat(mult(this, A));
    }

    //ROW REDUCTION

    //row operations
    //scaleRow scales a row by a constant
    public void scaleRow(int r, double scale){
	Mat l = new Mat(getRows());
	l.makeIdentity();
	l.setEntry(r,r,scale);
	lMult(l);
    }

    //swapRows swaps two rows
    public void swapRows(int r1, int r2){
	Mat l = new Mat(getRows());
	l.makeIdentity();
	l.setEntry(r1,r1,0);
	l.setEntry(r2,r2,0);
	l.setEntry(r1,r2,1);
	l.setEntry(r2,r1,1);
	lMult(l);
    }

    //addRow adds a multiple of row r2 to row r1
    public void addRow(int r1, int r2, double scale){
	Mat l = new Mat(getRows());
	l.makeIdentity();
	l.setEntry(r1,r2,scale);
	lMult(l);
    }

    //rowReduce row reduces the matrix. It does this by starting at the upper left. If there is a pivotal one in the row, it flips it to the top.
    public void rowReduce(){
	int row = 0;
	for(int col = 0; col < getCols(); col++){
	    int pivotalrow=row;
	    while((pivotalrow< getRows())&&(Math.abs(getEntry(pivotalrow,col))<.0000000001)){
		pivotalrow+=1;
	    }
	    if(pivotalrow<getRows()){//if it finds a pivotal one
		swapRows(row,pivotalrow);
		scaleRow(row,1/getEntry(row,col));   
		for(pivotalrow=0; pivotalrow < getRows(); pivotalrow++){//subtract from the other pivotal ones
		    if((pivotalrow!=row)&&(Math.abs(getEntry(pivotalrow,col))>.0000000001)){
			addRow(pivotalrow, row, -getEntry(pivotalrow,col));
		    }
		}
		//you now have a pivotal one so you can move down a row
		row++;
	    }
	}
    }

    //inverse finds the inverse of square matrices
    public Mat inverse(){
	Mat big = new Mat(getRows(), 2*getCols());
	for(int j = 0; j < 2*getCols(); j++){
	    if(j<getCols()){
		for(int i = 0; i < getRows(); i++){
		    big.setEntry(i,j,getEntry(i,j));
		}
	    }else{
		big.setEntry(j-getCols(),j,1);
	    }
	}
	big.rowReduce();
	Mat ret = new Mat(getRows());
	for(int i = 0; i< getRows(); i++){
	    for(int j = 0; j < getCols(); j++){
		ret.setEntry(i,j,big.getEntry(i,j+getRows()));
	    }
	}
	return ret;
    }

    //size finds the size of a matrix
    public double size(){
	double sum = 0;
	for(int i = 0; i < getRows(); i++){
	    for(int j = 0; j < getCols(); j++){
		sum+=Math.pow(getEntry(i,j),2);
	    }
	}
	return Math.sqrt(sum);
    }

    //add adds two matrices together
    public static Mat add(Mat m1, Mat m2){
	Mat ret = new Mat(m1.getRows(), m2.getCols());
	for(int i = 0; i < m1.getRows(); i++){
	    for(int j = 0; j < m1.getCols(); j++){
		ret.setEntry(i,j,m1.getEntry(i,j)+m2.getEntry(i,j));
	    }
	}
	return ret;
    }
    public void add(Mat m2){
	copyMat(add(this, m2));
    }
}