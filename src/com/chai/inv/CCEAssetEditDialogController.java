package com.chai.inv;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.chai.inv.model.CCEAssetBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CommonService;
import com.chai.inv.service.CCEAssetService;
import com.chai.inv.service.FacilityService;
import com.chai.inv.util.CalendarUtil;
import com.chai.inv.util.SelectKeyComboBoxListener;
import java.time.LocalDate;

public class CCEAssetEditDialogController {
        
        
        @FXML
        private TextField x_CCE_ASSET_EXPECTED_LIFE;
        @FXML
        private TextField x_CCE_ASSET_MAKE;
        @FXML
        private TextField x_CCE_ASSET_DESIGNATION;
        @FXML
        private TextField x_CCE_ASSET_NEG_VOL;
        @FXML
        private TextField x_CCE_ASSET_PRICE;
        @FXML
        private TextField x_CCE_ASSET_CATEGORY;
        @FXML
        private TextField x_CCE_ASSET_REFRIGERANT;
        @FXML
        private TextField x_CCE_ASSET_MODEL;
        @FXML
        private TextField x_CCE_ASSET_TYPE;
        @FXML
        private TextField x_CCE_ASSET_POS_VOL;
        @FXML
        private TextField x_CCE_ASSET_ENERGY_SOURCE;

        
        @FXML
        private Button x_CANCEL_BTN;
        @FXML
        private Button x_OK_BTN;
        
	private boolean okClicked = false;
	private String actionBtnString;
	private boolean state_cce_record = false;
	private CCEAssetBean cceAssetBean;
	private RootLayoutController rootLayoutController;
	private Stage dialogStage;
	private CCEAssetService cceAssetService;
	private CCEAssetMainController cceAssetMain;
	private UserBean userBean;
	private LabelValueBean role;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public CCEAssetMainController getCCEAssetMain() {
		return cceAssetMain;
	}

	public void setCCEAssetMain(CCEAssetMainController cceAssetMain) {
		this.cceAssetMain = cceAssetMain;
	}

	public void setCCEAssetService(CCEAssetService cceAssetService,
			String actionBtnString) {
		this.cceAssetService = cceAssetService;
		this.actionBtnString = actionBtnString;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void disableOkButton() {
		x_OK_BTN.setDisable(true);
	}

	public void setCCEAssetBeanFields(CCEAssetBean cceAssetBean) {
		this.cceAssetBean = new CCEAssetBean();
		this.cceAssetBean = cceAssetBean;

		System.out.println("Warehouse ID: " + userBean.getX_USER_WAREHOUSE_ID());
                
		if (actionBtnString.equals("edit")) {
			System.out.println("action button string: " + actionBtnString);
			
                        
                        if (!(cceAssetBean.getX_CCE_ASSET_CATEGORY()== null)) {
                            x_CCE_ASSET_CATEGORY.setText(cceAssetBean.getX_CCE_ASSET_CATEGORY());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_COMPANY() == null)) {
                            x_CCE_ASSET_MAKE.setText(cceAssetBean.getX_CCE_ASSET_COMPANY());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_DESIGNATION()== null)) {
                                x_CCE_ASSET_DESIGNATION.setText(cceAssetBean.getX_CCE_ASSET_DESIGNATION());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_ENERGY()== null)) {
                            x_CCE_ASSET_ENERGY_SOURCE.setText(cceAssetBean.getX_CCE_ASSET_ENERGY());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_EXPECTED_LIFE() == null)) {
                            x_CCE_ASSET_EXPECTED_LIFE.setText(cceAssetBean.getX_CCE_ASSET_EXPECTED_LIFE());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_MODEL()== null)) {
                                x_CCE_ASSET_MODEL.setText(cceAssetBean.getX_CCE_ASSET_MODEL());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_PRICE()== null)) {
                            x_CCE_ASSET_PRICE.setText(cceAssetBean.getX_CCE_ASSET_PRICE());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_REFRIGERANT() == null)) {
                            x_CCE_ASSET_REFRIGERANT.setText(cceAssetBean.getX_CCE_ASSET_REFRIGERANT());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_TYPE()== null)) {
                                x_CCE_ASSET_TYPE.setText(cceAssetBean.getX_CCE_ASSET_TYPE());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_VOL_NEG()== null)) {
                            x_CCE_ASSET_NEG_VOL.setText(cceAssetBean.getX_CCE_ASSET_VOL_NEG());
                        }
                        if (!(cceAssetBean.getX_CCE_ASSET_VOL_POS() == null)) {
                            x_CCE_ASSET_POS_VOL.setText(cceAssetBean.getX_CCE_ASSET_VOL_POS());
                        }

                
		}
		
		
	}

	@FXML
	private void handleSubmitCCE() throws SQLException {
		if (isValidate(actionBtnString)) {
                        cceAssetBean.setX_CCE_ASSET_CREATED_BY(userBean.getX_USER_ID());
			cceAssetBean.setX_CCE_ASSET_UPDATED_BY(userBean.getX_USER_ID());

                        if (x_CCE_ASSET_CATEGORY != null
					&& !x_CCE_ASSET_CATEGORY.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_CATEGORY(x_CCE_ASSET_CATEGORY.getText());
                                System.out.println("CCE Asset Category: "+cceAssetBean.getX_CCE_ASSET_CATEGORY());
			}
                        if (x_CCE_ASSET_DESIGNATION != null
					&& !x_CCE_ASSET_DESIGNATION.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_DESIGNATION(x_CCE_ASSET_DESIGNATION.getText());
                                System.out.println("CCE Asset Designation: "+cceAssetBean.getX_CCE_ASSET_DESIGNATION());
			}
                        if (x_CCE_ASSET_ENERGY_SOURCE != null
					&& !x_CCE_ASSET_ENERGY_SOURCE.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_ENERGY(x_CCE_ASSET_ENERGY_SOURCE.getText());
                                System.out.println("CCE Asset Energy source: "+cceAssetBean.getX_CCE_ASSET_ENERGY());
			}
                        if (x_CCE_ASSET_EXPECTED_LIFE != null
					&& !x_CCE_ASSET_EXPECTED_LIFE.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_EXPECTED_LIFE(x_CCE_ASSET_EXPECTED_LIFE.getText());
                                System.out.println("CCE Asset Expected working life: "+cceAssetBean.getX_CCE_ASSET_EXPECTED_LIFE());
			}
                        if (x_CCE_ASSET_MAKE != null
					&& !x_CCE_ASSET_MAKE.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_COMPANY(x_CCE_ASSET_MAKE.getText());
                                System.out.println("CCE Asset Company: "+cceAssetBean.getX_CCE_ASSET_COMPANY());
			}
                        if (x_CCE_ASSET_MODEL != null
					&& !x_CCE_ASSET_MODEL.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_MODEL(x_CCE_ASSET_MODEL.getText());
                                System.out.println("CCE Asset Model: "+cceAssetBean.getX_CCE_ASSET_MODEL());
			}
                        if (x_CCE_ASSET_NEG_VOL != null
					&& !x_CCE_ASSET_NEG_VOL.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_VOL_NEG(x_CCE_ASSET_NEG_VOL.getText());
                                System.out.println("CCE Asset Neg vol: "+cceAssetBean.getX_CCE_ASSET_VOL_NEG());
			}
                        if (x_CCE_ASSET_POS_VOL != null
					&& !x_CCE_ASSET_POS_VOL.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_VOL_POS(x_CCE_ASSET_POS_VOL.getText());
                                System.out.println("CCE Asset Pos Vol: "+cceAssetBean.getX_CCE_ASSET_VOL_POS());
			}
                        if (x_CCE_ASSET_PRICE != null
					&& !x_CCE_ASSET_PRICE.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_PRICE(x_CCE_ASSET_PRICE.getText());
                                System.out.println("CCE Asset Price: "+cceAssetBean.getX_CCE_ASSET_PRICE());
			}
                        if (x_CCE_ASSET_REFRIGERANT != null
					&& !x_CCE_ASSET_REFRIGERANT.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_REFRIGERANT(x_CCE_ASSET_REFRIGERANT.getText());
                                System.out.println("CCE Asset Refrigerant: "+cceAssetBean.getX_CCE_ASSET_REFRIGERANT());
			}
                        if (x_CCE_ASSET_TYPE != null
					&& !x_CCE_ASSET_TYPE.getText().equals("")) {
				cceAssetBean.setX_CCE_ASSET_TYPE(x_CCE_ASSET_TYPE.getText());
                                System.out.println("CCE Asset Type: "+cceAssetBean.getX_CCE_ASSET_TYPE());
			}
                        
			if (cceAssetService == null)
				cceAssetService = new CCEAssetService();
			if (actionBtnString.equals("search")) {
				cceAssetMain.refreshCCEAssetTable(cceAssetService
						.getSearchList(cceAssetBean));
				okClicked = true;
				dialogStage.close();
				// DatabaseOperation.getDbo().closeConnection();
				// DatabaseOperation.setDbo(null);
			} else {
				String masthead;
				String message;
				if (actionBtnString.equals("add")) {
					masthead = "Successfully Added!";
					message = "CCE is Saved to the CCE List";
				} else {
					masthead = "Successfully Updated!";
					message = "CCE is Updated to the CCE List";
				}
				cceAssetService.saveCCE(cceAssetBean, actionBtnString);
				cceAssetMain.refreshCCEAssetTable();
				okClicked = true;
				org.controlsfx.dialog.Dialogs.create().owner(dialogStage)
						.title("Information").masthead(masthead)
						.message(message).showInformation();
				dialogStage.close();
				// DatabaseOperation.getDbo().closeConnection();
				// DatabaseOperation.setDbo(null);
			}
		}
	}

	public boolean isValidate(String actionBtnString) {
		if (!actionBtnString.equals("search")) {
			String errorMessage = "";
                            System.out.println("In cceAsset isValidate()");
			
                        if (x_CCE_ASSET_CATEGORY.getText().equals("")) {
				errorMessage += "No Category entered!\n";
			}
                        if (x_CCE_ASSET_DESIGNATION.getText().equals("")) {
				errorMessage += "No Designation entered!\n";
			}
                        if (x_CCE_ASSET_ENERGY_SOURCE.getText().equals("")) {
				errorMessage += "No Energy Source entered!\n";
			}
                        if (x_CCE_ASSET_EXPECTED_LIFE.getText().equals("")) {
				errorMessage += "No Expected working life entered!\n";
			}
                        if (x_CCE_ASSET_MAKE.getText().equals("")) {
				errorMessage += "No Company/Make entered!\n";
			}
                        if (x_CCE_ASSET_MODEL.getText().equals("")) {
				errorMessage += "No Model entered!\n";
			}
                        if (x_CCE_ASSET_NEG_VOL.getText().equals("")) {
				errorMessage += "No Neg Vol entered!\n";
			}
                        if (x_CCE_ASSET_POS_VOL.getText().equals("")) {
				errorMessage += "No Pos Vol entered!\n";
			}
                        if (x_CCE_ASSET_PRICE.getText().equals("")) {
				errorMessage += "No Price entered!\n";
			}
                        if (x_CCE_ASSET_REFRIGERANT.getText().equals("")) {
				errorMessage += "No Refrigerant entered!\n";
			}
                        if (x_CCE_ASSET_TYPE.getText().equals("")) {
				errorMessage += "No Type entered!\n";
			}
                        
                        if (errorMessage.length() == 0) {
				return true;
			} else {
				// Show the error message
				Dialogs.create().owner(dialogStage)
						.title("Invalid Fields Error")
						.masthead("Please correct invalid fields")
						.message(errorMessage).showError();
				return false;
			}
		} else
			return true;
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancelCCE() {
		dialogStage.close();
		// DatabaseOperation.getDbo().closeConnection();
		// DatabaseOperation.setDbo(null);
	}

}
