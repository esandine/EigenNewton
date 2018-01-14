//the NewtonRet class is returned from the newtonsMethod function. It contains relevant information such as which value newton's method convereges to, and how many steps it took.                                                     
public class NewtonRet{
    private double x1;
    private double x2;
    private double eigenvalue;
    private int numSteps;

    public NewtonRet(double x1, double x2, double eigenvalue, int numSteps){
	setX1(x1);
	setX2(x2);
	setEigenvalue(eigenvalue);
	setNumSteps(numSteps);
    }
    public NewtonRet(NewtonRet r){
	setX1(r.getX1());
	setX2(r.getX2());
	setEigenvalue(r.getEigenvalue());
	setNumSteps(r.getNumSteps());
    }

    public void copyRet(NewtonRet r){
	setX1(r.getX1());
	setX2(r.getX2());
	setEigenvalue(r.getEigenvalue());
	setNumSteps(r.getNumSteps());
    }
    public double getX1(){
	return x1;
    }
    public double getX2(){
	return x2;
    }
    public double getEigenvalue(){
	return eigenvalue;
    }
    public int getNumSteps(){
	return numSteps;
    }

    public void setX1(double val){
	x1=val;
    }
    public void setX2(double val){
	x2=val;
    }
    public void setEigenvalue(double val){
	eigenvalue=val;
    }
    public void setNumSteps(int val){
	numSteps=val;
    }

}