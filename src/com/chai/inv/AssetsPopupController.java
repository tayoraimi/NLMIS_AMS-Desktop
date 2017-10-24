package com.chai.inv;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.controlsfx.control.PopOver;

import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.model.UserWarehouseLabelValue;
import com.chai.inv.service.ItemService;

public class AssetsPopupController {

	@FXML
	private GridPane x_GRID_PANE;
        @FXML
        private Button x_TMC_BTN;
        @FXML
        private Button x_GENERATOR_BTN;
        @FXML
        private Button x_CCE_BTN;
        @FXML
        private Button x_TRANSPORT_BTN;


	private MainApp mainApp;
	private ItemService itemService;
	private RootLayoutController rootLayoutController;
	private UserBean userBean;
	private Stage primaryStage;
	private LabelValueBean role;
	private BorderPane mainBorderPane;
	private UserWarehouseLabelValue userWarehouseLabelValue;
	private HomePageController homePageController;
	private PopOver popup;

	public MainApp getMainApp() {
		return mainApp;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public ItemService getItemService() {
		return itemService;
	}

	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	public RootLayoutController getRootLayoutController() {
		return rootLayoutController;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
	}

	public UserBean getUserBean() {
		return userBean;
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setMainBorderPane(BorderPane mainBorderPane) {
		this.mainBorderPane = mainBorderPane;
	}

	@FXML
	public void handleTransportBtn() {
		popup.hide();
		// PHC : Primary Health Facilities
		System.out.println("entered handleTransportBtn()");
		//TransportMainController.showButtons = false;
		getRootLayoutController().handleTransportMenuAction();
	}

	@FXML
	public void handleGeneratorBtn() {
		popup.hide();
		System.out.println("entered handleGeneratorBtn()");
		getRootLayoutController().handleGeneratorMenuAction();
	}

	@FXML
	public void handleCCEBtn() {
		popup.hide();
		System.out.println("entered handleCCEBtn()");
		getRootLayoutController().handleCCEMenuAction();
		//getRootLayoutController().handleUserMenuAction();
	}

	@FXML
	public void handleTMCBtn() {
		popup.hide();
		System.out.println("entered handleTMCBtn()");
		getRootLayoutController().handleTMCMenuAction();
		//getRootLayoutController().handleCategoryMenuAction();
	}

	@FXML
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		getRootLayoutController().handleHomeMenuAction();
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "NTO":
			// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// x_GRID_PANE.getChildren().remove(x_USERS_BTN);
			// x_GRID_PANE.add(x_USERS_BTN, 0, 0);
			// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// BorderPane.setAlignment(x_GRID_PANE, Pos.TOP_CENTER);
			break;
		case "CCO": // EMPLOYEE - LGA cold chin officer - access to each and
					// every module.
			break;
		case "LIO": // SUPER USER - LGA level admin access restricted to
					// particular views.
					// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// x_GRID_PANE.getChildren().remove(x_USERS_BTN);
			// x_GRID_PANE.add(x_USERS_BTN, 0, 0);
			break;
		case "MOH": // SUPER USER - LGA level admin access restricted to
					// particular views.
			break;
		case "SIO": // Should have state level admin access ( they can correct
					// orders placed/ monitor data entered by the CCOs as well
					// as having a general summary of reports from all LGAs
					// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// x_GRID_PANE.getChildren().remove(x_USERS_BTN);
			break;
		case "SCCO": // Should have state level admin access ( they can correct
						// orders placed/ monitor data entered by the CCOs as
						// well as having a general summary of reports from all
						// LGAs
						// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// x_GRID_PANE.getChildren().remove(x_USERS_BTN);
			break;
		case "SIFP": // State immunization Focal person: Should have State admin
						// read only access
						// x_GRID_PANE.getChildren().remove(x_CATEGORIES_BTN);
			// x_GRID_PANE.getChildren().remove(x_USERS_BTN);
			// x_GRID_PANE.add(x_USERS_BTN, 0, 0);
			break;
		}
	}

	public void setPopupObject(PopOver popup) {
		this.popup=popup;
		
	}
}