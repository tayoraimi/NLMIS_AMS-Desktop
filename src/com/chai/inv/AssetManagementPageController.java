package com.chai.inv;

import java.io.IOException;
import java.util.logging.Level;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.controlsfx.control.PopOver;
import org.controlsfx.control.PopOver.ArrowLocation;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.model.UserWarehouseLabelValue;
import com.chai.inv.service.ItemService;

public class AssetManagementPageController {
	String movePageDirection = "farward";
	@FXML
	private GridPane x_GRID_PANE;
        @FXML
        private Button x_DASHBOARDS_BTN;
        @FXML
        private Button x_ASSETS_BTN;
        @FXML
        private Button x_WASTE_MANAGE_BTN;
        @FXML
        private Button x_REGISTER_ASSET_BTN;
        

	private AssetManagementPageController assetManagementPageController;
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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("Asset Management");
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
	public void handleAssetsPopupBtn() throws Exception {
		MainApp.LOGGER.severe("Assets Button is clicked");
		System.out.println("entered handleAssetsPopupBtn()");
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/AssetsPopup.fxml"));
		try {
			BorderPane assetManagementPage = (BorderPane) loader.load();
			if (mainBorderPane == null) {
				System.out.println("minborderpane is null in assetsubmenu controller");
				mainBorderPane = new BorderPane();
			}
			assetManagementPage.setUserData(loader);
			AssetsPopupController controller = loader.getController();
			controller.setRootLayoutController(rootLayoutController);
			controller.setRole(role);
			controller.setMainApp(mainApp);
			controller.setUserBean(userBean);
			controller.setPrimaryStage(primaryStage);
			PopOver popup=new PopOver();
			controller.setPopupObject(popup);
			popup.setPrefSize(200, 200);
			popup.setCornerRadius(25);
			popup.setArrowLocation(ArrowLocation.LEFT_CENTER);
			popup.setContentNode(assetManagementPage);
			popup.setHideOnEscape(true);
			popup.setDetachable(false);
			popup.setPrefSize(300, 300);
			popup.setAutoHide(true);
			popup.setStyle("-fx-background-color: red");
			popup.show(x_ASSETS_BTN);
		} catch (IOException ex) {
			System.out.println("Error occured while loading Asset Management Page layout.. "
		+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("Error occured while loading Asset Management Page layout.. "
			+MyLogger.getStackTrace(ex));
			ex.printStackTrace();
		}
	}

	@FXML
	public void handleWasteManagementPopup() {
		
	}
        
        @FXML
	public void handleAssetRegisterBtn() {
		// PHC : Primary Health Facilities
		System.out.println("entered handleAssetRegisterBtn()");
		//TransportMainController.showButtons = false;
		getRootLayoutController().handleAssetRegisterMenuAction();
	}

	@FXML
	public void handleAssetDashboardsPopupBtn() throws Exception {
		MainApp.LOGGER.severe("handleReportsPopupBtn");
		System.out.println("entered in assetManagementPage.handleAssetDashboardsPopupBtn()");
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/chai/inv/view/AssetDashboardPopup.fxml"));
                try {
                        BorderPane popupContent = (BorderPane) loader.load();
			if (mainBorderPane == null) {
				System.out.println("minborderpane is null in asset dashboard submenu controller");
				mainBorderPane = new BorderPane();
			}
                        popupContent.setUserData(loader);
                        AssetDashboardPopupController controller = loader.getController();
                        controller.setRootLayoutController(rootLayoutController);
                        System.out.println("role : "+role.getLabel());
                        controller.setRole(role);
                        controller.setMainBorderPane(mainBorderPane);
			controller.setMainApp(mainApp);
                        controller.setUserBean(userBean);
                        controller.setPrimaryStage(primaryStage);
			PopOver popup=new PopOver();
			controller.setPopupObject(popup);
			popup.setPrefSize(200, 200);
			popup.setCornerRadius(25);
			popup.setArrowLocation(ArrowLocation.TOP_CENTER);
			popup.setContentNode(popupContent);
			popup.setHideOnEscape(true);
			popup.setDetachable(false);
			popup.setPrefSize(300, 300);
			popup.setAutoHide(true);
			popup.setStyle("-fx-background-color: red");
			popup.show(x_DASHBOARDS_BTN);
		} catch (IOException ex) {
			System.out.println("Error occured while loading Asset Management Page layout.. "
		+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("Error occured while loading Asset Management Page layout.. "
			+MyLogger.getStackTrace(ex));
			ex.printStackTrace();
		}
	}

	@FXML
	public void handleNotifications() {
		System.out.println("In AssetManagementPagecontroller.handleNotifications() mehtod");
	
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
				System.out.println("Exception In AssetManagementPageController.handleNotifications() mehtod: ");
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe("Exception In AssetManagementPageController.handleNotifications() mehtod: "
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
				
				break;
			case "CCO": // EMPLOYEE - LGA cold chain officer - access to each and
						// every module.
				break;
			case "LIO": // SUPER USER - LGA level admin access restricted to
						// particular views.
				//for hide
				//for add

				break;
			case "MOH": // SUPER USER - LGA level admin access restricted to
						// particular views.
				//for hide
				
				break;
			case "SIO": // Should have state level admin access ( they can
						// correct orders placed/ monitor data entered by the
						// CCOs as well as having a general summary of reports
						// from all LGAs		
				break;
			case "SCCO": // Should have state level admin access ( they can
							// correct orders placed/ monitor data entered by
							// the CCOs as well as having a general summary of
//							// reports from all LGAs
				//for hide		
				break;
			case "SIFP": // Should have state level admin access ( they can
				// correct orders placed/ monitor data entered by
				// the CCOs as well as having a general summary of
//				// reports from all LGAs
	//for hide
	break;
			}
		}
	}

}