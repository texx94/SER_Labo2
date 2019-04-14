/**
 * SER - LABO 02
 * Authors: Edoardo Carpita, Loris Gilliand, Nicodème Stalder
 * Date: 14-04-2019
 * File: Parser.java
 *
 */

package ch.heigvd.ser.labo2;

import ch.heigvd.ser.labo2.coups.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class Parser {

    // string contentant le format desiré du fichier de sortie
    private static final String FILEFORMAT =  ".pgn";

    /**
     * Methode principale pour parser un document passé en parametre
     * @param filePath parcorus du fichier à parser
     */
    public void parse(String filePath) {
        try {
            // création du lecteur de fichier et du fichier
            SAXBuilder saxBuilder = new SAXBuilder();
            File xmlFile = new File(filePath);

            // création du document et accès à sa racine
            Document document = saxBuilder.build(xmlFile);
            Element rootElement = document.getRootElement();

            // accéde à l'élement tournois
            Element tournois = rootElement.getChild("tournois");
            // crée une liste de tournoi
            List<Element> tournoi = tournois.getChildren("tournoi");

            // compteur de partie utilisé dans la création des fichiers de sorties
            int compteurPartie = 1;

            // pour chaque tournoi
            for (Element t : tournoi) {
                // accède a l'élement parties
                Element parties = t.getChild("parties");
                // crée une liste de partie
                List<Element> listParties = parties.getChildren("partie");

                // prépare l'écriture dans le fichier
                PrintWriter pw;

                // pour chaque partie
                for (Element p : listParties) {
                    // accède a l'élement coups
                    Element coups = p.getChild("coups");
                    // crée une liste de coup
                    List<Element> listCoup = coups.getChildren("coup");

                    // crée un nouvel objet d'ecriture dans le fichier crée a la volée
                    pw = new PrintWriter(new FileWriter("partie" + compteurPartie++ + FILEFORMAT));

                    // permet d'obtenir un comportement different pour la première ligne (pas de retour à la ligne AVANT)
					boolean premierCoup = true;

					// permet de garder en memoire la ligne sur laquelle nous écrivons dans le fichier
					int compteurLigne = 0;

					// pour chaque coup
                    for (int i = 0; i < listCoup.size(); ++i) {
                        // si c'est un coup des blanc
                        if (i % 2 == 0) {
                            // si ce n'est pas le premier coup de la partie
                            if (!premierCoup) {
                                // retourne a la ligne
                                pw.println();
                            }
                            premierCoup = false;

                            // écrit le numero de ligne
                            pw.print(compteurLigne++);
                        }

                        // accède au coup à traiter
                        Element coup = listCoup.get(i);

                        // regarde si c'est un coup spécial et obtient son equivalent dans l'enum CoupSpecial (retourne null si inexistant)
                        String coupSpecial = coup.getAttributeValue("coup_special");
                        CoupSpecial cs = getCoupSpecial(coupSpecial);

                        // accède a l'élement déplacement et roque (retourne null si inexistant)
                        Element deplacement = coup.getChild("deplacement");
                        Element roque = coup.getChild("roque");

                        // si la balise déplacement est présente
                        if (deplacement != null) {
                            // regarde la piece jouee, l'elimination realisee et la promotion realisee (retourne null si inexistant)
                            TypePiece piece = getTypePiece(deplacement.getAttributeValue("piece"));
                            TypePiece elimination = getTypePiece(deplacement.getAttributeValue("elimination"));
                            TypePiece promotion = getTypePiece(deplacement.getAttributeValue("promotion"));

                            // regarde la case de départ (retourne null si inexistant)
                            String depart = deplacement.getAttributeValue("case_depart");
                            Case dep = null;
                            // si la case de départ est indiquée
                            if (depart != null) {
                                // obtient la ligne et la colonne puis crée un objet Case
                                int ligneD = Integer.parseInt(depart.substring(1));
                                char colonneD = depart.charAt(0);
                                dep = new Case(colonneD, ligneD);
                            }

                            // regarde la case d'arrivée et crée un objet Case
                            String arrivee = deplacement.getAttributeValue("case_arrivee");
                            int ligneA = Integer.parseInt(arrivee.substring(1));
                            char colonneA = arrivee.charAt(0);
                            Case arr = new Case(colonneA, ligneA);

                            try {
                                // crée un objet déplacement à l'aide des informations traitées précédement
                                Deplacement d = new Deplacement(piece, elimination, promotion, cs, dep, arr);
                                // écrit un espace et sa notation PGN dans le fichier
                                pw.print(" " + d.notationPGNimplem());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if (roque != null) {
                            // si la balise roque est présente, crée un objet roque
                            TypeRoque typeRoque = getTypeRoque(roque.getAttributeValue("type"));
                            Roque r = new Roque(cs, typeRoque);
                            // écrit un espace et sa notation PGN dans le fichier
                            pw.print(" " + r.notationPGN());
                        }
                    }
                    // ferme le fichier
                    pw.flush();
                    pw.close();
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

    /**
     * Methode privée permetant d'obtenir l'objet ENUM en fonction de la valeur string passée en paramètre
     * @param name String represantant une pièce
     * @return l'objet TypePiece qui représente la piece dont le nom est passée en paramètre ou null si aucune correspondance
     */
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

    /**
     * Methode privée permetant d'obtenir l'objet ENUM en fonction de la valeur string passée en paramètre
     * @param name String represantant un coup spécial
     * @return l'objet CoupSpecial qui représente le coup dont le nom est passée en paramètre ou null si aucune correspondance
     */
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

    /**
     * Methode privée permetant d'obtenir l'objet ENUM en fonction de la valeur string passée en paramètre
     * @param type String represantant un roque
     * @return l'objet TypeRoque qui représente le roque dont le nom est passée en paramètre ou null si aucune correspondance
     */
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
