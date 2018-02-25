public class UnitCylindar{
    //NewtonRetUnit does Newton's method for every point on a parameterized unit circle in the xy plane, and varies z along the z axis
    public static NewtonRet[][] newtonMethodUnit(Mat matrix, double eigen1, double eigen2){
        Mat initialguess = new Mat(3,1);
        NewtonRet[][] ret = new NewtonRet[500][500];
        for(int i = 0; i < 500; i++){
            for(int j = 0; j < 500; j++){
                initialguess.setEntry(0,0,Math.cos(i/250.0*Math.PI));
                initialguess.setEntry(1,0,Math.sin(i/250.0*Math.PI));
                initialguess.setEntry(2,0,j/50.0-5);
                ret[i][j] = EigenNewton.newtonsMethod(matrix, initialguess, 1.0);
            }
        }
        return ret;
    }

    public static double[][] KantorovichUnit(Mat matrix){
        Mat initialguess = new Mat(3,1);
        double[][] ret = new double[500][500];
        for(int i = 0; i < 500; i++){
            for(int j = 0; j < 500; j++){
                initialguess.setEntry(0,0,Math.cos(i/250.0*Math.PI));
                initialguess.setEntry(1,0,Math.sin(i/250.0*Math.PI));
                initialguess.setEntry(2,0,j/50.0-5);
                ret[j][i]=EigenNewton.kantorovich(matrix, initialguess);//i & j are flipped to make the orientation more practical
            }
        }
        return ret;
    }

    //does the process of writing an images file and computations for Newton's method on the unit cylindar
    public static void wholeNewtonMethodUnit(Mat matrix, double eigen1, double eigen2, String filename){
        EigenNewton.writeToPPMNewtonMethod1(newtonMethodUnit(matrix, eigen1, eigen2),filename, eigen1, eigen2, 1.0);
    }
    
    //does the process of writing an image file and computations for Kantorovich's theorem on the unit cylindar
    public static void wholeKantorovichUnit(Mat matrix, String filename){
        EigenNewton.writeToPPMKantorovichArray(KantorovichUnit(matrix),filename);
    }


    //this tests multiple changes of base by multiplying by a shuffling matrix on the right and on the left for kantorovich on the unit cylindar
    public static void genImagesKantorovichUnitLevels(double eigen1, double eigen2, Mat shuffler, int levels){
        String dirbase = "CylKant_Eigens_"+Double.toString(eigen1)+"_"+Double.toString(eigen2)+
            "Shuffler_"+shuffler.toLineString();
        //Fixerupper.mkdir(dirbase+"test", true);
        Mat base = new Mat(eigen1, 0, 0, eigen2);
        Mat inv = new Mat(2,2);
        inv = shuffler.inverse();
        for(int i = 0; i < levels; i++){
            if(i>0){
                base.lMult(shuffler);
                base.rMult(inv);
            }
            wholeKantorovichUnit(base, dirbase+"_level_"+Integer.toString(i)+".ppm");
        }
        Fixerupper.stash(dirbase);
    }


    //this tests multiple changes of base by multiplying by a shuffling matrix on the right and on the left for newtons method on the unit cylindar
    public static void genImagesNewtonMethodUnitLevels(double eigen1, double eigen2, Mat shuffler, int levels){
        String dirbase = "Cyl_Eigens_"+Double.toString(eigen1)+"_"+Double.toString(eigen2)+
            "Shuffler_"+shuffler.toLineString();
        //Fixerupper.mkdir(dirbase+"test", true);
        Mat base = new Mat(eigen1, 0, 0, eigen2);
        Mat inv = new Mat(2,2);
        inv = shuffler.inverse();
        for(int i = 0; i < levels; i++){
            if(i>0){
                base.lMult(shuffler);
                base.rMult(inv);
            }
            wholeNewtonMethodUnit(base, eigen1, eigen2, dirbase+"_level_"+Integer.toString(i)+".ppm");
        }
        Fixerupper.stash(dirbase);
    }
}