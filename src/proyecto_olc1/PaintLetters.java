/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_olc1;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

/**
 *
 * @author Javier Bran
 */
public class PaintLetters 
{
    public JTextPane caja2 = new JTextPane(); 
    public StyleContext sc = new StyleContext();
    public DefaultStyledDocument doc = new DefaultStyledDocument(sc);

    public void insertar(String texto){
   
        caja2.setDocument(doc);
        try {
            doc.insertString(0,texto,null);

        }catch (Exception ex) {
            System.out.println("ERROr: no se pudo establecer estilo de documento");
        }
   
   }

    public void pintaAzul(int posini,int posfin)
    {
        Style azul = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(azul, new Color(hex("2846a5")));
        doc.setCharacterAttributes(posini,posfin, azul, false);
    }  
        
    public void pintaGris(int posini,int posfin)
    {
       Style cafe = sc.addStyle("ConstantWidth", null);
       StyleConstants.setForeground(cafe, new Color(hex("8c8c8c")));
       doc.setCharacterAttributes(posini,posfin, cafe, false);
    }

    public void pintaMora(int posini,int posfin)
    {
       Style mora = sc.addStyle("ConstantWidth", null);
       StyleConstants.setForeground(mora, new Color(hex("a00082")));
       doc.setCharacterAttributes(posini,posfin,mora, false);
    }

    public void pintaNara(int posini,int posfin)
    {
       Style nara = sc.addStyle("ConstantWidth", null);
       StyleConstants.setForeground(nara, new Color(hex("ff6905")));
       doc.setCharacterAttributes(posini,posfin,nara, false);
    }
    
    private int hex(String color_hex)
    {
        return Integer.parseInt(color_hex, 16);
    }
}
