package ch.heigvd.ser.labo2;

import ch.heigvd.ser.labo2.coups.Case;
import ch.heigvd.ser.labo2.coups.CoupSpecial;
import ch.heigvd.ser.labo2.coups.Deplacement;
import ch.heigvd.ser.labo2.coups.TypePiece;
import org.jdom2.*; // Librairie à utiliser !
import org.jdom2.input.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

class Main {

    private static final String xmlFilePath = "tournois_fse.xml";

    public static void main(String ... args) {
        // TODO : A implémenter...
        Parser parser = new Parser(xmlFilePath);
        parser.parse();
    }
}
