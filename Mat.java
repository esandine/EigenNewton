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
    public void setData(int r,int c,double dat){
	data[r][c]=dat;
    }

    //Accessors
    public int getRows(){
	return rows;
    }
    
    public int getCols(){
	return cols;
    }
    public double getData(int r,int c){
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

    //print function
    public String toString(){
	String printstr="";
	for(int i = 0; i<getRows(); i++){
	    for(int j = 0; j<getCols(); j++){
		printstr+=Double.toString(getData(i,j));
		printstr+=" ";
	    }
	    printstr+="\n";
	}
	return printstr;
    }
    public void printMat(){
	System.out.println(this);
    }

    //randomMatrix
    public void randomize(){
	for(int i = 0; i<rows; i++){
	    for(int j=0; j<cols; j++){
		setData(i,j,Math.random());
	    }
	}
    }
}