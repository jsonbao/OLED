import all.globalValues.GlobalValues;
import jep.Jep;
import jep.JepException;

/**
 * Created by nkatz on 3/7/2016.
 */

public class JepMemoryTest {

    public static void main(String[] args) throws JepException, java.io.IOException{

        Jep jep = new Jep();
        String task = GlobalValues.SCORE_RULES();
        java.io.File f = new java.io.File("/home/nkatz/Desktop/test");
        String solveMode = "all";
        while(true) {
            //jep.runScript(GlobalValues.ASPHandler());
            //jep.eval(String.format("run('%s', '%s', '%s')",f.getCanonicalPath(),solveMode,task));

            //jep.runScript("/home/nkatz/dev/ILED/src/main/java/jepleak.py");
            //jep.eval("run1()");

            jep.runScript("/home/nkatz/dev/ILED/src/main/java/aspleak.py");
            jep.eval(String.format("run('%s', '%s', '%s')",f.getCanonicalPath(),solveMode,task));
            //jep.eval("run()");
        }
        //jep.close();


        ///home/nkatz/dev/ILED/src/main/java
            /*
             * Ben Steffensmeier's example fro the JEP mailing list. This does not have a memory leak.
             *
            Jep jep = new Jep();
            jep.eval("from java.util import HashMap");
            for(int i = 0 ; i < 80 ; i += 1){
                for(int j = 0 ; j < 100000 ; j += 1){
                    jep.eval("a = HashMap()");
                    jep.eval("a['key'] = 'value'");
                    jep.getValue("a");
                }
                System.out.print(".");
            }
            jep.close();
            */

    }

}
