package com.chai.inv;

import com.chai.inv.DAO.DatabaseOperation;
import static com.chai.inv.RootLayoutController.mainBorderPane;
import java.io.IOException;
import java.util.logging.Level;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.model.UserWarehouseLabelValue;
import com.chai.inv.service.ItemService;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LandingPageController {
	String movePageDirection = "farward";
	@FXML
	private GridPane x_GRID_PANE;
	@FXML
	private AnchorPane x_ANCHOR_PANE;

	@FXML
	private Button x_MODULE1_BTN;
	
	@FXML
	private Button x_MODULE2_BTN;
	

	private HomePageController homePageController;
        private AssetManagementPageController assetManagementController;
        private LandingPageController landingPageController;
	private MainApp mainApp;
	private ItemService itemService;
	private RootLayoutController rootLayoutController;
	private UserBean userBean;
	private Stage primaryStage;
	public LabelValueBean role;
	private BorderPane mainBorderPane;
	private UserWarehouseLabelValue userWarehouseLabelValue;
	private LabelValueBean warehouseLvb;

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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("N-LMIS/ASSET MANAGEMENT");
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
        public void loadHomePage() throws SQLException {
            
				DatabaseOperation.CONNECT_TO_SERVER = false;
		System.out.println("RootLayout loadHomePage Called..");
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/HomePage.fxml"));
		try {
			BorderPane homePage = (BorderPane) loader.load();
			homePage.setUserData(loader);
			homePage.getStylesheets().add(RootLayoutController.class.getResource("/com/chai/inv/view/DisabledComboBoxOpacity.css")
							.toExternalForm());
			new SetTransitionOnScreen().setTransition(mainBorderPane,"parrallelFadeScale", null);
			mainBorderPane.setCenter(homePage);
			HomePageController controller = loader.getController();
			this.homePageController = controller;
			controller.setRootLayoutController(rootLayoutController);
			controller.setRole(role, true);
			controller.setMainBorderPane(mainBorderPane);
			controller.setMainApp(mainApp);
			controller.setUserBean(userBean);
			controller.setPrimaryStage(primaryStage);
		} catch (Exception ex) {
			System.out.println("Error occured while loading Home Page layout.. "+ ex.getMessage());
			ex.printStackTrace();
		}
	}
	
        @FXML 
                public void handleAssetManagementAction() {
                    
				DatabaseOperation.CONNECT_TO_SERVER = true;
		System.out.println("Asset Management selected..");
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/AssetManagementPage.fxml"));
		try {
                        BorderPane userOverviewPage = (BorderPane) loader.load();
			userOverviewPage.setUserData(loader);
			new SetTransitionOnScreen().setTransition(mainBorderPane,"parrallelFadeTranslate", movePageDirection);
			mainBorderPane.setCenter(userOverviewPage);
			AssetManagementPageController controller = loader.getController();
			controller.setMainApp(mainApp);
			controller.setRootLayoutController(rootLayoutController);
			//controller.setHomePageController(homePageController);
			controller.setRole(role,true);
			controller.setUserBean(userBean);
			controller.setPrimaryStage(primaryStage);
			//controller.setUserListData();
		} catch (Exception ex) {
			System.out.println("Error occured while loading asset management layout.. "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("Error occured while loading asset management layout.."+MyLogger.getStackTrace(ex));
		}
	}

        
	@FXML
	public void handleNotifications() {
		System.out.println("In HomePagecontroller.handleNotifications() mehtod");
	
			try {
				FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/NotificationGrid.fxml"));
				System.out.println("After fxml loader..");
				BorderPane notificationGrid = (BorderPane) loader.load();
				System.out.println("After fxml loader..2");
				mainBorderPane.setCenter(notificationGrid);
				System.out.println("After fxml loader..3");
//				NotificationController controller = loader.getController();
//				System.out.println("After fxml loader..4");
//				controller.setRootLayoutController(rootLayoutController);
//				controller.setRole(role);
//				controller.setUserBean(userBean);
//				controller.setPrimaryStage(primaryStage);
			}catch (NullPointerException | IOException e) {
				e.printStackTrace();
				System.out.println("Exception In HomePageController.handleNotifications() mehtod: ");
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe("Exception In HomePageController.handleNotifications() mehtod: "
				+MyLogger.getStackTrace(e));
			}
	}

	@FXML public void handleChangeFacilityDashboardBtn() throws Exception {
		getRootLayoutController().handleSelectWarehouse();
	}

	
	public void setRole(LabelValueBean role, Boolean calledFromRootLayout) {
		this.role = role;
		if (calledFromRootLayout) {
			switch (role.getLabel()) {
			case "NTO": // SUPER ADMIN - access to each and every module.
				if(MainApp.selectedLGA==null){
					System.out.println("called NTO switch.case");
					//x_GRID_PANE.getChildren().add(x_MODULE2_BTN);
				}
				break;
			case "CCO": // EMPLOYEE - LGA cold chain officer - access to each and
						// every module.
				//x_GRID_PANE.getChildren().add(x_MODULE2_BTN);
				break;
			case "LIO": // SUPER USER - LGA level admin access restricted to
						// particular views.
				
				//x_GRID_PANE.getChildren().add(x_MODULE2_BTN);

				break;
			case "MOH": // SUPER USER - LGA level admin access restricted to
						// particular views.
				
				x_GRID_PANE.getChildren().add(x_MODULE2_BTN);
				
				break;
			case "SIO": // Should have state level admin access ( they can
						// correct orders placed/ monitor data entered by the
						// CCOs as well as having a general summary of reports
						// from all LGAs
				x_GRID_PANE.getChildren().add(x_MODULE2_BTN);		
				break;
			case "SCCO": // Should have state level admin access ( they can
							// correct orders placed/ monitor data entered by
							// the CCOs as well as having a general summary of
//							// reports from all LGAs
				//for hide
				//x_GRID_PANE.getChildren().add(x_MODULE2_BTN);		
				break;
			case "SIFP": // Should have state level admin access ( they can
				// correct orders placed/ monitor data entered by
				// the CCOs as well as having a general summary of
//				// reports from all LGAs
	x_GRID_PANE.getChildren().add(x_MODULE2_BTN);
	break;
			}
		}
	}

}