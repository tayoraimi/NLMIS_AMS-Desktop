package com.chai.inv.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.chai.inv.CustomChoiceDialog;
import com.chai.inv.MainApp;
import com.chai.inv.DAO.DatabaseOperation;
import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.CCEBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.util.CalendarUtil;

public class CCEService {
	DatabaseOperation dao;
	PreparedStatement pstmt;
	ResultSet rs;

	public ObservableList<LabelValueBean> getDropdownList(String... action) {
		String x_QUERY = null;
		switch (action[0]) {
		case "StateList":
			x_QUERY = "SELECT STATE_ID,"
					+ "		  STATE_NAME "
					+ "  FROM VIEW_STATES "
					+ " WHERE STATE_NAME IS NOT NULL "
					+ "   AND STATE_NAME <> '' AND STATUS='A' AND COUNTRY_ID = "
					+ action[1] + " ORDER BY STATE_NAME ";
			break;
		case "defaultstorelist":
			if (MainApp.getUserRole() != null
					&& CustomChoiceDialog.selectedLGA == null
					&& (MainApp.getUserRole().getLabel().equals("SCCO")
							|| MainApp.getUserRole().getLabel().equals("SIO") 
							|| MainApp.getUserRole().getLabel().equals("SIFP"))) {
				x_QUERY = "SELECT WAREHOUSE_ID, "
						+ "		  WAREHOUSE_NAME "
						+ "  FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " ORDER BY WAREHOUSE_NAME ASC";
			} else if (MainApp.getUserRole().getLabel().equals("NTO")) {
				if (action[1] == null) {
					x_QUERY = "SELECT WAREHOUSE_ID, "
							+ "		  WAREHOUSE_NAME "
							+ "  FROM VIEW_INVENTORY_WAREHOUSES "
							+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
							+ MainApp.getUSER_WAREHOUSE_ID()
							+ " ORDER BY WAREHOUSE_NAME ASC";
				} else {
					x_QUERY = "SELECT WAREHOUSE_ID, "
							+ "		  WAREHOUSE_NAME "
							+ "  FROM VIEW_INVENTORY_WAREHOUSES "
							+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
							+ action[1] + " ORDER BY WAREHOUSE_NAME ASC";
				}

			} else {
				System.out.println("***************s**************");
				x_QUERY = "SELECT WAREHOUSE_ID, " 
						+ "		  WAREHOUSE_NAME "
						+ "  FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE STATUS = 'A' AND WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			}
			break;
		case "WardList":
			x_QUERY = " SELECT TYPE_ID, "
					+ "        TYPE_CODE  "
					+ "   FROM TYPES  "
					+ "  WHERE SOURCE_TYPE='CUSTOMER TYPE' AND STATUS='A' AND WAREHOUSE_ID = "
					+ action[1] + "  ORDER BY TYPE_CODE ";
			break;
		case "FacilityList":
			if (action[1] != null) {
				x_QUERY = " SELECT CUSTOMER_ID,         CUSTOMER_NAME   "
						+" FROM VIEW_CUSTOMERS  "
						+" WHERE CUSTOMER_TYPE_ID = " +action[1]
						+" ORDER BY CUSTOMER_NAME ";
			} else {
				x_QUERY = " SELECT CUSTOMER_ID, "
						+ "        CUSTOMER_NAME  "
						+ "   FROM VIEW_CUSTOMERS  "
						+ "  WHERE DEFAULT_STORE_ID IN "
						+ " (SELECT WAREHOUSE_ID FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID() + ") "
						+ "  ORDER BY CUSTOMER_NAME ";
			}
			break;
//		case "EquipmentLocationList":
//			x_QUERY = " SELECT TYPE_ID, TYPE_NAME"
//					+ "   FROM TYPES WHERE TYPE_ID = ("
//                                        + " SELECT WAREHOUSE_TYPE_ID FROM VIEW_STORE_TYPES "
//                                        + " WHERE FACILITY_ID = "+action[1]+")";
//			break;
		case "EquipmentLocationList":
			x_QUERY = " SELECT WAREHOUSE_TYPE_ID, LOCATION"
					+ "   FROM VIEW_STORE_TYPES "
                                + "WHERE FACILITY_ID = "+action[1]
                                + " OR WAREHOUSE_TYPE_ID = "+action[1];
			break;
		case "DesignationList":
			x_QUERY = " SELECT CCE_ID, DESIGNATION "
					+ "   FROM CCE_LIST GROUP BY DESIGNATION ASC ";
			break;
		case "MakeList":
			x_QUERY =  " SELECT CCE_ID, COMPANY "
					+ "   FROM CCE_LIST GROUP BY COMPANY ASC ";
			break;
		case "ModelList":
			x_QUERY =  " SELECT CCE_ID, MODEL "
					+ "   FROM CCE_LIST WHERE COMPANY = '"
                                        + action[1]+"' GROUP BY MODEL ASC ";
			break;
		case "TypeList":
			x_QUERY =  " SELECT CCE_ID, TYPE "
					+ "   FROM CCE_LIST WHERE COMPANY = '"
                                        + action[1]+"' AND MODEL = '"
                                        + action[2]+"' GROUP BY TYPE ASC ";
			break;
		case "CategoryList":
			x_QUERY =  " SELECT CCE_ID, CATEGORY "
					+ "   FROM CCE_LIST WHERE COMPANY = '"
                                        + action[1]+"' AND MODEL = '"
                                        + action[2]+"' AND TYPE = '"
                                        + action[3]+"' GROUP BY CATEGORY ASC ";
                        break;
		case "DecisionList":
			x_QUERY =  " SELECT DECISION_ID, DECISION "
					+ "   FROM CCE_DECISION WHERE STATUS = '"
                                        + action[1]+"' GROUP BY DECISION ASC ";
			break;
		case "StatusList":
			x_QUERY =  " SELECT DECISION_ID, STATUS "
					+ "   FROM CCE_DECISION GROUP BY STATUS ASC ";
			break;
		case "SourceList":
			x_QUERY =  " SELECT CCE_DATA_ID, SOURCE_OF_CCE "
					+ "   FROM E003 GROUP BY SOURCE_OF_CCE ASC ";
			break;
		}
		try {
			return DatabaseOperation.getDropdownList(x_QUERY);
		} catch (SQLException | NullPointerException ex) {
			System.out.println("An error occured while getting CCE form "+ " drop down menu lists, error: "
		+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while getting CCE form "
					+ " drop down menu lists, error: \n"+
			MyLogger.getStackTrace(ex));
		}
		System.out.println("return null");
		return null;
	}

	public ObservableList<CCEBean> getCCEList() {
		String WHERE_CONDITION = "";
                String[] splittedMonthYear;
		ObservableList<CCEBean> cceData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEService.getCCEList() ");
				dao = DatabaseOperation.getDbo();
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}

		if (MainApp.getUserRole() != null) {
			if ((MainApp.getUserRole().getLabel().equals("SIO")
					|| MainApp.getUserRole().getLabel().equals("SCCO") || MainApp
					.getUserRole().getLabel().equals("SIFP"))
					&& CustomChoiceDialog.selectedLGA == null) {
				// IF STATE ADMINS DOES NOT SELECT LGA
				WHERE_CONDITION = " WHERE FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID IN ( SELECT FACILITY_ID FROM VIEW_E003 WHERE DEFAULT_ORDERING_WAREHOUSE_ID"
                                        + " IN (SELECT FACILITY_ID FROM VIEW_E001 WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()+" ) )";
                                
			} else if ((MainApp.getUserRole().getLabel().equals("SIO")
					|| MainApp.getUserRole().getLabel().equals("SCCO") || MainApp
					.getUserRole().getLabel().equals("SIFP"))
					&& CustomChoiceDialog.selectedLGA != null) {
				// IF STATE ADMINS SELECTS LGA
                                WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
//				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID IN (SELECT viw.FACILITY_ID FROM VIEW_E001 viw"
//						+ " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
//						+ MainApp.getUSER_WAREHOUSE_ID()
//						+ " OR DEFAULT_ORDERING_WAREHOUSE_ID = "
//						+ MainApp.getUSER_WAREHOUSE_ID();

			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
				WHERE_CONDITION = "";
			} else if (MainApp.getUserRole().getLabel().equals("CCO")||MainApp.getUserRole().getLabel().equals("LIO")) {
				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			} else {
				WHERE_CONDITION = " WHERE FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			}
		}
		pstmt = dao.getPreparedStatement("SELECT CCE_DATA_ID, "
                                + "STATE_ID, "
                                + "STATE, "
                                + "LGA_ID, "
                                + "LGA, "
                                + "FACILITY_ID, "
                                + "FACILITY_NAME, "
                                + "WARD, "
                                + "WAREHOUSE_TYPE_ID, "
                                + "LOCATION, "
                                + "DEFAULT_ORDERING_WAREHOUSE_ID, "
                                + "CCE_ID, "
                                + "DESIGNATION, "
                                + "MAKE, "
                                + "MODEL, "
                                + "DEVICE_SERIAL_NO, "
                                + "REFRIGERANT, "
                                + "VOL_NEG, "
                                + "VOL_POS, "
                                + "DATE_FORMAT(DATE_NF, '%d-%b-%Y') DATE_NF, "
                                + "CATEGORY, "
                                + "TYPE, "
                                + "STATUS, "
                                + "DECISION, "
                                + "ENERGY, "
                                //+ "DATE_FORMAT(YEAR_OF_ACQUISITION, '%M, %Y') YEAR_OF_ACQUISITION, "
                                + "YEAR_OF_ACQUISITION, "
                                + "SOURCE_OF_CCE FROM VIEW_E003 "+ WHERE_CONDITION);
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CCEBean cceBean = new CCEBean();
                                cceBean.setX_CCE_DATA_ID(rs.getString("CCE_DATA_ID"));
                                cceBean.setX_CCE_STATE_ID(rs.getString("STATE_ID"));
				cceBean.setX_CCE_STATE(rs.getString("STATE"));
                                cceBean.setX_CCE_LGA_ID(rs.getString("LGA_ID"));
				cceBean.setX_CCE_LGA(rs.getString("LGA"));
				cceBean.setX_CCE_WARD_ID(rs.getString("WAREHOUSE_TYPE_ID"));
				cceBean.setX_CCE_WARD(rs.getString("WARD"));
                                cceBean.setX_CCE_FACILITY_ID(rs.getString("FACILITY_ID"));
				cceBean.setX_CCE_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                cceBean.setX_CCE_LOCATION(rs.getString("LOCATION"));
				cceBean.setX_CCE_STATE(rs.getString("STATE"));
                                cceBean.setX_CCE_WARD(rs.getString("WARD"));
                                cceBean.setX_CCE_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                cceBean.setX_CCE_ID(rs.getString("CCE_ID"));
                                cceBean.setX_CCE_SERIAL_NO(rs.getString("DEVICE_SERIAL_NO"));
                                cceBean.setX_CCE_DATE_NF(rs.getString("DATE_NF"));
                                cceBean.setX_CCE_MODEL(rs.getString("MODEL"));
                                cceBean.setX_CCE_LOCATION(rs.getString("LOCATION"));
                                cceBean.setX_CCE_CATEGORY(rs.getString("CATEGORY"));
                                cceBean.setX_CCE_TYPE(rs.getString("TYPE"));
                                cceBean.setX_CCE_STATUS(rs.getString("STATUS"));
                                cceBean.setX_CCE_DECISION(rs.getString("DECISION"));
                                cceBean.setX_CCE_LOCATION(rs.getString("LOCATION"));
                                cceBean.setX_CCE_SUMMARY(rs.getString("CATEGORY")+"-"+rs.getString("STATUS")+"-"+rs.getString("DECISION"));
                                cceBean.setX_CCE_VOL_NEG(rs.getString("VOL_NEG"));
                                cceBean.setX_CCE_DESIGNATION(rs.getString("DESIGNATION"));
                                cceBean.setX_CCE_VOL_POS(rs.getString("VOL_POS"));
                                cceBean.setX_CCE_REFRIGERANT(rs.getString("REFRIGERANT"));
                                cceBean.setX_CCE_ENERGY(rs.getString("ENERGY"));
                                cceBean.setX_CCE_SOURCE(rs.getString("SOURCE_OF_CCE"));
                                cceBean.setX_CCE_MAKE(rs.getString("MAKE"));
                                splittedMonthYear = rs.getString("YEAR_OF_ACQUISITION").split("/");
                                cceBean.setX_CCE_MONTH_OF_ACQUISITION(splittedMonthYear[0]);
                                cceBean.setX_CCE_YEAR_OF_ACQUISITION(splittedMonthYear[1]);
                                cceBean.setX_CCE_MONTHYEAR_OF_ACQUISITION(rs.getString("YEAR_OF_ACQUISITION"));
				
				
				cceData.add(cceBean);
			}
		} catch (Exception ex) {
			System.out.println("An error occured while CCE list, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(ex));
		} finally {
			System.out.println("Get CCE list query : " + pstmt.toString());
		}
		return cceData;
	}

	public boolean saveCCE(CCEBean cceBean,String actionBtnString) throws SQLException {
		boolean flag = true;
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEService.getCCEList() ");
				dao = DatabaseOperation.getDbo();
			}
			if (actionBtnString.equals("add")) {
				pstmt = dao.getPreparedStatement("INSERT INTO E003"
                                                                + " (FACILITY_ID, " //1
                                                                + "DEVICE_SERIAL_NO, " //2
                                                                + "CCE_ID, " //3
                                                                + "DATE_NF, " //4
                                                                + "STATUS, " //5
                                                                + "DECISION, " //6
                                                                + "YEAR_OF_ACQUISITION, " //7
                                                                + "SOURCE_OF_CCE, " //8
                                                                + "LOCATION, " //9
                                                                + "UPDATED_BY, " //10
                                                                + "LAST_UPDATED_ON, "
                                                                + "CREATED_BY, " //11
                                                                + "CREATED_ON ) "
								+ " VALUES(?,?,(SELECT CCE_ID FROM CCE_LIST WHERE MODEL =?),?,?,?,?,?,?,?,NOW(),?,NOW())");
                                
				pstmt.setString(11, cceBean.getX_CCE_CREATED_BY());
			} else {
				pstmt = dao.getPreparedStatement("UPDATE E003 SET "
                                                                + "FACILITY_ID=?, "
                                                                + "DEVICE_SERIAL_NO=?, " //2
                                                                + "CCE_ID=(SELECT CCE_ID FROM CCE_LIST WHERE MODEL =?), " //3
                                                                + "DATE_NF=?, " //4
                                                                + "STATUS=?, " //5
                                                                + "DECISION=?, " //6
                                                                + "YEAR_OF_ACQUISITION=?, " //7
                                                                + "SOURCE_OF_CCE=?, " //8
                                                                + "LOCATION=?, " //9
                                                                + "UPDATED_BY=?, " //10
                                                                + "LAST_UPDATED_ON=NOW()" 
								+ " WHERE CCE_DATA_ID=?"); //11
                                
				pstmt.setString(11, cceBean.getX_CCE_DATA_ID());
			}
                        pstmt.setString(1, cceBean.getX_CCE_FACILITY_ID());
			pstmt.setString(2, cceBean.getX_CCE_SERIAL_NO());
			pstmt.setString(3, cceBean.getX_CCE_MODEL());
			pstmt.setString(4, cceBean.getX_CCE_DATE_NF());
			pstmt.setString(5, cceBean.getX_CCE_STATUS());
			pstmt.setString(6, cceBean.getX_CCE_DECISION());
			pstmt.setString(7, cceBean.getX_CCE_MONTH_OF_ACQUISITION()+"/"+cceBean.getX_CCE_YEAR_OF_ACQUISITION());
			pstmt.setString(8, cceBean.getX_CCE_SOURCE());
			pstmt.setString(9, cceBean.getX_CCE_LOCATION());
                        pstmt.setString(10,cceBean.getX_CCE_UPDATED_BY());
			pstmt.executeUpdate();
		} catch (SQLException | NullPointerException ex) {
			flag = false;
			System.out.println("An error occured while saving/editing CCE, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while saving/editing CCE, error: "+MyLogger.getStackTrace(ex));
			return flag;
		} finally {
			System.out.println("warehouse insert/update query : "+ pstmt.toString());
		}
		return flag;
	}

	public ObservableList<CCEBean> getSearchList(CCEBean toSearchCCEBean) {
		ObservableList<CCEBean> searchData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEService.getCCEList() ");
				dao = DatabaseOperation.getDbo();
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}
		pstmt = dao.getPreparedStatement("SELECT COMPANY_ID,"
						+ " WAREHOUSE_ID, "
						+ " WAREHOUSE_CODE, "
						+ " WAREHOUSE_NAME, "
						+ " WAREHOUSE_DESCRIPTION, "
						+ " WAREHOUSE_TYPE_NAME,"
						+ " WAREHOUSE_TYPE_ID,"
						+ " ADDRESS1,"
						// + " CITY_NAME, "
						+ " STATE_NAME,"
						+ " COUNTRY_NAME,"
						// + " ZIP_CODE,"
						+ " ADDRESS2,"
						+ " ADDRESS3,"
						+ " COUNTRY_ID,"
						+ " STATE_ID,"
						// + " CITY_ID,"
						+ " TELEPHONE_NUMBER, "
						+ " FAX_NUMBER,"
						+ " STATUS, "
						+ " DATE_FORMAT(START_DATE, '%d-%b-%Y') START_DATE, "
						+ " DATE_FORMAT(END_DATE, '%d-%b-%Y') END_DATE, "
						+ " DEFAULT_ORDERING_WAREHOUSE_ID, "
						+ " DEFAULT_ORDERING_WAREHOUSE_CODE "
						+ " FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE UPPER(WAREHOUSE_CODE) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_CODE)),'%') "
						+ "  AND UPPER(WAREHOUSE_NAME) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_NAME)),'%') "
						+ "  AND UPPER(WAREHOUSE_DESCRIPTION) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_DESCRIPTION)),'%') "
						+ "  AND WAREHOUSE_TYPE_NAME = IFNULL(?, WAREHOUSE_TYPE_NAME) "
						+ "  AND UPPER(IFNULL(ADDRESS1, 'ASDFGHJK1234567')) LIKE CONCAT('%',UPPER(IFNULL(?, IFNULL(ADDRESS1, 'ASDFGHJK1234567'))),'%') "
						+ "  AND STATE_NAME = IFNULL(?, STATE_NAME) "
						+ "  AND COUNTRY_NAME = IFNULL(?, COUNTRY_NAME) "
						+ "  AND IFNULL(TELEPHONE_NUMBER, 'ASDFGHJK1234567') = IFNULL(?, IFNULL(TELEPHONE_NUMBER, 'ASDFGHJK1234567')) "
						+ "  AND STATUS = IFNULL(?, STATUS) "
						+ "  AND IFNULL(DATE_FORMAT(START_DATE, '%Y-%m-%d'), 'AAAAA') = IFNULL(?, IFNULL(DATE_FORMAT(START_DATE, '%Y-%m-%d'), 'AAAAA')) "
						+ "  AND IFNULL(DATE_FORMAT(END_DATE, '%Y-%m-%d'), 'AAAAA') = IFNULL(?, IFNULL(DATE_FORMAT(END_DATE, '%Y-%m-%d'), 'AAAAA')) "
						+ "  AND UPPER(DEFAULT_ORDERING_WAREHOUSE_CODE) LIKE CONCAT('%',UPPER(IFNULL(?, DEFAULT_ORDERING_WAREHOUSE_CODE)),'%') ");
		
		return searchData;
	}
        
//        public String summarize(String type, String status, String decision){
//            String summary="";
//            switch (type) {
//		case "Compression Freezer":
//			summary = summary+"CF";
//                    break;
//                case "Absorption  refrigerator":
//			summary = summary+"AR";
//                    break;
//                case "Compression Refrigerator":
//			summary = summary+"CR";
//                    break;
//                case "Absorption freezer":
//			summary = summary+"AF";
//                    break;
//                case "Solar Equipment":
//			summary = summary+"SE";
//                    break;
//                case "Walk-In Freezer Room":
//			summary = summary+"WIFR";
//                    break;
//                case "Walk-In Cold Room":
//			summary = summary+"WICR";
//                    break;
//                case "Domestic Electrical":
//			summary = summary+"DCR";
//                    break;
//                case "Domestic Solar":
//			summary = summary+"DCRS";
//                    break;
//            }
//            if(!summary.equals("")){
//                summary = summary+"-";
//            }
//            switch (status) {
//		case "Functional":
//			summary = summary+"F";
//                    break;
//                case "Not Functional":
//			summary = summary+"NF";
//                    break;
//                case "Not Installed":
//			summary = summary+"NI";
//                    break;
//            }
//            
//            if(!summary.equals("")){
//                summary = summary+"-";
//            }
//            switch (decision) {
//		case "Good":
//			summary = summary+"G";
//                    break;
//                case "Obsolete":
//			summary = summary+"O";
//                    break;
//                case "Repairable":
//			summary = summary+"R";
//                    break;
//                case "Install":
//			summary = summary+"I";
//                    break;
//            }
//            
//            return summary;
//        }

}
