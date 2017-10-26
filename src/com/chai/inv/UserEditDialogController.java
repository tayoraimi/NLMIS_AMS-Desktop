package com.chai.inv;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CommonService;
import com.chai.inv.service.CreateLogin;
import com.chai.inv.service.FacilityService;
import com.chai.inv.service.UserService;
import com.chai.inv.util.CalendarUtil;
import com.chai.inv.util.SelectKeyComboBoxListener;

public class UserEditDialogController {

	@FXML
	private TextField x_FIRST_NAME;
	@FXML
	private TextField x_LAST_NAME;
	@FXML
	private TextField x_LOGIN_NAME;
	@FXML
	private PasswordField x_PASSWORD;
	@FXML
	private PasswordField x_CONFIRM_PASSWORD;
	@FXML
	private CheckBox x_STATUS;
	@FXML
	private CheckBox x_ACTIVATED;
	@FXML
	private TextField x_TELEPHONE_NUMBER;
	@FXML
	private TextField x_EMAIL;
	@FXML
	private DatePicker x_START_DATE;
	@FXML
	private Label x_ACTIVATED_ON_LBL;
	@FXML
	private DatePicker x_ACTIVATED_ON;
	@FXML
	private DatePicker x_END_DATE;
	@FXML
	private ComboBox<LabelValueBean> x_USER_TYPE_NAME; // LOGIN_TYPE
	@FXML
	private ComboBox<LabelValueBean> x_USER_ROLE;
	@FXML
	private Label x_ASSIGN_LGA_LBL;
	@FXML
	private ComboBox<LabelValueBean> x_ASSIGN_LGA;

	private boolean okClicked = false;

	private UserBean userBean, loggedInUser;
	private RootLayoutController rootLayoutController;
	private UserMainController userMain;
	private String actionBtnString;
	private Stage dialogStage;
	private UserService userService;
	private LabelValueBean role;

	public UserMainController getUserMain() {
		return userMain;
	}

	public void setUserMain(UserMainController userMain) {
		this.userMain = userMain;
	}

	public UserService getUserService() {
		return userService;
	}
	
	public void setSelectedUserBean(UserBean selectedUserBean) {
		this.userBean = selectedUserBean;
	}

	public void setUserService(UserService userService, String actionBtnString, UserBean loggedInUser) {
		this.userService = userService;
		this.actionBtnString = actionBtnString;
		this.loggedInUser = loggedInUser;
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public RootLayoutController getRootLayoutController() {
		return rootLayoutController;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void initialize() {
		x_CONFIRM_PASSWORD.focusedProperty().addListener(
			new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					System.out.println("In confirm passwrd change Listener ");
					boolean confirm = x_CONFIRM_PASSWORD.getText().equals(x_PASSWORD.getText());
					System.out.println("Password: " + x_PASSWORD.getText()+ " ,Confirm Password: "+ x_CONFIRM_PASSWORD.getText());
					if (!newValue.booleanValue()) {
						System.out.println("change Listeners : outer if called..");
						if (!confirm) {
							System.out.println("change Listeners : dialog called..");
							Dialogs.create()
							.owner(dialogStage)
							.title("Warning")
							.masthead("Confirm Password does not match with Password")
							.message("Please re-enter")
							.showWarning();
						x_PASSWORD.clear();
						x_CONFIRM_PASSWORD.clear();
						x_PASSWORD.requestFocus();
					}
				}
			}
		});
	}

	public void setUserBeanFields(LabelValueBean labelValueBean) throws SQLException {
		//for display roleName in RoleDropDown
		String roleName="";
		x_FIRST_NAME.setText(userBean.getX_FIRST_NAME());
		x_LAST_NAME.setText(userBean.getX_LAST_NAME());
		x_LOGIN_NAME.setText(userBean.getX_LOGIN_NAME());
		if(actionBtnString.equals("edit")){
			x_LOGIN_NAME.setDisable(true);
		}else{
			x_LOGIN_NAME.setDisable(false);
		}
		x_USER_TYPE_NAME.setItems(userService.getDropdownList("TYPE"));
		new SelectKeyComboBoxListener(x_USER_TYPE_NAME);	
		if (userBean.getX_USER_ROLE_ID() != null) {
			//when edit btn pressed for each type of ROLES
			if(userBean.getX_USER_ROLE_NAME().equals("NTO")){
				roleName="NATIONAL";
			}else{
				roleName=userBean.getX_USER_ROLE_NAME();
			}
			x_USER_ROLE.setValue(new LabelValueBean(roleName, userBean.getX_USER_ROLE_ID()));
		}	
		if (actionBtnString.equals("edit")) {
			x_ASSIGN_LGA.setValue(new LabelValueBean(userBean.getX_ASSIGN_LGA(), userBean.getX_ASSIGN_LGA_ID()));
			x_ASSIGN_LGA.setDisable(true);
		}
		
		
		if (!actionBtnString.equals("add")) {
			x_PASSWORD.setDisable(true);
			x_CONFIRM_PASSWORD.setDisable(true);
		}
		if (!actionBtnString.equals("search")){
			if(actionBtnString.equals("add") ){
				if(role.getLabel().equals("NTO")){
					x_USER_TYPE_NAME.setValue(x_USER_TYPE_NAME.getItems().get(0));
				}				
			}else{
				x_USER_TYPE_NAME.setValue(labelValueBean);
			}			
		}
		if ((userBean != null) && (userBean.getX_STATUS() != null)) {
			if (userBean.getX_STATUS().equals("Active"))
				x_STATUS.setSelected(true);
			else
				x_STATUS.setSelected(false);
			if (userBean.getX_ACTIVATED().equals("Yes"))
				x_ACTIVATED.setSelected(true);
			else
				x_ACTIVATED.setSelected(false);
			x_EMAIL.setText(userBean.getX_EMAIL());
			x_TELEPHONE_NUMBER.setText(userBean.getX_TELEPHONE_NUMBER());
			if(userBean.getX_START_DATE()==null || userBean.getX_START_DATE().length()==0){
				x_START_DATE.setValue(LocalDate.now());
			}else{
				x_START_DATE.setValue(CalendarUtil.fromString(userBean.getX_START_DATE()));
			}
			x_ACTIVATED_ON.setValue(CalendarUtil.fromString(userBean.getX_ACTIVATED_ON()));
			x_END_DATE.setValue(CalendarUtil.fromString(userBean.getX_END_DATE()));
		} else {
			x_STATUS.setSelected(true);
			x_ACTIVATED.setSelected(false);
			x_STATUS.setDisable(true);
			if (!actionBtnString.equals("search")) {
				x_ACTIVATED.setDisable(true);
			}
			if (!actionBtnString.equals("search")) {
				x_START_DATE.setValue(LocalDate.now());
				x_ACTIVATED_ON.setValue(LocalDate.now());
			}
		}
	}

	@FXML
	public void handleUserTypeChange() {
		System.out.println("In UserEditDialogController.handleUserTypeChange() handler");
		try {
			if (x_USER_TYPE_NAME != null && x_USER_TYPE_NAME.getValue() != null) {
				if (MainApp.getUserRole().getLabel().toUpperCase().equals("SCCO")
						&& x_USER_TYPE_NAME.getValue().getLabel().toUpperCase().equals("ADMIN")) {
					// userService.getDropdownList("ROLE",<boolean
					// excludeNto>,<boolean excludeCCO>)
					x_USER_ROLE.setItems(userService.getDropdownList("ROLE"));
					new SelectKeyComboBoxListener(x_USER_ROLE);
				} else if (MainApp.getUserRole().getLabel().toUpperCase().equals("SCCO")
						&& x_USER_TYPE_NAME.getValue().getLabel().toUpperCase().equals("EMPLOYEE")) {					
					x_USER_ROLE.setItems(userService.getDropdownList("ROLE_CCO"));
					new SelectKeyComboBoxListener(x_USER_ROLE);
				}
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);			
			MainApp.LOGGER.severe("Exception: "+e.getMessage());
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			Dialogs.create()
			.owner(getDialogStage())
			.title("Error")
			.message(e.getMessage()).showException(e);
		}
	}
	
	@FXML
	public void handleOnUserRoleChange() {
		System.out.println(" In handleOnUserRoleChange() ... ");
		LocalDate date = LocalDate.now();
		if (x_ACTIVATED_ON.getValue() != null) {
			date = x_ACTIVATED_ON.getValue();
		}
		System.out.println("date = "+date);
		if(actionBtnString.equals("add")){
			x_STATUS.setSelected(true);
			switch (x_USER_ROLE.getValue().getLabel()) {
			case "CCO": // CCO - EMPLOYEE
				//activated must be false, so that new CCO's can register on application
				x_ACTIVATED.setSelected(false);
				x_ACTIVATED_ON.setValue(null);
				x_ASSIGN_LGA.setDisable(false);
				x_ASSIGN_LGA.setValue(null);
				x_ASSIGN_LGA_LBL.setText("Assign LGA");				
				x_ASSIGN_LGA.setItems(new FacilityService().getDropdownList("ASSIGN_LGA"));
				if (x_ASSIGN_LGA.getItems().size() == 0) {
					x_ASSIGN_LGA.setValue(new LabelValueBean("No LGA available to Assign", null));
					Dialogs.create().owner(dialogStage)
					.masthead("LGA not available to be assigned for CCO role user.")
					.showWarning();
				}
				new SelectKeyComboBoxListener(x_ASSIGN_LGA);
				break;
			case "LIO": // LIO				
				x_ACTIVATED.setSelected(true);
				x_ACTIVATED_ON.setValue(date);
				x_ASSIGN_LGA.setDisable(false);
				x_ASSIGN_LGA.setValue(null);
				x_ASSIGN_LGA_LBL.setText("Assign LGA");
				x_ASSIGN_LGA.setItems(new FacilityService().getDropdownList("ASSIGN_LGA_FOR_LIO_MOH"));
				new SelectKeyComboBoxListener(x_ASSIGN_LGA);
				break;
			case "MOH": // MOH
				x_ACTIVATED.setSelected(true);
				x_ACTIVATED_ON.setValue(date);
				x_ASSIGN_LGA_LBL.setText("Assign LGA");
				x_ASSIGN_LGA.setDisable(false);
				x_ASSIGN_LGA.setValue(null);
				x_ASSIGN_LGA.setItems(new FacilityService().getDropdownList("ASSIGN_LGA_FOR_LIO_MOH"));
				new SelectKeyComboBoxListener(x_ASSIGN_LGA);
				break;
			case "SIO": // SIO
				x_ACTIVATED.setSelected(true);
				x_ACTIVATED_ON.setValue(date);
				x_ASSIGN_LGA_LBL.setText("Assign State Cold Store");
				x_ASSIGN_LGA.setDisable(true);
				if(actionBtnString.equals("add")){
					x_ASSIGN_LGA.setValue(new LabelValueBean(loggedInUser.getX_USER_WAREHOUSE_NAME(),loggedInUser.getX_USER_WAREHOUSE_ID()));
				}
				break;
			case "SIFP": // SIFP
				x_ACTIVATED.setSelected(true);
				x_ACTIVATED_ON.setValue(date);
				x_ASSIGN_LGA_LBL.setText("Assign State Cold Store");
				x_ASSIGN_LGA.setDisable(true);
				if(actionBtnString.equals("add")){
					x_ASSIGN_LGA.setValue(new LabelValueBean(loggedInUser.getX_USER_WAREHOUSE_NAME(),loggedInUser.getX_USER_WAREHOUSE_ID()));					
				}
				break;
			case "SCCO": // SCCO	
				x_ACTIVATED.setSelected(true);
				x_ACTIVATED_ON.setValue(date);
				x_ASSIGN_LGA_LBL.setText("Assign State Cold Store");
				x_ASSIGN_LGA.setDisable(true);	
				if(actionBtnString.equals("add")){
					x_ASSIGN_LGA.setValue(new LabelValueBean(loggedInUser.getX_USER_WAREHOUSE_NAME(),loggedInUser.getX_USER_WAREHOUSE_ID()));				
				}				
				break;
			default: System.out.println("x_ASSIGN_LGA.getValue().getLabel() : "+x_ASSIGN_LGA.getValue().getLabel()); 
			
			}
		}
	}

	@FXML
	private void handleSubmitUser() {
		try {
			if (isValidate(actionBtnString)) {
				userBean.setX_ACTIVATED_BY(loggedInUser.getX_USER_ID());// activated-by
				userBean.setX_CREATED_BY(loggedInUser.getX_USER_ID());
				userBean.setX_UPDATED_BY(loggedInUser.getX_USER_ID());
				System.out.println("selected user's user_ID: "+ userBean.getX_USER_ID());
				System.out.println(userBean.getX_COMPANY_ID());
				userBean.setX_FIRST_NAME(x_FIRST_NAME.getText());
				userBean.setX_LAST_NAME(x_LAST_NAME.getText());
				userBean.setX_LOGIN_NAME(x_LOGIN_NAME.getText());
				userBean.setX_PASSWORD(x_PASSWORD.getText());
				userBean.setX_EMAIL(x_EMAIL.getText());
				userBean.setX_TELEPHONE_NUMBER(x_TELEPHONE_NUMBER.getText());
				if (x_USER_TYPE_NAME.getValue() != null
						&& !x_USER_TYPE_NAME.getValue().getLabel().equals("----(select none)----")) {
					userBean.setX_USER_TYPE_NAME(x_USER_TYPE_NAME.getValue().getLabel());
					userBean.setX_USER_TYPE_ID(x_USER_TYPE_NAME.getValue().getValue());
					userBean.setX_COMPANY_ID(x_USER_TYPE_NAME.getValue().getExtra());
				}
				if (x_USER_ROLE.getValue() != null && !x_USER_ROLE.getValue().getLabel().equals("----(select none)----")) {
					userBean.setX_USER_ROLE_ID(x_USER_ROLE.getValue().getValue());
					if(x_USER_ROLE.getValue().getLabel().equals("NATIONAL")){
						userBean.setX_USER_ROLE_NAME("NTO");
					}else{
						userBean.setX_USER_ROLE_NAME(x_USER_ROLE.getValue().getLabel());
					}
					userBean.setX_USER_ROLE_DETAILS(x_USER_ROLE.getValue().getExtra());
				}
				userBean.setX_STATUS(x_STATUS.isSelected() ? "A" : "I");
				userBean.setX_ACTIVATED(x_ACTIVATED.isSelected() ? "Y" : "N");
				if (x_ACTIVATED.isSelected() && x_ACTIVATED_ON.getValue() != null) {
					userBean.setX_ACTIVATED_ON(x_ACTIVATED_ON.getValue().toString());
				} else {
					userBean.setX_ACTIVATED_ON(null);
				}
				if (x_START_DATE.getValue() != null){
					userBean.setX_START_DATE(x_START_DATE.getValue().toString());
				}else {
					userBean.setX_START_DATE(null);
				}
				if (x_END_DATE.getValue() != null) {
					userBean.setX_END_DATE(x_END_DATE.getValue().toString());
				} else {
					userBean.setX_END_DATE(null);
				}
				if (x_USER_ROLE.getValue() != null
						&& !MainApp.getUserRole().getLabel().equals("CCO")) {
					System.out.println("LGA/NTO assigned : "+x_ASSIGN_LGA.getValue().getValue());
					userBean.setX_ASSIGN_LGA(x_ASSIGN_LGA.getValue().getLabel());
					userBean.setX_ASSIGN_LGA_ID(x_ASSIGN_LGA.getValue().getValue());
				}
				if (userService == null)
					userService = new UserService();
				if (actionBtnString.equals("search")) {
					userMain.refreshUserTable(userService.getSearchList(userBean));
					okClicked = true;
					dialogStage.close();
				} else {
					String masthead;
					String message;
					if (actionBtnString.equals("add")) {
						masthead = "Successfully Added!";
						message = "User is Saved to the Users List";
					} else {
						masthead = "Successfully Updated!";
						message = "User is Updated to the Users List";
					}
					boolean userRoleSaved = false;
					boolean userWarehouseAssignSaved = false;
					boolean userSaved = userService.saveUser(userBean,actionBtnString);
					if (userSaved) {
						System.out.println("userBean.getX_USER_ID()"+ userBean.getX_USER_ID());
						userRoleSaved = userService.setRoleIDMapping(userBean, actionBtnString);
						userWarehouseAssignSaved = userService.setWarehouseIdAssingment(userBean, actionBtnString);
					}
					userMain.refreshUserTableGrid();
					okClicked = true;
					if (userSaved && userRoleSaved && userWarehouseAssignSaved) {
						Dialogs.create().owner(getDialogStage())
								.title("Information").masthead(masthead)
								.message(message).showInformation();
						dialogStage.close();
					} else {
						Dialogs.create().owner(getDialogStage()).title("Error")
								.masthead("User record not saved").showError();
						dialogStage.close();
					}
				}
			}
		} catch (ClassNotFoundException | IOException | SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("Error occured while closing Connection:"+MyLogger.getStackTrace(e));
			Dialogs.create()
			.owner(getDialogStage())
			.title("Error")
			.message(e.getMessage()).showException(e);
		}
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public boolean isValidate(String actionBtnString) throws SQLException, ClassNotFoundException, IOException {
		boolean loginFlag = false;
		if (!actionBtnString.equals("search")) {
			String errorMessage = "";
			if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& actionBtnString.equals("add")) {
				if (x_ASSIGN_LGA == null || x_ASSIGN_LGA.getValue() == null
						|| x_ASSIGN_LGA.getValue().getLabel() == null
						|| x_ASSIGN_LGA.getValue().getLabel().length() == 0) {
					errorMessage += " Select an LGA to assign the user. \n ";
				}
			}
			if (x_FIRST_NAME.getText() == null
					|| x_FIRST_NAME.getText().length() == 0) {
				errorMessage += "First name cannot be left empty!\n";
			}
			if (x_LAST_NAME.getText() == null
					|| x_LAST_NAME.getText().length() == 0) {
				errorMessage += "Last name cannot be left empty\n";
			}
			if (x_LOGIN_NAME.getText() == null || x_LOGIN_NAME.getText().length() == 0) {
				errorMessage += "Login Name cannot be left empty\n";
			} else if (!x_LOGIN_NAME.getText().equals(userBean.getX_LOGIN_NAME())) {
				if (MainApp.isInternetAvailable(true)) {
					if (CreateLogin.checkIsUserNameExist(x_LOGIN_NAME.getText())) {
						errorMessage += "Please enter a different login-name.\n Other edited details will be saved.\n";
						loginFlag = true;
					}
				} else {
					errorMessage += "Internet is not available now, cannot add/update Login Name!";
				}
			}
			if (x_USER_TYPE_NAME.getValue() == null
					|| x_USER_TYPE_NAME.getValue() == null
					|| x_USER_TYPE_NAME.getValue().toString().length() == 0
					|| x_USER_TYPE_NAME.getValue().getLabel().equals("----(select none)----")) {
				errorMessage += "choose a login type!\n";
			}
			if (x_USER_ROLE.getValue() == null
					|| x_USER_ROLE.getValue().toString().length() == 0
					|| x_USER_ROLE.getValue().getLabel().equals("----(select none)----")) {
				errorMessage += "choose a user role!\n";
			}
			if (!actionBtnString.equals("edit")) {
				if (x_PASSWORD.getText() == null
						|| x_PASSWORD.getText().length() == 0) {
					errorMessage += "No valid password!\n";
				}
				if (x_CONFIRM_PASSWORD.getText() == null
						|| x_CONFIRM_PASSWORD.getText().length() == 0) {
					errorMessage += "No valid confirm password!\n";
				}
			}
			if (x_EMAIL.getText() != null && x_EMAIL.getText().length() != 0) {
				boolean valid = CommonService.validateEmailAddress(x_EMAIL.getText());
				errorMessage += (valid ? "" : "Enter a valid e-mail address\n");
			}
			// telephone number formats: (123)456-7890, 123-456-7890,
			// 1234567890, (123)-456-7890
			if (x_TELEPHONE_NUMBER.getText() != null && x_TELEPHONE_NUMBER.getText().length() != 0) {
				boolean valid = CommonService.isPhoneNumberValid(x_TELEPHONE_NUMBER.getText());
				errorMessage += (valid ? "" : "Enter phone number in the format specified in tooltip \n");
			}
			if (x_START_DATE.getValue() == null || x_START_DATE.getValue().toString().length() == 0) {
				errorMessage += "Select Start Date\n";
			}
			if (errorMessage.length() == 0) {
				return true;
			} else if (loginFlag) {
				Dialogs.create().owner(dialogStage)
						.title("Invalid Fields Error")
						.masthead("Login name Already Exist!")
						.message(errorMessage).showWarning();
				return true;
			} else {
				Dialogs.create().owner(dialogStage)
						.title("Invalid Fields Error")
						.masthead("Please correct invalid fields")
						.message(errorMessage).showError();
				return false;
			}
		} else
			return true;
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO
			x_USER_TYPE_NAME.setDisable(true);
			x_USER_ROLE.setDisable(true);
			x_ACTIVATED_ON.setDisable(true);
			x_ACTIVATED.setDisable(true);
			x_STATUS.setDisable(true);
			x_START_DATE.setDisable(true);
			x_END_DATE.setDisable(true);
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(true);
			x_ACTIVATED_ON.setVisible(true);
			break;
		case "MOH": // MOH
			x_USER_TYPE_NAME.setDisable(true);
			x_USER_ROLE.setDisable(true);
			x_ACTIVATED_ON.setDisable(true);
			x_ACTIVATED.setDisable(true);
			x_STATUS.setDisable(true);
			x_START_DATE.setDisable(true);
			x_END_DATE.setDisable(true);
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(true);
			x_ACTIVATED_ON.setVisible(true);
			break;
		case "SIO": // SIO
			x_USER_TYPE_NAME.setDisable(true);
			x_USER_ROLE.setDisable(true);
			x_ACTIVATED_ON.setDisable(true);
			x_ACTIVATED.setDisable(true);
			x_STATUS.setDisable(true);
			x_START_DATE.setDisable(true);
			x_END_DATE.setDisable(true);
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(true);
			x_ACTIVATED_ON.setVisible(true);
			break;
		case "SIFP": // SIFP
			x_USER_TYPE_NAME.setDisable(true);
			x_USER_ROLE.setDisable(true);
			x_ACTIVATED_ON.setDisable(true);
			x_ACTIVATED.setDisable(true);
			x_STATUS.setDisable(true);
			x_START_DATE.setDisable(true);
			x_END_DATE.setDisable(true);
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(true);
			x_ACTIVATED_ON.setVisible(true);
			break;
		case "SCCO": // SCCO
			if(actionBtnString.equals("add")){
				x_USER_TYPE_NAME.setDisable(false);
				x_USER_ROLE.setDisable(false);
			}else{
				x_USER_TYPE_NAME.setDisable(true);
				x_USER_ROLE.setDisable(true);
				x_ACTIVATED.setDisable(true);
				x_STATUS.setDisable(true);
			}		
//			x_USER_ROLE.setItems(userService.getDropdownList("ROLE"));
//			new SelectKeyComboBoxListener(x_USER_ROLE);			
			x_ASSIGN_LGA_LBL.setVisible(true);
			x_ASSIGN_LGA.setVisible(true);			
			x_ACTIVATED_ON_LBL.setVisible(false);
			x_ACTIVATED_ON.setVisible(false);		
			break;
		case "NTO": // NTO
			System.out.println("In setRole() case : NTO : "+actionBtnString);
			x_USER_ROLE.setDisable(true);
			x_USER_TYPE_NAME.setDisable(true);			
			x_USER_ROLE.setValue(new LabelValueBean("NATIONAL",role.getValue(),"National Technical Officer - SUPER ADMINISTRATOR"));
//			new SelectKeyComboBoxListener(x_USER_ROLE);
//			x_USER_ROLE.getSelectionModel().selectFirst();			
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(false);
			x_ACTIVATED_ON.setVisible(false);
			x_ACTIVATED.setSelected(true);
			if(actionBtnString.equals("search")){
				x_USER_TYPE_NAME.setDisable(false);
				x_USER_ROLE.setDisable(false);
				x_ACTIVATED.setDisable(false);
				x_ASSIGN_LGA_LBL.setVisible(true);
				x_ASSIGN_LGA.setVisible(true);
				x_ASSIGN_LGA.setItems(new FacilityService().getDropdownList("ALL_STORES"));
				new SelectKeyComboBoxListener(x_ASSIGN_LGA);
			}else{
				System.out.println("userBean.getX_USER_WAREHOUSE_NAME() : "+userBean.getX_USER_WAREHOUSE_NAME());
				System.out.println("MainApp.getUSER_WAREHOUSE_ID() : "+MainApp.getUSER_WAREHOUSE_ID());
				x_ASSIGN_LGA.setValue(new LabelValueBean(MainApp.userBean.getX_USER_WAREHOUSE_NAME(),MainApp.getUSER_WAREHOUSE_ID()));
			}
			break;
		case "CCO": // CCO - EMPLOYEE
			x_USER_TYPE_NAME.setDisable(true);
			x_USER_ROLE.setDisable(true);
			x_ACTIVATED_ON.setDisable(true);
			x_ACTIVATED.setDisable(true);
			x_STATUS.setDisable(true);
			x_START_DATE.setDisable(true);
			x_END_DATE.setDisable(true);
			x_ASSIGN_LGA_LBL.setVisible(false);
			x_ASSIGN_LGA.setVisible(false);
			x_ACTIVATED_ON_LBL.setVisible(true);
			x_ACTIVATED_ON.setVisible(true);
			break;
		}
	}
}