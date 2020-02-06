package com.martin.view;

import com.martin.Stats;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class ResearchContr�le {

	@FXML private Label actuElectricit�, prixElectricit�,
			actuMaxAcheteur, prixMaxAcheteur,
			  prixSch�ma,
			    actuNiveau, prixNiveau;
	@FXML private AnchorPane �lectricit�,
				maxAcheteur,
				  sch�ma,
				    niveau;
	
	private static boolean �lectricit�Disable = false;
	private static boolean maxAcheteurDisable = false;
	private static boolean sch�maDisable = false;
	private static boolean niveauDisable = false;
	
	public ResearchContr�le() {}
	
	public void initialize() {
		�lectricit�.setDisable(�lectricit�Disable);
		maxAcheteur.setDisable(maxAcheteurDisable);
		sch�ma.setDisable(sch�maDisable);
		niveau.setDisable(niveauDisable);
		
		actuElectricit�.setText("Actuel : "+Stats.�lectricit�);
		actuMaxAcheteur.setText("Actuel : "+Stats.maxAcheteur);
		actuNiveau.setText("Actuel : niveau "+Stats.niveau);
		
		prixElectricit�.setText("Co�t : "+Stats.prixRechercheElectricit� + " �");
		prixMaxAcheteur.setText("Co�t : "+Stats.prixRechercheMaxAcheteur + " �");
		prixSch�ma.setText("Co�t : "+Stats.prixRechercheSch�ma + " �");
		prixNiveau.setText("Co�t : "+Stats.prixRechercheNiveau + " �");
	}
	
	@FXML private void �lectricit�() {
		System.out.println("�lectricit�");
		if(Stats.�lectricit� > 1 && JeuContr�le.argent.get() >= Stats.prixRechercheElectricit�) {
			Stats.�lectricit�--;
			actuElectricit�.setText("Actuel : "+Stats.�lectricit�+" �");
			JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.prixRechercheElectricit�);
			
			Stats.prixRechercheElectricit� *= 2;
			prixElectricit�.setText("Prix : "+Stats.prixRechercheElectricit�+" �");
		}
		
		if(Stats.�lectricit� == 1)
			�lectricit�Disable = true;
		
		�lectricit�.setDisable(�lectricit�Disable);
	}
	@FXML private void maxAcheteur() {
		System.out.println("maxAcheteur");
		if(Stats.maxAcheteur < 20 && JeuContr�le.argent.get() >= Stats.prixRechercheMaxAcheteur) {
			Stats.maxAcheteur++;
			actuMaxAcheteur.setText("Actuel : "+Stats.maxAcheteur);
			JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.prixRechercheMaxAcheteur);
			
			Stats.prixRechercheMaxAcheteur *= 2;
			prixMaxAcheteur.setText("Prix : "+Stats.prixRechercheMaxAcheteur+" �");
			
			Stats.prixRechercheSch�ma *= 2;
			prixSch�ma.setText("Prix : "+Stats.prixRechercheSch�ma+" �");
		}
		
		if(Stats.maxAcheteur == 20)
			maxAcheteurDisable = true;
		
		maxAcheteur.setDisable(maxAcheteurDisable);
	}
	@FXML private void sch�ma() {
		System.out.println("sch�ma");
		if(Stats.sch�ma < 20 && JeuContr�le.argent.get() >= Stats.prixRechercheSch�ma) {
			Stats.sch�ma++;
			JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.prixRechercheSch�ma);
			
			
			prixSch�ma.setText("Co�t : "+Stats.prixRechercheSch�ma+" �");
		}
		
		if(Stats.sch�ma == 20)
			sch�maDisable = true;
		
		sch�ma.setDisable(sch�maDisable);
	}
	@FXML private void niveau() {
		System.out.println("niveau");
		if(Stats.niveau < 3 && JeuContr�le.argent.get() >= Stats.prixRechercheNiveau) {
			Stats.niveau++;
			actuNiveau.setText("Actuel : niveau "+Stats.niveau);
			JeuContr�le.argent.set(JeuContr�le.argent.get()-Stats.prixRechercheNiveau);
			
			Stats.prixRechercheNiveau *= 3;
			prixNiveau.setText("Prix : "+Stats.prixRechercheNiveau+" �");
		}
		
		if(Stats.niveau == 3)
			niveauDisable = true;
		
		niveau.setDisable(niveauDisable);
	}
}
