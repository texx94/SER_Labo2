package ch.heigvd.ser.labo2;

import ch.heigvd.ser.labo2.coups.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Parser {
    SAXBuilder saxBuilder;
    File xmlFile;

    public Parser(String filePath) {
        saxBuilder = new SAXBuilder();
        xmlFile = new File(filePath);
    }

    public void parse() {
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
                        CoupSpecial cs = getCoupSpecial(coupSpecial);
                        if (coupSpecial != null) {
//                            System.out.println(coupSpecial);
                        }
                        Element deplacement = coup.getChild("deplacement");
                        Element roque = coup.getChild("roque");
                        if (deplacement != null) {
                            TypePiece piece = getTypePiece(deplacement.getAttributeValue("piece"));
                            TypePiece elimination = getTypePiece(deplacement.getAttributeValue("elimination"));
                            TypePiece promotion = getTypePiece(deplacement.getAttributeValue("promotion"));

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
                                Deplacement d = new Deplacement(piece, elimination, promotion, cs, dep, arr);
                                System.out.println(d.notationPGNimplem());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if (roque != null) {
                            TypeRoque typeRoque = getTypeRoque(roque.getAttributeValue("type"));
                            Roque r = new Roque(cs, typeRoque);
                            System.out.println(r.notationPGN());
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

    private TypePiece getTypePiece(String name) {
        if (name != null) {
            if (name.equals("Tour")) {
                return TypePiece.Tour;
            } else if (name.equals("Cavalier")) {
                return TypePiece.Cavalier;
            } else if (name.equals("Fou")) {
                return TypePiece.Fou;
            } else if (name.equals("Roi")) {
                return TypePiece.Roi;
            } else if (name.equals("Dame")) {
                return TypePiece.Dame;
            } else if (name.equals("Pion")) {
                return TypePiece.Pion;
            }
        }
        return null;
    }

    private CoupSpecial getCoupSpecial(String name) {
        if (name != null) {
            if (name.equals("echec")) {
                return CoupSpecial.ECHEC;
            } else if (name.equals("mat")) {
                return CoupSpecial.MAT;
            } else if (name.equals("nulle")) {
                return CoupSpecial.NULLE;
            }
        }
        return null;
    }

    private TypeRoque getTypeRoque(String type) {
        if (type != null) {
            if (type.equals("petit_roque")) {
                return TypeRoque.PETIT;
            } else if (type.equals("grand_roque")) {
                return TypeRoque.GRAND;
            }
        }
        return null;
    }
}
