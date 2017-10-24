package com.chai.inv;

import java.sql.SQLException;
import java.time.LocalDate;
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

import com.chai.inv.model.GeneratorBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CommonService;
import com.chai.inv.service.GeneratorService;
import com.chai.inv.service.FacilityService;
import com.chai.inv.util.CalendarUtil;
import com.chai.inv.util.SelectKeyComboBoxListener;

public class GeneratorEditDialogController {
        
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_MANUFACTURER;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_POWER;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_STATE;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_WARD;
        @FXML
        private CheckBox x_GENERATOR_REPAIRS;
        @FXML
        private CheckBox x_GENERATOR_PPM;
        @FXML
        private Label x_GENERATOR_NF_DURATION;
        @FXML
        private DatePicker x_GENERATOR_NF_DATE;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_FUEL;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_STATUS;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_LGA;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_MODEL;
        @FXML
        private ComboBox<String> x_GENERATOR_LOCATION;
        @FXML
        private ComboBox<LabelValueBean> x_GENERATOR_FACILITY_NAME;
        @FXML
        private Label x_GENERATOR_DATE_NF_F;
        @FXML
        private CheckBox x_LOCATION_HAS_ELECTRICITY;
        @FXML
        private CheckBox x_FUEL_AVAILABLE;
        @FXML
        private ComboBox<String> x_GENERATOR_AGE;
        @FXML
        private ComboBox<String> x_HOURS_ELECTRICITY;
        @FXML
        private Button x_CANCEL_BTN;
        @FXML
        private Button x_OK_BTN;
        
	private boolean okClicked = false;
	private String actionBtnString;
	private boolean state_generator_record = false;
	private GeneratorBean generatorBean;
	private RootLayoutController rootLayoutController;
	private Stage dialogStage;
	private GeneratorService generatorService;
	private GeneratorMainController generatorMain;
	private UserBean userBean;
	private LabelValueBean role;
        private int nfDuration = 0;
        private String status;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public GeneratorMainController getGeneratorMain() {
		return generatorMain;
	}

	public void setGeneratorMain(GeneratorMainController generatorMain) {
		this.generatorMain = generatorMain;
	}

	public void setGeneratorService(GeneratorService generatorService,
			String actionBtnString) {
		this.generatorService = generatorService;
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

	public void setGeneratorBeanFields(GeneratorBean generatorBean,
			LabelValueBean statelabelValueBean,
			LabelValueBean lgalabelValueBean,
			LabelValueBean wardlabelValueBean) {
		this.generatorBean = new GeneratorBean();
		this.generatorBean = generatorBean;

		System.out.println("Warehouse ID: " + userBean.getX_USER_WAREHOUSE_ID());
                
                
                if (actionBtnString.equals("add")) {
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                                x_GENERATOR_STATE.getItems().addAll(new LabelValueBean("", null));
				x_GENERATOR_STATE.setItems(generatorService.getDropdownList("defaultstorelist", null));
				x_GENERATOR_STATE.setPromptText("State");
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				x_GENERATOR_STATE.setValue(new LabelValueBean(userBean.getX_USER_WAREHOUSE_NAME(), userBean.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_STATE.setDisable(true);
                                x_GENERATOR_LGA.getItems().addAll(new LabelValueBean("", null));
				x_GENERATOR_LGA.setItems(generatorService.getDropdownList("defaultstorelist",
								userBean.getX_USER_WAREHOUSE_ID()));
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_GENERATOR_STATE.setValue(new LabelValueBean(userBean
						.getX_USER_WAREHOUSE_NAME(), userBean
						.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_STATE.setDisable(true);
                                x_GENERATOR_LGA.getItems().addAll(new LabelValueBean("", null));
				x_GENERATOR_LGA.setItems(generatorService
						.getDropdownList("defaultstorelist","ALL_LGA_UNDER_STATE"));
				x_GENERATOR_LGA
						.setPromptText("LGA");
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list

                                x_GENERATOR_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_STATE.setDisable(true);
				x_GENERATOR_LGA.setItems(generatorService.getDropdownList("defaultstorelist"));
				x_GENERATOR_LGA.setValue(x_GENERATOR_LGA.getItems().get(0));
				x_GENERATOR_LGA.setDisable(true);
                                System.out.println("To search for ward, LGA value is "+x_GENERATOR_LGA.getItems().get(0).getValue());
                                //x_GENERATOR_WARD.getItems().addAll(new LabelValueBean("", null));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						x_GENERATOR_LGA.getItems().get(0).getValue()));
                                //x_GENERATOR_LOCATION.setItems();
//				x_GENERATOR_LOCATION.setValue(new LabelValueBean(
//                                                generatorService.getDropdownList("EquipmentLocationList",
//                                                userBean.getX_USER_WAREHOUSE_ID()).get(0).getLabel(),null));
                             
			}else if (MainApp.getUserRole().getLabel().equals("LIO")) {
				// get only the selected LGA name in the list

                                x_GENERATOR_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_STATE.setDisable(true);
				x_GENERATOR_LGA.setItems(generatorService.getDropdownList("defaultstorelist"));
				x_GENERATOR_LGA.setValue(new LabelValueBean(userBean
						.getX_USER_WAREHOUSE_NAME(), userBean
						.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_LGA.setDisable(true);
                                System.out.println("To search for ward, LGA value is "+x_GENERATOR_LGA.getItems().get(0).getValue());
                                x_GENERATOR_WARD.getItems().addAll(new LabelValueBean("", null));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						x_GENERATOR_LGA.getItems().get(0).getValue()));
                             
			}
                        
                                //x_GENERATOR_AGE.getItems().addAll("2017","2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006");
                                x_GENERATOR_AGE.setItems(CalendarUtil.get20Years());
                                x_GENERATOR_MANUFACTURER.setItems(generatorService.getDropdownList("ManufacturerList"));
                                //x_HOURS_ELECTRICITY.setText(generatorBean.getX_GENERATOR_ELECTRICITY_HRS());
                                x_GENERATOR_POWER.setItems(generatorService.getDropdownList("PowerList"));
                                x_GENERATOR_MODEL.setItems(generatorService.getDropdownList("ModelList",null));
                                x_GENERATOR_FUEL.setItems(generatorService.getDropdownList("FuelTypeList",null));
                                handleLocationHasElectricity();
                               // x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",null));

		}else if (actionBtnString.equals("edit")) {
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_GENERATOR_STATE.setValue(new LabelValueBean(generatorBean
						.getX_GENERATOR_STATE(), generatorBean
						.getX_GENERATOR_STATE_ID()));
				x_GENERATOR_STATE.setDisable(true);
				x_GENERATOR_LGA.setDisable(false);
				x_GENERATOR_LGA.setItems(generatorService
						.getDropdownList("defaultstorelist", null));
				x_GENERATOR_LGA.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_LGA(), generatorBean
								.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_WARD(), generatorBean
								.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean(
                                                generatorBean.getX_GENERATOR_FACILITY_NAME(),generatorBean
                                                                .getX_GENERATOR_FACILITY_ID()));
//                                x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
//                                                generatorBean.getX_GENERATOR_FACILITY_ID()));
				x_GENERATOR_LOCATION.setValue(generatorBean.getX_GENERATOR_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_GENERATOR_STATE.setValue(new FacilityService()
						.getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_GENERATOR_STATE.setDisable(true);
				x_GENERATOR_LGA.setDisable(true);
				x_GENERATOR_LGA.setItems(generatorService
						.getDropdownList("defaultstorelist", null));
				x_GENERATOR_LGA.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_LGA(), generatorBean
								.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_WARD(), generatorBean
								.getX_GENERATOR_WARD_ID()));
				new SelectKeyComboBoxListener(x_GENERATOR_WARD);
				x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FACILITY_NAME(),
						generatorBean.getX_GENERATOR_FACILITY_ID()));
//                                x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
//                                                generatorBean.getX_GENERATOR_FACILITY_ID()));
				x_GENERATOR_LOCATION.setValue(generatorBean.getX_GENERATOR_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list
				x_GENERATOR_STATE.setValue(new FacilityService()
						.getStateStoreId(generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_STATE.setDisable(true);
				x_GENERATOR_LGA.setDisable(true);
				x_GENERATOR_LGA.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_LGA(), generatorBean
								.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_WARD(),
						generatorBean.getX_GENERATOR_WARD_ID()));
				new SelectKeyComboBoxListener(x_GENERATOR_WARD);
				x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FACILITY_NAME(),
						generatorBean.getX_GENERATOR_FACILITY_ID()));
//                                x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
//                                                generatorBean.getX_GENERATOR_FACILITY_ID()));
				x_GENERATOR_LOCATION.setValue(generatorBean.getX_GENERATOR_LOCATION());
			}

			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_GENERATOR_STATE.setItems(generatorService.getDropdownList(
						"defaultstorelist", null));
				new SelectKeyComboBoxListener(x_GENERATOR_STATE);
				x_GENERATOR_STATE.setValue((new LabelValueBean(generatorBean
						.getX_GENERATOR_STATE(), generatorBean
						.getX_GENERATOR_STATE_ID())));
				x_GENERATOR_LGA.setItems(generatorService
						.getDropdownList("defaultstorelist", x_GENERATOR_STATE
								.getValue().getValue()));
				x_GENERATOR_LGA.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_LGA(), generatorBean
								.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_WARD(),
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FACILITY_NAME(),
						generatorBean.getX_GENERATOR_FACILITY_ID()));
//                                x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
//                                                generatorBean.getX_GENERATOR_FACILITY_ID()));
				x_GENERATOR_LOCATION.setValue(generatorBean.getX_GENERATOR_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_GENERATOR_STATE.setDisable(true);
				// x_GENERATOR_STATE.setItems(generatorService.getDropdownList("defaultstorelist",null));
				x_GENERATOR_STATE.setValue(new LabelValueBean(generatorBean
						.getX_GENERATOR_STATE(), generatorBean
						.getX_GENERATOR_STATE_ID()));
				x_GENERATOR_LGA.setItems(generatorService
						.getDropdownList("defaultstorelist", x_GENERATOR_STATE
								.getValue().getValue()));
				x_GENERATOR_LGA.setValue(new LabelValueBean(
						generatorBean.getX_GENERATOR_LGA(), generatorBean
								.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
						generatorBean.getX_GENERATOR_LGA_ID()));
				x_GENERATOR_WARD.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_WARD(),
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
						generatorBean.getX_GENERATOR_WARD_ID()));
				x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FACILITY_NAME(),
						generatorBean.getX_GENERATOR_FACILITY_ID()));
//                                x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
//                                                generatorBean.getX_GENERATOR_FACILITY_ID()));
				x_GENERATOR_LOCATION.setValue(generatorBean.getX_GENERATOR_LOCATION());
			}
                        
                        if (!(generatorBean.getX_GENERATOR_MANUFACTURER() == null)) {
                        x_GENERATOR_MANUFACTURER.setItems(generatorService.getDropdownList("ManufacturerList"));
			x_GENERATOR_MANUFACTURER.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_MANUFACTURER(),null));
                        }
                        if (!(generatorBean.getX_GENERATOR_POWER() == null)) {
                                x_GENERATOR_POWER.setItems(generatorService.getDropdownList("PowerList"));
                                x_GENERATOR_POWER.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_POWER(),null));
                        }
                        if (!(generatorBean.getX_GENERATOR_MODEL() == null)) {
                                x_GENERATOR_MODEL.setItems(generatorService.getDropdownList("ModelList",null));
                                x_GENERATOR_MODEL.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_MODEL(),null));
                        }
                        if (!(generatorBean.getX_GENERATOR_FUEL_TYPE() == null)) {
                                x_GENERATOR_FUEL.setItems(generatorService.getDropdownList("FuelTypeList",null));
                                x_GENERATOR_FUEL.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FUEL_TYPE(),null));
                        }

                        if (!(generatorBean.getX_GENERATOR_AGE() == null)) {
                            System.out.println("The generator Age is "+generatorBean.getX_GENERATOR_AGE());
                            //x_GENERATOR_AGE.getItems().addAll("2017","2016","2015","2014","2013","2012","2011","2010","2009","2008","2007","2006");
                            x_GENERATOR_AGE.setItems(CalendarUtil.get20Years());
                            x_GENERATOR_AGE.setValue(generatorBean.getX_GENERATOR_AGE());
        //                    x_GENERATOR_AGE.setValue(LocalDate.parse(generatorBean.getX_GENERATOR_AGE(),
                        }

                        if (!(generatorBean.getX_GENERATOR_FUNCTIONAL() == null)){
                            System.out.println("The generator Status is "+generatorBean.getX_GENERATOR_FUNCTIONAL());
                            x_GENERATOR_STATUS.setItems(generatorService.getDropdownList("StatusList",null));
                            x_GENERATOR_STATUS.setValue(new LabelValueBean(generatorBean.getX_GENERATOR_FUNCTIONAL(),null));
			}


                        if (generatorBean.getX_GENERATOR_LOCATION_HAS_ELECTRICITY() != null){
                                if (generatorBean.getX_GENERATOR_LOCATION_HAS_ELECTRICITY().equals("Yes")){
                                    x_LOCATION_HAS_ELECTRICITY.setSelected(true);
                                    handleLocationHasElectricity();
                                }
                                else{
                                    x_LOCATION_HAS_ELECTRICITY.setSelected(false);
                                    handleLocationHasElectricity();
                                }

                        }
                        
                        if (!(generatorBean.getX_GENERATOR_ELECTRICITY_HRS() == null)) {
                            handleLocationHasElectricity();
                        }
                
		}
		
		
	}


	@FXML
	private void handleLocationHasElectricity() {
            if (!x_LOCATION_HAS_ELECTRICITY.isSelected()) {
                x_HOURS_ELECTRICITY.setDisable(true);
                x_HOURS_ELECTRICITY.setValue("0");
            }
            else{
                x_HOURS_ELECTRICITY.setDisable(false);
                x_HOURS_ELECTRICITY.setValue(generatorBean.getX_GENERATOR_ELECTRICITY_HRS());
                x_HOURS_ELECTRICITY.getItems().addAll("1","2","3","4","5","6","7","8","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24");
            }
	}
        
        @FXML
	private void handleFunctionalStatus() {
            if (x_GENERATOR_STATUS.getValue().getLabel().equals("NF")) {
                        x_GENERATOR_DATE_NF_F.setText("Date Not Functional");
                        x_GENERATOR_NF_DATE.setPromptText("Date Not Functional");
//                        x_GENERATOR_NF_DATE.setDisable(false);
                        x_GENERATOR_NF_DATE.setValue(LocalDate.now());
            }
            else{
                x_GENERATOR_DATE_NF_F.setText("Date Functional");
                x_GENERATOR_NF_DATE.setPromptText("Date Functional");
//                x_GENERATOR_NF_DATE.setDisable(true);
                x_GENERATOR_NF_DATE.setValue(LocalDate.now());
            }
	}
        
        @FXML
        private void handleOnStateStoreChange() {
//                x_GENERATOR_LGA.setPromptText("LGA");                                                                                                        // initial
//                x_GENERATOR_WARD.setPromptText("Ward");                                                                                                        // initial
//                x_GENERATOR_FACILITY_NAME.setPromptText("Facility Name");                                                                                                        // initial
//                x_GENERATOR_LOCATION.setPromptText("Equipment Location");
                if (x_GENERATOR_STATE.getValue() != null 
                        && !x_GENERATOR_STATE.getValue().getLabel().equalsIgnoreCase("State")) {
//			x_GENERATOR_LGA.getItems().addAll(
//					new LabelValueBean("LGA", null));
                        x_GENERATOR_LGA.setValue(new LabelValueBean("", null));
                        x_GENERATOR_WARD.setValue(new LabelValueBean("", null));
                        x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean("", null));
                        x_GENERATOR_LGA.setItems(generatorService.getDropdownList(
                                        "defaultstorelist", x_GENERATOR_STATE.getValue().getValue()));
                        new SelectKeyComboBoxListener(x_GENERATOR_LGA);
			x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
					x_GENERATOR_STATE.getValue().getValue()));
			x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
					x_GENERATOR_WARD.getValue().getValue()));
//			x_GENERATOR_LOCATION.setValue(generatorService.getDropdownList("EquipmentLocationList",
//					x_GENERATOR_STATE.getValue().getValue()).get(0));
                }
        }
        @FXML
	private void handleOnLgaStoreChange() {
//                x_GENERATOR_WARD.setPromptText("Ward");                                                                                                    // initial
//                x_GENERATOR_LOCATION.setPromptText("Equipment Location");
//                x_GENERATOR_FACILITY_NAME.setPromptText("Facility Name");
		if (x_GENERATOR_LGA.getValue() != null
                        && !x_GENERATOR_LGA.getValue().getLabel().equalsIgnoreCase("LGA")) {
                        x_GENERATOR_WARD.setValue(new LabelValueBean("", null));
                        x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean("", null));
//			x_GENERATOR_WARD.getItems().addAll(
//					new LabelValueBean("Ward", null));
//			x_GENERATOR_FACILITY_NAME.getItems().addAll(
//					new LabelValueBean("Facility Name", null));
			x_GENERATOR_WARD.setItems(generatorService.getDropdownList("WardList",
					x_GENERATOR_LGA.getValue().getValue()));
			new SelectKeyComboBoxListener(x_GENERATOR_WARD);
			x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
					x_GENERATOR_LGA.getValue().getValue()));
//			x_GENERATOR_LOCATION.setValue(generatorService.getDropdownList("EquipmentLocationList",
//					x_GENERATOR_LGA.getValue().getValue()).get(0));
		}

	}
        @FXML
	private void handleOnWardStoreChange() {
		x_GENERATOR_FACILITY_NAME.setPromptText("Facility Name");
//                x_GENERATOR_LOCATION.setPromptText("Equipment Location");
		if (x_GENERATOR_WARD.getValue() != null
                        && !x_GENERATOR_WARD.getValue().getLabel().equalsIgnoreCase("Ward")) {
                        x_GENERATOR_LOCATION.setValue("");
//                        x_GENERATOR_FACILITY_NAME.setValue(new LabelValueBean("", null));
			x_GENERATOR_FACILITY_NAME.getItems().addAll(
					new LabelValueBean("Facility Name", null));
			x_GENERATOR_FACILITY_NAME.setItems(generatorService.getDropdownList("FacilityList",
					x_GENERATOR_WARD.getValue().getValue()));
                        System.out.println("Ward Value re ee o "+x_GENERATOR_WARD.getValue().getValue());
			new SelectKeyComboBoxListener(x_GENERATOR_FACILITY_NAME);
//			x_GENERATOR_LOCATION.setValue(generatorService.getDropdownList("EquipmentLocationList",
//					x_GENERATOR_WARD.getValue().getValue()).get(0));
		}

	}
//        @FXML
//	private void handleOnFacilityChange() {
//		x_GENERATOR_LOCATION.setPromptText("Equipment Location");
//		if (x_GENERATOR_FACILITY_NAME.getValue() != null
//                        && !x_GENERATOR_FACILITY_NAME.getValue().getLabel().equalsIgnoreCase("Facility Name")) {
////			x_GENERATOR_LOCATION.getItems().addAll(
////					new LabelValueBean("Equipment Location", null));
////			x_GENERATOR_LOCATION.setItems(generatorService.getDropdownList("EquipmentLocationList",
////					x_GENERATOR_FACILITY_NAME.getValue().getValue()));
////			x_GENERATOR_LOCATION.setValue(generatorService.getDropdownList("EquipmentLocationList",
////					x_GENERATOR_FACILITY_NAME.getValue().getValue()).get(0));
//		}
//
//	}
        @FXML
	private void handleOnNFDurationChange() {
                
		x_GENERATOR_NF_DATE.setPromptText("Date Not Functional");
		if (x_GENERATOR_NF_DATE.getValue() != null) {
			nfDuration = LocalDate.now().getMonthValue()
                                - x_GENERATOR_NF_DATE.getValue().getMonthValue();
                        System.out.println("Current month "+ LocalDate.now().getMonthValue()
                                +" and Past month "+x_GENERATOR_NF_DATE.getValue().getMonthValue());
                        if(nfDuration>=1)
                            x_GENERATOR_NF_DURATION.setText("Duration NF - "+nfDuration+" Month(s)");
                        else if(nfDuration>=0 && nfDuration<1){
                            x_GENERATOR_NF_DURATION.setText("Duration NF is <1 Month");
                        }
                        else{
                            x_GENERATOR_NF_DURATION.setText("Select earlier date!");
                        }
                            
		}

	}
        
	@FXML
	private void handleSubmitGenerator() throws SQLException {
		if (isValidate(actionBtnString)) {
                        generatorBean.setX_GENERATOR_CREATED_BY(userBean.getX_USER_ID());
			generatorBean.setX_GENERATOR_UPDATED_BY(userBean.getX_USER_ID());
//			if (x_GENERATOR_STATE != null
//					&& x_GENERATOR_STATE.getValue() != null
//					&& !x_GENERATOR_STATE.getValue().getLabel().equals("State")) {
//				generatorBean.setX_GENERATOR_STATE(x_GENERATOR_STATE.getValue().getLabel());
//				generatorBean.setX_GENERATOR_STATE_ID(x_GENERATOR_STATE.getValue().getValue());
//			}
//			if (x_GENERATOR_LGA != null
//					&& x_GENERATOR_LGA.getValue() != null
//					&& !x_GENERATOR_LGA.getValue().getLabel().equals("LGA")) {
//				generatorBean.setX_GENERATOR_LGA(x_GENERATOR_LGA.getValue().getLabel());
//				generatorBean.setX_GENERATOR_LGA_ID(x_GENERATOR_LGA.getValue().getValue());
//			}
//			if (x_GENERATOR_WARD != null 
//                                        && x_GENERATOR_WARD.getValue() != null
//					&& !x_GENERATOR_WARD.getValue().getLabel().equals("Ward")) {
//				generatorBean.setX_GENERATOR_WARD(x_GENERATOR_WARD.getValue().getLabel());
//				generatorBean.setX_GENERATOR_WARD_ID(x_GENERATOR_WARD.getValue().getValue());
////			}
//			if (x_GENERATOR_LOCATION != null
//					&& !x_GENERATOR_LOCATION.getValue().getLabel().equals("Equipment Location")) {
//				generatorBean.setX_GENERATOR_LOCATION(x_GENERATOR_LOCATION.getValue().getLabel());
//                                System.out.println("Generator Location Label: "+generatorBean.getX_GENERATOR_LOCATION());
//			}
			generatorBean.setX_GENERATOR_LOCATION_HAS_ELECTRICITY(x_LOCATION_HAS_ELECTRICITY.isSelected() ? "Yes" : "No");
                        
			if (x_GENERATOR_FACILITY_NAME != null
					&& !x_GENERATOR_FACILITY_NAME.getValue().getLabel().equals("Facility Name")) {
				generatorBean.setX_GENERATOR_FACILITY_NAME(x_GENERATOR_FACILITY_NAME.getValue().getLabel());
				generatorBean.setX_GENERATOR_FACILITY_ID(x_GENERATOR_FACILITY_NAME.getValue().getValue());
                                System.out.println("Generator Facility Name Label: "+generatorBean.getX_GENERATOR_FACILITY_NAME());
			}
                        
			if (x_GENERATOR_MANUFACTURER != null
					&& !String.valueOf(x_GENERATOR_MANUFACTURER.getValue()).equals("Manufacturer")) {
				generatorBean.setX_GENERATOR_MANUFACTURER(String.valueOf(x_GENERATOR_MANUFACTURER.getValue()));
                                System.out.println("Generator Manufacturer Label: "+generatorBean.getX_GENERATOR_MANUFACTURER());
			}
                        
			if (x_HOURS_ELECTRICITY != null
					&& !x_HOURS_ELECTRICITY.getValue().equals("Hours of Electricity")) {
				generatorBean.setX_GENERATOR_ELECTRICITY_HRS(x_HOURS_ELECTRICITY.getValue());
                                System.out.println("Generator Hrs Electricity Label: "+generatorBean.getX_GENERATOR_ELECTRICITY_HRS());
			}
                        else{
				generatorBean.setX_GENERATOR_ELECTRICITY_HRS("0");
                        }
                        
			if (x_GENERATOR_POWER != null
					&& !String.valueOf(x_GENERATOR_POWER.getValue()).equals("Power")) {
				generatorBean.setX_GENERATOR_POWER(String.valueOf(x_GENERATOR_POWER.getValue()));
                                System.out.println("Generator Power Label: "+generatorBean.getX_GENERATOR_POWER());
			}
                        if (x_GENERATOR_MODEL != null
					&& !String.valueOf(x_GENERATOR_MODEL.getValue()).equals("Model")) {
				generatorBean.setX_GENERATOR_MODEL(String.valueOf(x_GENERATOR_MODEL.getValue()));
                                System.out.println("Generator Model Label: "+generatorBean.getX_GENERATOR_MODEL());
			}
                        if (x_GENERATOR_AGE.getValue() != null) {
				generatorBean.setX_GENERATOR_AGE(x_GENERATOR_AGE.getValue().toString());
			} else {
				generatorBean.setX_GENERATOR_AGE(null);
			}
                        if (x_GENERATOR_STATUS.getValue() != null) {
				generatorBean.setX_GENERATOR_FUNCTIONAL(x_GENERATOR_STATUS.getValue().toString());
			} else {
				generatorBean.setX_GENERATOR_FUNCTIONAL(null);
			}
			generatorBean.setX_GENERATOR_FUEL_AVAILABLE(x_FUEL_AVAILABLE.isSelected() ? "Y": "N");
                        if (x_GENERATOR_NF_DATE.getValue() != null) {
				generatorBean.setX_GENERATOR_NF_DATE(x_GENERATOR_NF_DATE.getValue().toString());
			} else {
				generatorBean.setX_GENERATOR_NF_DATE(null);
			}
                        if (x_GENERATOR_FUEL != null
					&& !String.valueOf(x_GENERATOR_FUEL.getValue()).equals("Fuel Type")) {
				generatorBean.setX_GENERATOR_FUEL_TYPE(String.valueOf(x_GENERATOR_FUEL.getValue()));
                                System.out.println("Generator Fuel Label: "+generatorBean.getX_GENERATOR_FUEL_TYPE());
			}
			generatorBean.setX_GENERATOR_PLANNED_REPAIRS(x_GENERATOR_REPAIRS.isSelected() ? "Y": "N");
			generatorBean.setX_GENERATOR_PPM(x_GENERATOR_PPM.isSelected() ? "Y": "N");
                        generatorBean.setX_GENERATOR_DURATION_NF(Integer.toString(nfDuration));
			
			if (generatorService == null)
				generatorService = new GeneratorService();
			if (actionBtnString.equals("search")) {
				generatorMain.refreshGeneratorTable(generatorService
						.getSearchList(generatorBean));
				okClicked = true;
				dialogStage.close();
				// DatabaseOperation.getDbo().closeConnection();
				// DatabaseOperation.setDbo(null);
			} else {
				String masthead;
				String message;
				if (actionBtnString.equals("add")) {
					masthead = "Successfully Added!";
					message = "Generator is Saved to the Generator List";
				} else {
					masthead = "Successfully Updated!";
					message = "Generator is Updated to the Generator List";
				}
				generatorService.saveGenerator(generatorBean, actionBtnString);
				generatorMain.refreshGeneratorTable();
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
//			if (x_WAREHOUSE_CODE.getText() == null
//					|| x_WAREHOUSE_CODE.getText().length() == 0) {
//				errorMessage += "No valid Generator code!\n";
//			}
                            System.out.println("Here is the Ward "+x_GENERATOR_WARD.getValue());
			
			if (x_GENERATOR_STATE.getValue() == null
                                || x_GENERATOR_STATE.getValue().toString().length() == 0) {
				errorMessage += "No State selected!\n";
			}
			if (x_GENERATOR_LGA.getValue() == null
                                || x_GENERATOR_LGA.getValue().toString().length() == 0) {
				errorMessage += "No LGA selected!\n";
			}
			if (x_GENERATOR_WARD.getValue() == null
                                || x_GENERATOR_WARD.getValue().toString().length() == 0) {
                            //System.out.println("Here is the Ward 2 "+x_GENERATOR_WARD.getValue().toString());
				errorMessage += "No Ward selected!\n";
			}
			if (x_GENERATOR_LOCATION.getValue()==null
                                || x_GENERATOR_LOCATION.getValue().toString().length() == 0) {
				errorMessage += "No Location Type selected!\n";
			}
                        
			if (x_GENERATOR_FACILITY_NAME.getValue()==null
                                || x_GENERATOR_FACILITY_NAME.getValue().toString().length() == 0) {
				errorMessage += "No Facility selected!\n";
			}
                        
			if (x_GENERATOR_MANUFACTURER.getValue()==null
                                || String.valueOf(x_GENERATOR_MANUFACTURER.getValue()).length() == 0) {
				errorMessage += "No Manufacturer selected!\n";
			}
                        
                        if (x_LOCATION_HAS_ELECTRICITY.isSelected() && (x_HOURS_ELECTRICITY.getValue()== null
                                    || x_HOURS_ELECTRICITY.getValue().length() == 0)) {
                            errorMessage += " Location has electricity but Hours of electricity not specified selected!\n";
                        }
			
                        
			if (x_GENERATOR_POWER.getValue()==null
                                || String.valueOf(x_GENERATOR_POWER.getValue()).length() == 0) {
				errorMessage += "No Generator Power selected!\n";
			}
                        if (x_GENERATOR_MODEL.getValue()==null
                                || String.valueOf(x_GENERATOR_MODEL.getValue()).length() == 0) {
				errorMessage += "No Generator Model selected!\n";
			}
                        if (x_GENERATOR_AGE.getValue() == null 
                                || x_GENERATOR_AGE.getValue().toString().length() == 0) {
				errorMessage += "No Valid Age date selected!\n";
			}
                        if (x_GENERATOR_FUEL.getValue()==null
                                || String.valueOf(x_GENERATOR_FUEL.getValue()).length() == 0) {
				errorMessage += "No Fuel Type selected!\n";
			}
                        if (x_GENERATOR_STATUS.getValue()==null
                                || String.valueOf(x_GENERATOR_STATUS.getValue()).length() == 0) {
				errorMessage += "No Functionality Status selected!\n";
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
	private void handleCancelGenerator() {
		dialogStage.close();
		// DatabaseOperation.getDbo().closeConnection();
		// DatabaseOperation.setDbo(null);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
                    x_GENERATOR_STATE.setDisable(true);
                    x_GENERATOR_LGA.setDisable(true);
                    x_GENERATOR_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "MOH": 
                    x_GENERATOR_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "SIO": // SIO
                    x_GENERATOR_STATE.setDisable(true);
                    x_GENERATOR_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SCCO": // SCCO
                    x_GENERATOR_STATE.setDisable(true);
                    x_GENERATOR_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SIFP": // SIFP
                    x_GENERATOR_STATE.setDisable(true);
                    x_GENERATOR_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "CCO": // CCO - EMPLOYEE
                    x_GENERATOR_STATE.setDisable(true);
                    x_GENERATOR_LGA.setDisable(true);
                    x_GENERATOR_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		}
	}
}
