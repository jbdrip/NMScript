/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_olc1;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Javier Bran
 */
public class Proyecto_OLC1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        /*int i;
        int j;
        int k;
        int n;
        
        double[][] x = new double[80][80];
        n = 10;

        for (i = 1; i <= n; i++) {
            for (j = 1; j <= n; j++) {
                x[i][j] = 10.0*i;
            }
        }

        for (k = 1; k < n; k++) {
            for (i = k + 1; i <= n; i++) {
                for (j = 1; j <= n; j++) {
                    x[i][j] = x[i][j] - (x[k][j] * x[i][k]) / x[k][k];
                }

            }
        }

        System.out.println("la matriz escalonada es");
        for (i = 1; i <= n; i++) {
            System.out.println("\n");
            for (j = 1; j <= n; j++) {
                System.out.println(x[i][j]);
            }
        }
        
        for (k = n; k > 1; k--) {
            for (i = k - 1; i >= 1; i--) {
                for (j = n; j >= 1; j--) {
                    x[i][j] = x[i][j] - (x[k][j] * x[i][k]) / x[k][k];
                }
            }
        }

        System.out.println("la matriz escalonada es");
        for (i = 1; i <= n; i++) {
            System.out.println("\n");
            for (j = 1; j <= n; j++) {
                System.out.println(x[i][j]);
            }
        }*/
        
        Window window = new Window();
        window.setVisible(true);
        
        /*String dotPath = "\"C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
        String fileInputPath =  System.getProperty("user.dir") + "\\graphviz\\dots\\grafo1.dot";
        String fileOutputPath = "\"" + System.getProperty("user.dir") + "\\graphviz\\images\\grafo.png\"";
        fileOutputPath = "\"C:\\Users\\Javier Bran\\Documents\\NetBeansProjects\\Proyecto_OLC1\\graphviz\\images\\grafo1.png\"";
        fileInputPath = "\"C:\\Users\\Javier Bran\\Documents\\NetBeansProjects\\Proyecto_OLC1\\graphviz\\dots\\grafo2.dot\"";
        //fileInputPath = fileInputPath.replace("\\", "/");
        //fileOutputPath = fileOutputPath.replace("\\", "/");

        String tParam = "-Tjpg";
        String tOParam = "-o";
        
        ProcessBuilder pbuilder;
        //pbuilder = new ProcessBuilder( "dot", "-Tpng", fileInputPath, "-o", fileOutputPath);
        pbuilder = new ProcessBuilder( "dot", "-Tpng", "C:\\Users\\grafo1.dot", "-o", "C:\\Users\\grafo1.png");
        pbuilder.redirectErrorStream(true);
        try {
            //Ejecuta el proceso
            pbuilder.start();
        } catch (IOException ex) {
            System.out.println("error");
            Logger.getLogger(Proyecto_OLC1.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
}
