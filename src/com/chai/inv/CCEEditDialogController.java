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

import com.chai.inv.model.CCEBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CommonService;
import com.chai.inv.service.CCEService;
import com.chai.inv.service.FacilityService;
import com.chai.inv.util.CalendarUtil;
import com.chai.inv.util.SelectKeyComboBoxListener;
import java.text.DateFormatSymbols;
import java.time.LocalDate;

public class CCEEditDialogController {
        
        
        @FXML
        private ComboBox<LabelValueBean> x_CCE_DECISION;
//        @FXML
//        private ComboBox<LabelValueBean> x_CCE_DESIGNATION;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_TYPE;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_LOCATION;
        @FXML
        private ComboBox<String> x_CCE_ACQUISITION1;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_ACQUISITION2;
        @FXML
        private TextField x_CCE_SERIAL;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_MODEL;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_MAKE;
        @FXML
        private DatePicker x_CCE_NF_DATE;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_STATUS;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_SRC;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_LGA;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_CATEGORY;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_WARD;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_FACILITY_NAME;
        @FXML
        private ComboBox<LabelValueBean> x_CCE_STATE;
        @FXML
        private Label x_CCE_DATE_NF_F;

        
        @FXML
        private Button x_CANCEL_BTN;
        @FXML
        private Button x_OK_BTN;
        
	private boolean okClicked = false;
	private String actionBtnString;
	private boolean state_cce_record = false;
	private CCEBean cceBean;
	private RootLayoutController rootLayoutController;
	private Stage dialogStage;
	private CCEService cceService;
	private CCEMainController cceMain;
	private UserBean userBean;
	private LabelValueBean role;
        private int nfDuration = 0;
        private String status;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public CCEMainController getCCEMain() {
		return cceMain;
	}

	public void setCCEMain(CCEMainController cceMain) {
		this.cceMain = cceMain;
	}

	public void setCCEService(CCEService cceService,
			String actionBtnString) {
		this.cceService = cceService;
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

	public void setCCEBeanFields(CCEBean cceBean,
			LabelValueBean statelabelValueBean,
			LabelValueBean lgalabelValueBean,
			LabelValueBean wardlabelValueBean) {
		this.cceBean = new CCEBean();
		this.cceBean = cceBean;

		System.out.println("Warehouse ID: " + userBean.getX_USER_WAREHOUSE_ID());
                
                
//                x_CCE_LOCATION.setDisable(true);
                if (actionBtnString.equals("add")) {
//                        cceBean.setX_CCE_FACILITY_ID(userBean.getX_WAREHOUSE_ID());
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                                x_CCE_STATE.getItems().addAll(new LabelValueBean("", null));
				x_CCE_STATE.setItems(cceService.getDropdownList("defaultstorelist", null));
				x_CCE_STATE.setPromptText("State");
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				x_CCE_STATE.setValue(new LabelValueBean(userBean.getX_USER_WAREHOUSE_NAME(), userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_STATE.setDisable(true);
                                x_CCE_LGA.getItems().addAll(new LabelValueBean("", null));
				x_CCE_LGA.setItems(cceService.getDropdownList("defaultstorelist",
								userBean.getX_USER_WAREHOUSE_ID()));
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_CCE_STATE.setValue(new LabelValueBean(userBean
						.getX_USER_WAREHOUSE_NAME(), userBean
						.getX_USER_WAREHOUSE_ID()));
				x_CCE_STATE.setDisable(true);
                                x_CCE_LGA.getItems().addAll(new LabelValueBean("", null));
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist","ALL_LGA_UNDER_STATE"));
				x_CCE_LGA
						.setPromptText("LGA");
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list

                                x_CCE_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_STATE.setDisable(true);
				x_CCE_LGA.setItems(cceService.getDropdownList("defaultstorelist"));
				x_CCE_LGA.setValue(x_CCE_LGA.getItems().get(0));
				x_CCE_LGA.setDisable(true);
                                System.out.println("To search for ward, LGA value is "+x_CCE_LGA.getItems().get(0).getValue());
                                x_CCE_WARD.getItems().addAll(new LabelValueBean("", null));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						x_CCE_LGA.getItems().get(0).getValue()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean("LGA",null));
                             
			} else if (MainApp.getUserRole().getLabel().equals("LIO")) {
				// get only the selected LGA name in the list

                                x_CCE_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_STATE.setDisable(true);
				x_CCE_LGA.setItems(cceService.getDropdownList("defaultstorelist"));
				x_CCE_LGA.setValue(new LabelValueBean(userBean
						.getX_USER_WAREHOUSE_NAME(), userBean
						.getX_USER_WAREHOUSE_ID()));
				x_CCE_LGA.setDisable(true);
                                System.out.println("To search for ward, LGA value is "+x_CCE_LGA.getItems().get(0).getValue());
                                x_CCE_WARD.getItems().addAll(new LabelValueBean("", null));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						x_CCE_LGA.getItems().get(0).getValue()));
				x_CCE_LOCATION.setValue(new LabelValueBean("LGA",null));
                             
			}
                        
                                //x_CCE_ACQUISITION1.setItems(CalendarUtil.getYear());
                                //x_CCE_ACQUISITION1.getItems().addAll("2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006","2005","2004","2003","2002","2001","2000","1999","1998","1997","1996");
                                x_CCE_ACQUISITION1.setItems(CalendarUtil.get20Years());
                                //x_CCE_DESIGNATION.setItems(cceService.getDropdownList("DesignationList",null));
                                x_CCE_MAKE.setItems(cceService.getDropdownList("MakeList",null));
                                x_CCE_MODEL.setItems(cceService.getDropdownList("ModelList",null));
                                x_CCE_TYPE.setItems(cceService.getDropdownList("TypeList",null,null));
                                x_CCE_CATEGORY.setItems(cceService.getDropdownList("CategoryList",null,null,null));
                                x_CCE_STATUS.setItems(cceService.getDropdownList("StatusList",null));
                                x_CCE_DECISION.setItems(cceService.getDropdownList("DecisionList",null));
                                x_CCE_SRC.setItems(cceService.getDropdownList("SourceList"));

		}else if (actionBtnString.equals("edit")) {
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_CCE_STATE.setValue(new LabelValueBean(cceBean
						.getX_CCE_STATE(), cceBean
						.getX_CCE_STATE_ID()));
				x_CCE_STATE.setDisable(true);
				x_CCE_LGA.setDisable(false);
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist", null));
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(
						cceBean.getX_CCE_WARD(), cceBean
								.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(
                                                cceBean.getX_CCE_FACILITY_NAME(),cceBean
                                                                .getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_CCE_STATE.setValue(new FacilityService()
						.getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_STATE.setDisable(true);
				x_CCE_LGA.setDisable(true);
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist", null));
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(
						cceBean.getX_CCE_WARD(), cceBean
								.getX_CCE_WARD_ID()));
				new SelectKeyComboBoxListener(x_CCE_WARD);
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(cceBean.getX_CCE_FACILITY_NAME(),
						cceBean.getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
			} else if (MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list
//				x_CCE_STATE.setValue(new FacilityService()
//						.getStateStoreId(cceBean.getX_CCE_LGA_ID()));
				x_CCE_STATE.setDisable(true);
                                x_CCE_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_LGA.setDisable(true);
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(cceBean.getX_CCE_WARD(),
						cceBean.getX_CCE_WARD_ID()));
				new SelectKeyComboBoxListener(x_CCE_WARD);
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(cceBean.getX_CCE_FACILITY_NAME(),
						cceBean.getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
			}

			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_CCE_STATE.setItems(cceService.getDropdownList(
						"defaultstorelist", null));
				new SelectKeyComboBoxListener(x_CCE_STATE);
				x_CCE_STATE.setValue((new LabelValueBean(cceBean
						.getX_CCE_STATE(), cceBean
						.getX_CCE_STATE_ID())));
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist", x_CCE_STATE
								.getValue().getValue()));
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(cceBean.getX_CCE_WARD(),
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(cceBean.getX_CCE_FACILITY_NAME(),
						cceBean.getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_CCE_STATE.setDisable(true);
				// x_CCE_STATE.setItems(cceService.getDropdownList("defaultstorelist",null));
				x_CCE_STATE.setValue(new LabelValueBean(cceBean
						.getX_CCE_STATE(), cceBean
						.getX_CCE_STATE_ID()));
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist", x_CCE_STATE
								.getValue().getValue()));
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(cceBean.getX_CCE_WARD(),
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(cceBean.getX_CCE_FACILITY_NAME(),
						cceBean.getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
			}else if (MainApp.getUserRole().getLabel().equals("LIO")) {
				// get only the selected LGA name in the list

                                x_CCE_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_CCE_LGA.setItems(cceService
						.getDropdownList("defaultstorelist", x_CCE_STATE
								.getValue().getValue()));
				x_CCE_LGA.setValue(new LabelValueBean(
						cceBean.getX_CCE_LGA(), cceBean
								.getX_CCE_LGA_ID()));
				x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
						cceBean.getX_CCE_LGA_ID()));
				x_CCE_WARD.setValue(new LabelValueBean(cceBean.getX_CCE_WARD(),
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
						cceBean.getX_CCE_WARD_ID()));
				x_CCE_FACILITY_NAME.setValue(new LabelValueBean(cceBean.getX_CCE_FACILITY_NAME(),
						cceBean.getX_CCE_FACILITY_ID()));
//                                x_CCE_LOCATION.setItems(cceService.getDropdownList("EquipmentLocationList",
//                                                cceBean.getX_CCE_FACILITY_ID()));
				x_CCE_LOCATION.setValue(new LabelValueBean(cceBean.getX_CCE_LOCATION(),null));
                             
			}
                        
                        
//                        if (!(cceBean.getX_CCE_DESIGNATION()== null)) {
//                        x_CCE_DESIGNATION.setItems(cceService.getDropdownList("DesignationList"));
//			x_CCE_DESIGNATION.setValue(new LabelValueBean(cceBean.getX_CCE_DESIGNATION(),null));
//                        }
                        if (!(cceBean.getX_CCE_MAKE()== null)) {
                                x_CCE_MAKE.setItems(cceService.getDropdownList("MakeList"));
                                x_CCE_MAKE.setValue(new LabelValueBean(cceBean.getX_CCE_MAKE(),null));
                        }
                        if (!(cceBean.getX_CCE_MODEL() == null)) {
                                x_CCE_MODEL.setItems(cceService.getDropdownList("ModelList",cceBean.getX_CCE_MAKE()));
                                x_CCE_MODEL.setValue(new LabelValueBean(cceBean.getX_CCE_MODEL(),null));
                        }
                        if (!(cceBean.getX_CCE_TYPE() == null)) {
                                x_CCE_TYPE.setItems(cceService.getDropdownList("TypeList",
                                        cceBean.getX_CCE_MAKE(),cceBean.getX_CCE_MODEL()));
                                x_CCE_TYPE.setValue(new LabelValueBean(cceBean.getX_CCE_TYPE(),null));
                        }
                        if (!(cceBean.getX_CCE_CATEGORY()== null)) {
                                x_CCE_CATEGORY.setItems(cceService.getDropdownList("CategoryList",
                                        cceBean.getX_CCE_MAKE(),cceBean.getX_CCE_MODEL(),cceBean.getX_CCE_TYPE()));
                                x_CCE_CATEGORY.setValue(new LabelValueBean(cceBean.getX_CCE_CATEGORY(),null));
                        }
                        if (!(cceBean.getX_CCE_SERIAL_NO() == null)) {
                                x_CCE_SERIAL.setText(cceBean.getX_CCE_SERIAL_NO());
                        }
                        if (!(cceBean.getX_CCE_STATUS()== null)) {
                                x_CCE_STATUS.setItems(cceService.getDropdownList("StatusList"));
                                x_CCE_STATUS.setValue(new LabelValueBean(cceBean.getX_CCE_STATUS(),null));
                        }

                        if (cceBean.getX_CCE_STATUS()!= null){
                            System.out.println("cceBean.Status is "+cceBean.getX_CCE_STATUS());
                            handleOnStatusChange();

                        }
                        if (!(cceBean.getX_CCE_DECISION()== null)) {
                                x_CCE_DECISION.setItems(cceService.getDropdownList("DecisionList",cceBean.getX_CCE_STATUS()));
                                x_CCE_DECISION.setValue(new LabelValueBean(cceBean.getX_CCE_DECISION(),null));
                        }
                        if (!(cceBean.getX_CCE_DATE_NF()== null)) {
                            x_CCE_NF_DATE.setValue(CalendarUtil.fromString(cceBean.getX_CCE_DATE_NF()));
                        }
                        x_CCE_ACQUISITION1.setItems(CalendarUtil.get20Years());
                        if (!(cceBean.getX_CCE_YEAR_OF_ACQUISITION()== null)) {
                            System.out.println("The cce Year of Acquisition is "+cceBean.getX_CCE_YEAR_OF_ACQUISITION());
                            x_CCE_ACQUISITION1.setValue(cceBean.getX_CCE_YEAR_OF_ACQUISITION());
                        }
                        x_CCE_ACQUISITION2.setItems(CalendarUtil.getMonthAndNumber("short_month_inyear"));
                        if (!(cceBean.getX_CCE_MONTH_OF_ACQUISITION()== null)) {
                            System.out.println("The cce Month of Acquisition is "+cceBean.getX_CCE_MONTH_OF_ACQUISITION());
                            x_CCE_ACQUISITION2.setValue(new LabelValueBean(new DateFormatSymbols().getShortMonths()[Integer.parseInt(cceBean.getX_CCE_MONTH_OF_ACQUISITION())],cceBean.getX_CCE_MONTH_OF_ACQUISITION()));
                        }
                        if (!(cceBean.getX_CCE_SOURCE()== null)) {
                            x_CCE_SRC.setValue(new LabelValueBean(cceBean.getX_CCE_SOURCE(),null));
                            x_CCE_SRC.setItems(cceService.getDropdownList("SourceList"));
                        }

                
		}
		
		
	}

        @FXML public void onYearChange(){
		if(x_CCE_ACQUISITION1.getValue()!=null){
			//x_CCE_ACQUISITION2.setItems(new CalendarUtil().getMonth(x_CCE_ACQUISITION1.getValue()));
                        x_CCE_ACQUISITION2.setItems(CalendarUtil.getMonthAndNumber("short_month_inyear"));
		}
	}

	@FXML
	private void handleOnStatusChange() {
            
            x_CCE_DECISION.setValue(new LabelValueBean("",""));
            if (x_CCE_STATUS.getValue().getLabel().equals("Not Functional")) {
                        x_CCE_DATE_NF_F.setText("Date Not Functional");
                        x_CCE_NF_DATE.setPromptText("Date Not Functional");
//                        x_CCE_NF_DATE.setDisable(false);
                        x_CCE_NF_DATE.setValue(LocalDate.now());
                        x_CCE_ACQUISITION1.setDisable(false);
                        x_CCE_ACQUISITION2.setDisable(false);
                        if(!(cceBean.getX_CCE_DATE_NF()==null)){
                            x_CCE_ACQUISITION1.setValue(cceBean.getX_CCE_YEAR_OF_ACQUISITION());
                            x_CCE_ACQUISITION2.setValue(new LabelValueBean(new DateFormatSymbols().getShortMonths()[Integer.parseInt(cceBean.getX_CCE_MONTH_OF_ACQUISITION())],cceBean.getX_CCE_MONTH_OF_ACQUISITION()));
                        }
                        else{
                            x_CCE_ACQUISITION1.setValue(null);
                            x_CCE_ACQUISITION2.setValue(null);
                        }
                        
            }
            else{
                x_CCE_DATE_NF_F.setText("Date Functional");
                x_CCE_NF_DATE.setPromptText("Date Functional");
                x_CCE_ACQUISITION1.setDisable(false);
                x_CCE_ACQUISITION2.setDisable(false);
//                x_CCE_NF_DATE.setDisable(true);
                x_CCE_NF_DATE.setValue(LocalDate.now());
                if(x_CCE_STATUS.getValue().getLabel().equals("Not Installed")){
                    x_CCE_ACQUISITION1.setValue(null);
                    x_CCE_ACQUISITION2.setValue(null);
                    x_CCE_ACQUISITION1.setDisable(true);
                    x_CCE_ACQUISITION2.setDisable(true);
                }
            }
            
            
            x_CCE_DECISION.setPromptText("Decision");
            x_CCE_DECISION.setItems(cceService.getDropdownList("DecisionList",x_CCE_STATUS.getValue().getLabel()));
	}
        
        @FXML
        private void handleOnStateStoreChange() {
                x_CCE_LGA.setPromptText("LGA");                                                                                                        // initial
                x_CCE_WARD.setPromptText("Ward");                                                                                                        // initial
                x_CCE_FACILITY_NAME.setPromptText("Site Name");                                                                                                        // initial
//                x_CCE_LOCATION.setPromptText("Equipment Location");
                if (x_CCE_STATE.getValue() != null 
                        && !x_CCE_STATE.getValue().getLabel().equalsIgnoreCase("State")) {
			x_CCE_LGA.getItems().addAll(
					new LabelValueBean("LGA", null));
                        x_CCE_LGA.setItems(cceService.getDropdownList(
                                        "defaultstorelist", x_CCE_STATE.getValue().getValue()));
                        new SelectKeyComboBoxListener(x_CCE_LGA);
			x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
					x_CCE_STATE.getValue().getValue()));
			x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
					x_CCE_WARD.getValue().getValue()));
			x_CCE_LOCATION.setValue(new LabelValueBean("STATE",null));
                }
        }
        @FXML
	private void handleOnLgaStoreChange() {
		x_CCE_WARD.setPromptText("Ward");                                                                                                    // initial
//                x_CCE_LOCATION.setPromptText("Equipment Location");
		x_CCE_FACILITY_NAME.setPromptText("Site Name");   
		if (x_CCE_LGA.getValue() != null) {
			x_CCE_WARD.getItems().addAll(
					new LabelValueBean("Ward", null));
			x_CCE_WARD.setItems(cceService.getDropdownList("WardList",
					x_CCE_LGA.getValue().getValue()));
			new SelectKeyComboBoxListener(x_CCE_WARD);
			x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
					x_CCE_LGA.getValue().getValue()));
			x_CCE_LOCATION.setValue(new LabelValueBean("LGA",null));
		}

	}
        @FXML
	private void handleOnWardStoreChange() {
            System.out.println("**In handleOnWardStoreChange");
		x_CCE_FACILITY_NAME.setPromptText("Site Name");
//                x_CCE_LOCATION.setPromptText("Equipment Location");
		if (x_CCE_WARD.getValue() != null) {
//                        x_CCE_LOCATION.setValue("");
			x_CCE_FACILITY_NAME.getItems().addAll(
					new LabelValueBean("Site Name", null));
			x_CCE_FACILITY_NAME.setItems(cceService.getDropdownList("FacilityList",
					x_CCE_WARD.getValue().getValue()));
			new SelectKeyComboBoxListener(x_CCE_FACILITY_NAME);
			x_CCE_LOCATION.setValue(new LabelValueBean("LGA",null));
                        
		}

	}
        @FXML
	private void handleOnFacilityChange() {
            System.out.println("**In handleOnFacilityChange");
//		x_CCE_LOCATION.setPromptText("Equipment Location");
		if (x_CCE_FACILITY_NAME.getValue() != null) {
			x_CCE_LOCATION.setValue(new LabelValueBean("HF",null));
		}

	}
        
        
//        @FXML
//        private void handleOnDesignationChange() {
//                    x_CCE_CATEGORY.setPromptText("Category"); 
//                    x_CCE_CATEGORY.setDisable(false);                                                                                                            // initial
//                    x_CCE_MAKE.setPromptText("Company");                                                                                                       // initial
//                    x_CCE_MODEL.setPromptText("Model");
//                    
//                if (x_CCE_DESIGNATION.getValue() != null 
//                        && !x_CCE_DESIGNATION.getValue().getLabel().equals("")) {
//                    x_CCE_CATEGORY.setItems(cceService.getDropdownList(
//                                    "CategoryList", x_CCE_MAKE.getValue().getLabel(),
//                                    x_CCE_MODEL.getValue().getLabel(),x_CCE_TYPE.getValue().getLabel()));
//                    new SelectKeyComboBoxListener(x_CCE_CATEGORY);
//                }
//        }
        @FXML
	private void handleOnMakeChange() {
            System.out.println("**In handleOnMakeChange");
		if (x_CCE_MAKE.getValue() != null 
                        && !x_CCE_MAKE.getValue().getLabel().equals("")) {
                        x_CCE_MODEL.setPromptText("Model"); 
                        x_CCE_MODEL.setDisable(false);     
			//x_CCE_MODEL.getItems().addAll(new LabelValueBean("", null));
			x_CCE_MODEL.setItems(cceService.getDropdownList("ModelList",
					x_CCE_MAKE.getValue().getLabel()));
			new SelectKeyComboBoxListener(x_CCE_MODEL);
                        x_CCE_ACQUISITION1.setValue(null);
                        x_CCE_ACQUISITION2.setValue(null);
                        x_CCE_DECISION.setItems(null);
                        x_CCE_STATUS.setItems(null);
                        x_CCE_NF_DATE.setValue(null);
                        x_CCE_SERIAL.setText("");
                        x_CCE_SRC.setItems(cceService.getDropdownList("SourceList"));
		}

	}
        @FXML
	private void handleOnModelChange() {
            System.out.println("**In handleOnModelChange");
		if (x_CCE_MODEL.getValue() != null 
                        && !x_CCE_MODEL.getValue().getLabel().equals("")) {
                        x_CCE_ACQUISITION1.setDisable(false);
                        x_CCE_ACQUISITION2.setDisable(false);
                        x_CCE_DECISION.setDisable(false);
//                        x_CCE_STATUS.setDisable(false);
			x_CCE_TYPE.setItems(cceService.getDropdownList("TypeList",
					x_CCE_MAKE.getValue().getLabel(),x_CCE_MODEL.getValue().getLabel()));
                        x_CCE_SERIAL.setDisable(false);
                        x_CCE_SRC.setDisable(false);
			new SelectKeyComboBoxListener(x_CCE_TYPE);
                        
//			x_CCE_DECISION.getItems().addAll(new LabelValueBean("", null));
//                        x_CCE_STATUS.getItems().addAll(new LabelValueBean("", null));
//                        x_CCE_SOURCE.getItems().addAll(new LabelValueBean("", null));
                        
		}

	}
        @FXML
	private void handleOnTypeChange() {
            System.out.println("**In handleOnTypeChange");
		if (x_CCE_TYPE.getValue() != null 
                        && !x_CCE_TYPE.getValue().getLabel().equals("")) {
                    x_CCE_CATEGORY.setPromptText("Category"); 
                    x_CCE_CATEGORY.setDisable(false);     
//                    x_CCE_CATEGORY.getItems().addAll(
//                                    new LabelValueBean("", null));
                    x_CCE_CATEGORY.setItems(cceService.getDropdownList("CategoryList",
					x_CCE_MAKE.getValue().getLabel(), 
                                        x_CCE_MODEL.getValue().getLabel(), 
                                        x_CCE_TYPE.getValue().getLabel()));
			new SelectKeyComboBoxListener(x_CCE_CATEGORY);
                }

	}
        @FXML
	private void handleOnCategoryChange() {
            System.out.println("**In handleOnCategoryChange");
		if (x_CCE_CATEGORY.getValue() != null 
                        && !x_CCE_CATEGORY.getValue().getLabel().equals("")) {
                    x_CCE_STATUS.setItems(cceService.getDropdownList("StatusList",null));
                }

	}
        
        
	@FXML
	private void handleSubmitCCE() throws SQLException {
		if (isValidate(actionBtnString)) {
                        cceBean.setX_CCE_CREATED_BY(userBean.getX_USER_ID());
			cceBean.setX_CCE_UPDATED_BY(userBean.getX_USER_ID());


                        if (x_CCE_STATE != null
					&& !x_CCE_STATE.getValue().getLabel().equals("State")) {
				cceBean.setX_CCE_STATE_ID(x_CCE_MODEL.getValue().getValue());
				cceBean.setX_CCE_STATE(x_CCE_MODEL.getValue().getLabel());
//                                cceBean.setX_CCE_FACILITY_ID(x_CCE_STATE.getValue().getValue());
                                System.out.println("CCE State Label: "+cceBean.getX_CCE_STATE());
			}
                        if (x_CCE_LGA != null
					&& !x_CCE_MODEL.getValue().getLabel().equals("LGA")) {
				cceBean.setX_CCE_LGA_ID(x_CCE_LGA.getValue().getValue());
				cceBean.setX_CCE_LGA(x_CCE_LGA.getValue().getLabel());
				cceBean.setX_CCE_FACILITY_ID(x_CCE_LGA.getValue().getValue());
                                System.out.println("CCE LGA Label: "+cceBean.getX_CCE_LGA());
			}
//                        if (x_CCE_WARD != null
//					&& !x_CCE_MODEL.getValue().getLabel().equals("Ward")) {
//				cceBean.setX_CCE_WARD_ID(x_CCE_WARD.getValue().getValue());
//				cceBean.setX_CCE_WARD(x_CCE_WARD.getValue().getLabel());
//                                System.out.println("CCE Ward Label: "+cceBean.getX_CCE_WARD());
//			}
                        if (x_CCE_FACILITY_NAME.getValue() != null
					&& !x_CCE_FACILITY_NAME.getValue().getLabel().equals("Site Name")) {
				cceBean.setX_CCE_FACILITY_ID(x_CCE_FACILITY_NAME.getValue().getValue());
//				cceBean.setX_CCE_FACILITY_NAME(x_CCE_FACILITY_NAME.getValue().getLabel());
                                System.out.println("CCE Facility Label: "+cceBean.getX_CCE_FACILITY_ID());
			}
                        if (x_CCE_LOCATION != null
					&& !x_CCE_LOCATION.getValue().equals("Equipment Location")) {
				cceBean.setX_CCE_LOCATION(x_CCE_LOCATION.getValue().getLabel());
                                System.out.println("CCE Location Label: "+cceBean.getX_CCE_LOCATION());
			}
//                        if (x_CCE_DESIGNATION != null
//					&& !x_CCE_DESIGNATION.getValue().getLabel().equals("Designation")) {
//				cceBean.setX_CCE_DESIGNATION(x_CCE_DESIGNATION.getValue().getLabel());
//                                System.out.println("CCE Desiignation Label: "+cceBean.getX_CCE_DESIGNATION());
//			}
                        if (x_CCE_CATEGORY != null
					&& !x_CCE_CATEGORY.getValue().getLabel().equals("Category")) {
				cceBean.setX_CCE_TYPE(x_CCE_CATEGORY.getValue().getLabel());
                                System.out.println("CCE Location Label: "+cceBean.getX_CCE_TYPE());
			}
                        if (x_CCE_MAKE != null
					&& !x_CCE_MAKE.getValue().getLabel().equals("Company")) {
				cceBean.setX_CCE_MAKE(x_CCE_MAKE.getValue().getLabel());
                                System.out.println("CCE Make Label: "+cceBean.getX_CCE_MAKE());
			}
                        if (x_CCE_MODEL != null
					&& !x_CCE_MODEL.getValue().getLabel().equals("Model")) {
				cceBean.setX_CCE_MODEL(x_CCE_MODEL.getValue().getLabel());
                                System.out.println("CCE Model Label: "+cceBean.getX_CCE_MODEL());
			}
                        if (x_CCE_STATUS != null
					&& !x_CCE_STATUS.getValue().getLabel().equals("Status")) {
				cceBean.setX_CCE_STATUS(x_CCE_STATUS.getValue().getLabel());
                                System.out.println("CCE Status Label: "+cceBean.getX_CCE_STATUS());
			}
                        if (x_CCE_SERIAL != null
					&& !x_CCE_SERIAL.getText().equals("")) {
				cceBean.setX_CCE_SERIAL_NO(x_CCE_SERIAL.getText());
                                System.out.println("CCE Serial Label: "+cceBean.getX_CCE_SERIAL_NO());
			}
                        if (x_CCE_DECISION != null
					&& !x_CCE_DECISION.getValue().getLabel().equals("Status")) {
				cceBean.setX_CCE_DECISION(x_CCE_DECISION.getValue().getLabel());
                                System.out.println("CCE Decision Label: "+cceBean.getX_CCE_DECISION());
			}
//                        System.out.println("Oun ree eee oooo "+x_CCE_SRC.getValue());
                        if (x_CCE_SRC != null && !String.valueOf(x_CCE_SRC.getValue()).equals("Source of CCE")) {
				cceBean.setX_CCE_SOURCE(String.valueOf(x_CCE_SRC.getValue()));
                                System.out.println("CCE Source Label: "+cceBean.getX_CCE_SOURCE());
			}
                        if (x_CCE_ACQUISITION1.getValue() != null
                                        && !x_CCE_ACQUISITION1.getValue().toString().equals("")) {
				cceBean.setX_CCE_YEAR_OF_ACQUISITION(x_CCE_ACQUISITION1.getValue().toString());
			} else {
				cceBean.setX_CCE_YEAR_OF_ACQUISITION(null);
			}
                        if (x_CCE_ACQUISITION2.getValue() != null
                                        && !x_CCE_ACQUISITION2.getValue().toString().equals("")) {
				cceBean.setX_CCE_MONTH_OF_ACQUISITION(x_CCE_ACQUISITION2.getValue().getValue());
			} else {
				cceBean.setX_CCE_MONTH_OF_ACQUISITION(null);
			}
                        if (x_CCE_NF_DATE.getValue() != null
                                        && !x_CCE_NF_DATE.getValue().toString().equals("")) {
				cceBean.setX_CCE_DATE_NF(x_CCE_NF_DATE.getValue().toString());
			} else {
				cceBean.setX_CCE_DATE_NF(null);
			}
                        
			if (cceService == null)
				cceService = new CCEService();
			if (actionBtnString.equals("search")) {
				cceMain.refreshCCETable(cceService
						.getSearchList(cceBean));
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
				cceService.saveCCE(cceBean, actionBtnString);
				cceMain.refreshCCETable();
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
//                            System.out.println("Here is the Ward "+x_CCE_WARD.getValue());
			
			if (x_CCE_STATE.getValue() == null
                                || x_CCE_STATE.getValue().toString().length() == 0) {
				errorMessage += "No State selected!\n";
			}
//			if (x_CCE_LGA.getValue() == null
//                                || x_CCE_LGA.getValue().toString().length() == 0) {
//				errorMessage += "No LGA selected!\n";
//			}
//			if (x_CCE_WARD.getValue() == null
//                                || x_CCE_WARD.getValue().toString().length() == 0) {
                            System.out.println("Here is the Ward 2 "+x_CCE_WARD.getValue().toString()+" "+x_CCE_LOCATION.getValue().getLabel());
//				errorMessage += "No Ward selected!\n";
//			}
//			if (x_CCE_LOCATION.getValue()==null
//                                || x_CCE_LOCATION.getValue().toString().length() == 0) {
//				errorMessage += "No Location Type selected!\n";
//			}
                        
//			if (x_CCE_FACILITY_NAME.getValue()==null
//                                || x_CCE_FACILITY_NAME.getValue().toString().length() == 0) {
//				errorMessage += "No Facility selected!\n";
//			}
//			if (x_CCE_DESIGNATION.getValue()==null
//                                || x_CCE_DESIGNATION.getValue().toString().length() == 0) {
//				errorMessage += "No CCE Designation selected!\n";
//			}
                        
                        if ((x_CCE_FACILITY_NAME.getValue().toString() != null
					&& x_CCE_LOCATION.getValue().getLabel().equals("LGA"))
                                        || (x_CCE_WARD.getValue().toString()==null
                                            && x_CCE_LOCATION.getValue().getLabel().equals("HF")) ){
				errorMessage += "Select the right Ward or Facility\n";
                        }
                        if (x_CCE_CATEGORY.getValue()==null
                                || x_CCE_CATEGORY.getValue().toString().length() == 0) {
				errorMessage += "No CCE Category selected!\n";
			}
                        if (x_CCE_MAKE.getValue()==null
                                || x_CCE_MAKE.getValue().toString().length() == 0) {
				errorMessage += "No CCE Company/Make selected!\n";
			}
                        if (x_CCE_MODEL.getValue()==null
                                || x_CCE_MODEL.getValue().toString().length() == 0) {
				errorMessage += "No CCE Model selected!\n";
			}
                        if (x_CCE_SERIAL.getText()==null
                                || x_CCE_SERIAL.getText().length() == 0) {
				errorMessage += "No CCE Serial Number specified!\n";
			}
                        if (x_CCE_STATUS.getValue()==null
                                || x_CCE_STATUS.getValue().toString().length() == 0) {
				errorMessage += "No CCE Status selected!\n";
			}
                        else{
                            if (x_CCE_STATUS.getValue().toString().equalsIgnoreCase("Not Functional")
                                    && (x_CCE_NF_DATE.getValue() == null 
                                || x_CCE_NF_DATE.getValue().toString().length() == 0)) {
                                errorMessage += "No Valid Non Functional date selected!\n";
                            }
			}
                        if (x_CCE_DECISION.getValue()==null
                                || x_CCE_DECISION.getValue().toString().length() == 0) {
				errorMessage += "No CCE Decision selected!\n";
			}
                        if (x_CCE_ACQUISITION1.getValue()==null
                                || x_CCE_ACQUISITION1.getValue().length() == 0) {
                            if(!x_CCE_ACQUISITION1.isDisabled()){
                                errorMessage += "No Year of Acquisition selected!\n";
                            }
				
			}
                        else{
                            int countYearsInstalled = CalendarUtil.getCurrentYear()-Integer.parseInt(x_CCE_ACQUISITION1.getValue());
                            
                            System.out.println("Age of CCE : "+countYearsInstalled+" ,Decision is : "+x_CCE_DECISION.getValue());
                            if(countYearsInstalled>10 && !x_CCE_DECISION.getValue().equals("Obsolete")){
                                errorMessage += "Decision and Year of Installation does not agree!\n";
                            }
                        }
                        if (x_CCE_ACQUISITION2.getValue()==null
                                || x_CCE_ACQUISITION2.getValue().toString().length() == 0) {
                            if(!x_CCE_ACQUISITION2.isDisabled()){
                                errorMessage += "No Month of Acquisition selected!\n";
                            }
				
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

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
                    x_CCE_STATE.setDisable(true);
                    x_CCE_LGA.setDisable(true);
//                    x_CCE_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "MOH": 
//                    x_CCE_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "SIO": // SIO
                    x_CCE_STATE.setDisable(true);
//                    x_CCE_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SCCO": // SCCO
                    x_CCE_STATE.setDisable(true);
//                    x_CCE_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SIFP": // SIFP
                    x_CCE_STATE.setDisable(true);
//                    x_CCE_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "CCO": // CCO - EMPLOYEE
                    x_CCE_STATE.setDisable(true);
                    x_CCE_LGA.setDisable(true);
//                    x_CCE_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		}
	}
}
