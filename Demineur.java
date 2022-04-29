import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Demineur extends JFrame implements ActionListener, MouseListener {
 
    // Déclaration des variables
    int nbrColonnes, nbrLignes, numMine;
    int nbLignes = 9, nbColonnes = 9, numMines = 10;
    GridLayout grille = new GridLayout(nbLignes, nbColonnes); // Création du layout de la grille
    boolean[] mines = new boolean[nbLignes * nbColonnes];   // Tableau de mines
    boolean[] clickable = new boolean[nbLignes * nbColonnes];   // Tableau de cliquables
    boolean perdu = false;  // Perdu ?
    boolean gagne = false;  // Gagné ?
    int[] cases = new int[nbLignes * nbColonnes];   // Tableau de cases
    JButton[] buttons = new JButton[nbLignes * nbColonnes];  // Tableau de boutons
    boolean[] estClique = new boolean[nbLignes * nbColonnes];   // Tableau de cliqués
    JMenuItem nouvellePartie = new JMenuItem("Nouveu Jeu"); // Nouvelle partie
    JMenuItem difficulte = new JMenuItem("OPTIONS");    // Difficulté
    JLabel renseignementDesMines = new JLabel("MINES: " + numMines + " MARQUÉ(S): 0");  // Renseignement des mines
    JPanel p = new JPanel();    // Panel principal
 
    // Constructeur
    public Demineur() {
        p.setLayout(grille);    
        commencer();
        // Création du menu
        for (int i = 0; i < (nbLignes * nbColonnes); i++) {
            p.add(buttons[i]);
        }
        // Création du menu
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("PROJET DEMINEUR EN JAVA DU GROUPE 2");
        nouvellePartie.addActionListener(this); // Action listener
        m.add(nouvellePartie);  // Ajout du menu
        difficulte.addActionListener(this); // Action listener
        m.add(difficulte);  // Ajout du menu
        mb.add(m);  
        this.setJMenuBar(mb);   
        this.add(p);
        this.add(renseignementDesMines, BorderLayout.SOUTH);    // Ajout du label
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // Fermeture de la fenêtre
        this.setVisible(true);  // Affichage de la fenêtre
    }
 
    // Méthode pour générer les mines
    public void genererMines() {
        int needed = numMines;  // Nombre de mines nécessaires
        while (needed > 0) {    // Tant que le nombre de mines nécessaires est supérieur à 0
            int x = (int) Math.floor(Math.random() * nbLignes); // Ligne aleatoire
            int y = (int) Math.floor(Math.random() * nbColonnes);   // Colonne aleatoire
            if (!mines[(nbLignes * y) + x]) {   // Si la case n'est pas déjà marquée
                mines[(nbLignes * y) + x] = true;   // Marque la case
                needed--;   // Diminue le nombre de mines nécessaires
            }
        }
    }
 
    // Méthode pour générer les cases
    public void genererCases() {
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                int cur = (nbLignes * y) + x;   // Numéro de la case
                if (mines[cur]) {   // Si c'est une mine
                    cases[cur] = 0; // 0 mines autour
                    continue;   // On passe à la case suivante
                }
                int temp = 0;   // Nombre de mines autour
                boolean l = (x - 1) >= 0;   // Est-ce qu'il y a une ligne ?
                boolean r = (x + 1) < nbLignes;  // Est-ce qu'il y a une ligne ?
                boolean u = (y - 1) >= 0;   // Est-ce qu'il y a une colonne ?
                boolean d = (y + 1) < nbColonnes;   // Est-ce qu'il y a une colonne ?
                int left = (nbLignes * (y)) + (x - 1);  // Numéro de la case à gauche
                int right = (nbLignes * (y)) + (x + 1); // Numéro de la case à droite
                int up = (nbLignes * (y - 1)) + (x);    // Numéro de la case en haut
                int upleft = (nbLignes * (y - 1)) + (x - 1);    // Numéro de la case en haut à gauche
                int upright = (nbLignes * (y - 1)) + (x + 1);   // Numéro de la case en haut à droite
                int down = (nbLignes * (y + 1)) + (x);  // Numéro de la case en bas
                int downleft = (nbLignes * (y + 1)) + (x - 1);  // Numéro de la case en bas à gauche
                int downright = (nbLignes * (y + 1)) + (x + 1); // Numéro de la case en bas à droite
                if (u) {    // Si il y a une ligne
                    if (mines[up]) {    // Si c'est une mine à côté
                        temp++; 
                    }
                    if (l) {    // Si il y a une colonne
                        if (mines[upleft]) {    // Si c'est une mine à côté à gauche
                            temp++;
                        }
                    }
                    if (r) {    // Si il y a une colonne à droite de la ligne
                        if (mines[upright]) {   // Si c'est une mine
                            temp++;
                        }
                    }
                }
                if (d) {    // Si il y a une ligne à droite de la ligne
                    if (mines[down]) {  // Si c'est une mine à la ligne
                        temp++;
                    }
                    if (l) {    // Si il y a une colonne  
                        if (mines[downleft]) {  // Si c'est une mine à gauche
                            temp++;
                        }
                    }
                    if (r) {    // Si il y a une colonne à droite de la ligne
                        if (mines[downright]) { // Si c'est une mine à droite de la ligne
                            temp++;
                        }
                    }
                }
                if (l) {
                    if (mines[left]) {
                        temp++;
                    }
                }
                if (r) {    // Si il y a une colonne à droite de la ligne
                    if (mines[right]) { // Si c'est une mine à droite de la ligne
                        temp++;
                    }
                }
                cases[cur] = temp;  // On met le nombre de mines autour de la case
            }
        }
    }
    //methode qui demande la difficulte
    public void difficulte() {
        String[] difficulte = {"Facile", "Moyen", "Difficile"};
        int choix = JOptionPane.showOptionDialog(null, "Choisissez votre difficulté", "Difficulté", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, difficulte, difficulte[0]);
        if (choix == 0) {
            nbrLignes = 5;
            nbrColonnes = 5;
            numMine = 3;
        } else if (choix == 1) {
            nbrLignes = 9;
            nbrColonnes = 9;
            numMine = 10;
        } else if (choix == 2) {
            nbrLignes = 15;
            nbrColonnes = 15;
            numMine = 16;
        }
    }
 
    // Méthode pour commencer une partie
    public void commencer() {
        //appeler la methode difficulte
        difficulte();
        //initialiser les lignes et colonnes et mines
        nbLignes = nbrLignes;
        nbColonnes = nbrColonnes;
        numMines = numMine;
        //initialiser le tableau de mines
        mines = new boolean[nbLignes * nbColonnes];
        //initialiser le tableau de cases
        cases = new int[nbLignes * nbColonnes];
        //appel de la methode commencerPartie avec les parametres nbLignes, nbColonnes, numMines et mines  et cases
        commencerPartie(nbrLignes, nbrColonnes, numMines, mines, cases);
    }

    void commencerPartie(int nbLignes, int nbColonnes, int numMines, boolean[] mines, int[] cases) {
        //initialiser le tableau de mines
        mines = new boolean[nbLignes * nbColonnes];
        //initialiser le tableau de cases
        cases = new int[nbLignes * nbColonnes];
        estClique = new boolean[nbLignes * nbColonnes];
        clickable = new boolean[nbLignes * nbColonnes];
        buttons = new JButton[nbLignes * nbColonnes];
        //initialiser le tableau de cases
        estClique = new boolean[nbLignes * nbColonnes];
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                mines[(nbLignes * y) + x] = false;
                estClique[(nbLignes * y) + x] = false;
                clickable[(nbLignes * y) + x] = true;
                buttons[(nbLignes * y) + x] = new JButton( /*"" + ( x * y )*/);
                buttons[(nbLignes * y) + x].setPreferredSize(new Dimension(
                        45, 45));
                buttons[(nbLignes * y) + x].addActionListener(this);
                buttons[(nbLignes * y) + x].addMouseListener(this);
            }
        }

        genererMines();
        genererCases();
    }
 
    // Méthode pour générer les cases autour de la case cliquée
    public void recommencer() {
        this.remove(p);
        p = new JPanel();
        grille = new GridLayout(nbLignes, nbColonnes);
        p.setLayout(grille);
        buttons = new JButton[nbLignes * nbColonnes];
        mines = new boolean[nbLignes * nbColonnes];
        estClique = new boolean[nbLignes * nbColonnes];
        clickable = new boolean[nbLignes * nbColonnes];
        cases = new int[nbLignes * nbColonnes];
        commencer();
        for (int i = 0; i < (nbLignes * nbColonnes); i++) {
            p.add(buttons[i]);
        }
        this.add(p);
        this.pack();
        genererMines();
        genererCases();
    }
 
    // Méthode pour gérer les actions de la souris sur les cases
    public void reprendre() {
        for (int x = 0; x < nbLignes; x++) {
            for (int y = 0; y < nbColonnes; y++) {
                mines[(nbLignes * y) + x] = false;
                estClique[(nbLignes * y) + x] = false;
                clickable[(nbLignes * y) + x] = true;
                buttons[(nbLignes * y) + x].setEnabled(true);
                buttons[(nbLignes * y) + x].setText("");
            }
        }
        genererMines();
        genererCases();
        perdu = false;
        renseignementDesMines.setText("MINES: " + numMines + " MARQUÉS: 0");    // On met le nombre de mines restantes
    }
 
    // Méthode pour gérer les actions de la souris sur les cases et les actions de la souris sur les boutons de menu
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == difficulte) {  // Si on clique sur le bouton de difficulté
            nbLignes = Integer.parseInt((String) JOptionPane.showInputDialog(   // On demande le nombre de lignes
                    this, "nbLignes", "nbLignes", JOptionPane.PLAIN_MESSAGE, null,  null, 10));
            nbColonnes = Integer.parseInt((String) JOptionPane.showInputDialog(
                    this, "Columns", "Columns", JOptionPane.PLAIN_MESSAGE,null, null, 10));
            numMines = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Mines", "Mines",
                    JOptionPane.PLAIN_MESSAGE, null, null, 10));
            recommencer();
        }
        if (!gagne) {   // Si on n'a pas gagné la partie
            for (int x = 0; x < nbLignes; x++) {    // On parcourt les lignes
                for (int y = 0; y < nbColonnes; y++) {  // On parcourt les colonnes
                    if (e.getSource() == buttons[(nbLignes * y) + x]    // Si on clique sur une case et que cette case n'est pas marquée
                            && !gagne && clickable[(nbLignes * y) + x]) { // et que la partie n'est pas gagnée et que la case est cliquable
                        verifierCheck(x, y);  // On appelle la méthode pour vérifier la case  cliquée
                        break;
                    }
                }
            }
        }
        if (e.getSource() == nouvellePartie) {  // Si on clique sur le bouton de nouvelle partie et que la partie n'est pas gagnée
            reprendre();    // On appelle la méthode pour réinitialiser la partie
            gagne = false;  // On met la partie comme non gagnée
            return;
 
        }
        checkWin(); // On vérifie si la partie est gagnée
    }
 
    // Méthode pour vérifier la case cliquée
    public void mouseEntered(MouseEvent e) {    
    }
 
    // Méthode pour vérifier la case cliquée
    public void mouseExited(MouseEvent e) {
    }
 
    // Méthode pour vérifier la case cliquée et mettre à jour les cases autour de la case cliquée
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 3) {   // Si on clique avec le bouton droit sur la case 
            int n = 0;
            for (int x = 0; x < nbLignes; x++) {
                for (int y = 0; y < nbColonnes; y++) {
                    if (e.getSource() == buttons[(nbLignes * y) + x]) { // Si on clique sur une case et que cette case n'est pas marquée
                        clickable[(nbLignes * y) + x] = !clickable[(nbLignes * y)  + x]; // On met la case comme non cliquable ou cliquable
                    }
                    if (!estClique[(nbLignes * y) + x]) {   // Si la case n'est pas marquée
                        if (!clickable[(nbLignes * y) + x]) {   // Si la case n'est pas cliquable
                            buttons[(nbLignes * y) + x].setText("?");   // On met le symbole ? sur la case  pour indiquer que la case n'est pas cliquable
                            n++;    // On incrémente le nombre de cases non cliquables
                        } else {
                            buttons[(nbLignes * y) + x].setText("");    // Sinon on met le symbole vide sur la case
                        }
                        renseignementDesMines.setText("mines: " + numMines + " marked: " + n);  // On met le nombre de mines restantes et le nombre de cases non cliquables
                    }
                }
            }
        }
    }
 
    public void mouseReleased(MouseEvent e) {
    }
 
    public void mouseClicked(MouseEvent e) {
    }
 
    // Méthode pour vérifier si la partie est gagnée ou perdue
    public void verifierCheck(int x, int y) {
        int cur = (nbLignes * y) + x;   // On met la case cliquée dans une variable
        boolean l = (x - 1) >= 0;   // On met la valeur de la case à gauche dans une variable
        boolean r = (x + 1) < nbLignes;  // On met la valeur de la case à droite dans une variable
        boolean u = (y - 1) >= 0;   // On met la valeur de la case en haut dans une variable
        boolean d = (y + 1) < nbColonnes;   // On met la valeur de la case en bas dans une variable
        int left = (nbLignes * (y)) + (x - 1);  // On met la case à gauche dans une variable
        int right = (nbLignes * (y)) + (x + 1); // On met la case à droite dans une variable
        int up = (nbLignes * (y - 1)) + (x);    // On met la case en haut dans une variable
        int upleft = (nbLignes * (y - 1)) + (x - 1);    // On met la case en haut à gauche dans une variable
        int upright = (nbLignes * (y - 1)) + (x + 1);   // On met la case en haut à droite dans une variable
        int down = (nbLignes * (y + 1)) + (x);  // On met la case en bas dans une variable
        int downleft = (nbLignes * (y + 1)) + (x - 1);  // On met la case en bas à gauche dans une variable
        int downright = (nbLignes * (y + 1)) + (x + 1); // On met la case en bas à droite dans une variable
        
        estClique[cur] = true;  // On met la case comme marquée dans une variable   
        buttons[cur].setEnabled(false);     // On désactive la case
        if (cases[cur] == 0 && !mines[cur] && !perdu && !gagne) {   // Si la case est vide et que la case n'est pas une mine et que la partie n'est pas perdue
            if (u && !gagne) {  // Si la case en haut existe et que la partie n'est pas perdue
                if (!estClique[up] && !mines[up]) {  // Si la case en haut n'est pas marquée et que la case en haut n'est pas une mine
                    estClique[up] = true;   // On met la case en haut comme marquée
                    buttons[up].doClick();  // On clique sur la case en haut
                }   
                if (l && !gagne) {  // Si la case à gauche existe et que la partie n'est pas perdue
                    if (!estClique[upleft] && cases[upleft] != 0 && !mines[upleft]) {   // Si la case à gauche n'est pas marquée et que la case à gauche n'est pas une mine
                        estClique[upleft] = true;   // On met la case à gauche comme marquée
                        buttons[upleft].doClick();  // On clique sur la case à gauche
                    }
                }
                if (r && !gagne) {  // Si la case à droite existe et que la partie n'est pas perdue
                    if (!estClique[upright] && cases[upright] != 0   && !mines[upright]) {  // Si la case à droite n'est pas marquée et que la case à droite n'est pas une mine
                        estClique[upright] = true;  // On met la case à droite comme marquée
                        buttons[upright].doClick(); // On clique sur la case à droite
                    }
                }
            }
            if (d && !gagne) {  // Si la case en bas existe et que la partie n'est pas perdue
                if (!estClique[down] && !mines[down]) {     
                    estClique[down] = true;
                    buttons[down].doClick();
                }
                if (l && !gagne) {  // Si la case à gauche existe et que la partie n'est pas perdue
                    if (!estClique[downleft] && cases[downleft] != 0 && !mines[downleft]) {
                        estClique[downleft] = true;
                        buttons[downleft].doClick();
                    }
                }
                if (r && !gagne) {  // Si la case à droite existe et que la partie n'est pas perdue
                    if (!estClique[downright] &&  cases[downright] != 0 && !mines[downright]) { // Si la case à droite n'est pas marquée et que la case à droite n'est pas une mine
                        estClique[downright] = true;
                        buttons[downright].doClick();
                    }
                }
            }
            if (l && !gagne) {  // Si la case à gauche existe et que la partie n'est pas perdue
                if (!estClique[left] && !mines[left]) { // Si la case à gauche n'est pas marquée et que la case à gauche n'est pas une mine
                    estClique[left] = true;
                    buttons[left].doClick();
                }
            }
            if (r && !gagne) {  // Si la case à droite existe et que la partie n'est pas perdue
                if (!estClique[right] && !mines[right]) {   // Si la case à droite n'est pas marquée et que la case à droite n'est pas une mine
                    estClique[right] = true;
                    buttons[right].doClick();
                }
            }
        } else {    // Si la case est une mine
            buttons[cur].setText("" + cases[cur]);  // On met le nombre de mines autour de la case
            if (!mines[cur] && cases[cur] == 0) {   // Si la case n'est pas une mine et que le nombre de mines autour de la case est 0
                buttons[cur].setText("");
            }
        }
        if (mines[cur] && !gagne) { // Si la case est une mine et que la partie n'est pas perdue
            buttons[cur].setText("B");  // On met un X sur la case pour indiquer que la case est une mine
            doLose();   // On perd la partie
        }
    }
 
    // Méthode pour perdre  la partie
    public void checkWin() {    // On vérifie si la partie est gagnée
        for (int x = 0; x < nbLignes; x++) {    // On parcours les lignes
            for (int y = 0; y < nbColonnes; y++) {  // On parcours les colonnes
                int cur = (nbLignes * y) + x;   // On met la case dans une variable
                if (!estClique[cur]) {  // Si la case n'est pas marquée
                    if (mines[cur]) {   // Si la case est une mine
                        continue;   // On continue
                    } else {    // Sinon
                        return;  // On retourne
                    }
                }
            }
        }
 
        doWin();    // On gagne
    }
 
    // Méthode pour gagner la partie
    public void doWin() {
        if (!perdu && !gagne) { // Si la partie n'est pas perdue et que la partie n'est pas gagnée
            gagne = true;   // On met la partie comme gagnée
            JOptionPane.showMessageDialog(null, "Voulez vous rejouer une nouvelle partie ?", "VOUS AVEZ GAGNÉ(E)",
                JOptionPane.INFORMATION_MESSAGE);   // On affiche un message de victoire
            nouvellePartie.doClick();   // On clique sur le bouton de nouvelle partie
        }
    }
 
    // Méthode pour perdre la partie
    public void doLose() {
        if (!perdu && !gagne) {
            perdu = true;
            for (int i = 0; i < nbLignes * nbColonnes; i++) {
                if (!estClique[i]) {    // Si la case n'est pas marquée
                    buttons[i].doClick(0);  // On clique sur la case
                }
            }
            JOptionPane.showMessageDialog(null,"Voulez vous rejouer une nouvelle partie ?", "VOUS AVEZ PERDU(E)",
                JOptionPane.ERROR_MESSAGE); // On affiche un message de défaite
            reprendre();    // On réinitialise la partie
        }
    }

    public static void main(String[] args) {
        new Demineur();
    }
}
