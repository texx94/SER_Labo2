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
        SAXBuilder saxBuilder = new SAXBuilder();
        File xmlFile = new File(xmlFilePath);

        try {
            Document document = (Document) saxBuilder.build(xmlFile);
            Element rootElement = document.getRootElement();

            Element tournois = rootElement.getChild("tournois");

            List tournoi = ((Element) tournois).getChildren("tournoi");
            for (Object t : tournoi) {
                Element parties = ((Element) t).getChild("parties");
                List listParties = ((Element) parties).getChildren("partie");
                for (Object p : listParties) {
                    Element coups = ((Element) p).getChild("coups");
                    List listCoup = coups.getChildren("coup");
                    for (Object c : listCoup) {
                        Element coup = ((Element) c);
                        String coupSpecial = coup.getAttributeValue("coup_special");
                        if (coupSpecial != null) {
//                            System.out.println(coupSpecial);
                        }
                        Element deplacement = coup.getChild("deplacement");
                        if (deplacement != null) {
                            System.out.println(deplacement);
                            // constr depla
                            String depart = deplacement.getAttributeValue("case_depart");
                            Case dep = null;
                            if (depart != null) {
                                int ligneD = Integer.parseInt(depart.substring(1));
                                char colonneD = depart.charAt(0);
                                dep = new Case(colonneD, ligneD);
                            }

                            String arrivee = deplacement.getAttributeValue("case_arrivee");
                            int ligneA = Integer.parseInt(arrivee.substring(1));
                            char colonneA = arrivee.charAt(0);
                            Case arr = new Case(colonneA, ligneA);

                            try {
                                // ACTUELLEMENT EXCEPTION LEVEE A LA CONSTRUCTION
                                Deplacement d = new Deplacement(TypePiece.valueOf(coup.getAttributeValue("piece")),
                                        TypePiece.valueOf(coup.getAttributeValue("elimination")),
                                        TypePiece.valueOf(coup.getAttributeValue("promotion")),
                                        CoupSpecial.valueOf(coupSpecial),
                                        dep,
                                        arr);
                                System.out.println(d.notationPGNimplem());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            // constr roque
                        }
                    }
                }
            }
        }

        catch (IOException io) {
            System.out.println(io.getMessage());
        }

        catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }

    }

}
