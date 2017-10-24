package com.chai.inv;

import static com.chai.inv.RootLayoutController.mainBorderPane;
import java.io.IOException;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import org.controlsfx.control.PopOver;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.ItemService;

public class AssetDashboardPopupController {

	String movePageDirection="farward";

	@FXML
	private Button x_CCE_DASHBOARD_BTN;
	@FXML
        private Button x_CCE_CAP_MR_DASHBOARD_BTN;
        @FXML
        private Button x_CCE_MEN_A_DASHBOARD_BTN;
        @FXML
        private Button x_CCE_CAP_HPV_DASHBOARD_BTN;
        @FXML
        private Button x_CCE_CAP_DASHBOARD_BTN;
        @FXML
        private Button x_CCE_CAP_ROTA_DASHBOARD_BTN;
        @FXML
        private Button x_CCE_CAP_RI_DASHBOARD_BTN;
	@FXML
	private GridPane x_GRID_PANE;
	@FXML 
	HBox x_HBOX_HOME_BACK;
	@FXML
	HBox x_HBOX_TITLE;

	private MainApp mainApp;
	private ItemService itemService;
	private RootLayoutController rootLayoutController;
	private UserBean userBean;
	private Stage primaryStage;
	public LabelValueBean role;
	private BorderPane mainBorderPane;
	private HomePageController homePageController;

	private PopOver popup;

	public static ReportsButtonPopupController reportsButtonPopupController;

	public HomePageController getHomePageController() {
		return homePageController;
	}

	public void setHomePageController(HomePageController homePageController) {
		this.homePageController = homePageController;
	}

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
	public void setReportButtonPopupCont(
			ReportsButtonPopupController reportsButtonPopupController) {
		LGAReportsSubController.reportsButtonPopupController=reportsButtonPopupController;
	}
	@FXML
	public void handleWastageReportDashboardBtn() {
		System.out.println("entered handleWastageReportDashboardBtn()");
		try {
			popup.hide();
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/LGAWastageReport.fxml"));
			BorderPane homePage = (BorderPane) loader.load();
			homePage.setUserData(loader);
			new SetTransitionOnScreen().setTransition(mainBorderPane,"parrallelFadeTranslate", movePageDirection);
			mainBorderPane.setCenter(homePage);
			LGAWastageReportController controller = loader.getController();
			controller.setRootLayoutController(rootLayoutController);
			controller.setHomePageController(homePageController);
			controller.setRole(role);
			controller.setUserBean(userBean);
			controller.setPrimaryStage(primaryStage);
			controller.setReportButtonPopupCont(reportsButtonPopupController);
			//controller.setLgaReportSubController(this);
			controller.setDefaults();
		} catch (IOException |NullPointerException e) {
				e.printStackTrace();
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe("Error occured while loading Home Page layout.. "+
				MyLogger.getStackTrace(e));
				System.out.println("Error occured while loading Home Page layout.. "
				+ e.getMessage());
			}catch(Exception e){
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe("Error occured while loading Home Page layout.. "+
				MyLogger.getStackTrace(e));
				System.out.println("Error occured while loading Home Page layout.. "+ 
				e.getMessage());
			}
	}
        
	@FXML
	public void handleCCEStatusDashboardBtn(){
		popup.hide();
		System.out.println("entered handleCCEBtn()");
		getRootLayoutController().handleCCEStatusDashboardMenuAction();
	}
	@FXML
	public void handleCCECapRiDashboardBtn(){
		popup.hide();
		System.out.println("entered handleCCECapRiBtn()");
		getRootLayoutController().handleCCECapRiDashboardMenuAction();
	}
	@FXML
	public void handleCCECapacityDashboardBtn(){
		popup.hide();
		System.out.println("entered handleCCECapacityBtn()");
		getRootLayoutController().handleCCECapacityDashboardMenuAction();
	}
	@FXML
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		getRootLayoutController().handleHomeMenuAction();
	}
	@FXML
	public void handleBackToInventorySubMenu() throws Exception {
		System.out.println("entered handleBackToInventorySubMenu()");
		homePageController.movePageDirection = "backward";
		reportsButtonPopupController.handleLgaReportSubDashboard();
	}
	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "CCO": // EMPLOYEE - LGA cold chin officer - access to each and
					// every module.
			break;
		case "LIO": // SUPER USER - LGA level admin access restricted to
					// particular views.
			break;
		case "MOH": // SUPER USER - LGA level admin access restricted to
					// particular views.
			break;
		case "SIO": // Should have state level admin access ( they can correct\
			break;
		case "SCCO": // Should have state level admin access ( they can correct
			break;
		case "SIFP": // State immunization Focal person: Should have State admin
						// read only access
			break;
		case "NTO":
		break;
		}
	}

	public void setPopupObject(PopOver popup) {
		this.popup=popup;
		
	}

}
