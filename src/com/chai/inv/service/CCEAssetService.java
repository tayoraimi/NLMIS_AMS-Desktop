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
import com.chai.inv.model.CCEAssetBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.util.CalendarUtil;

public class CCEAssetService {
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
				x_QUERY = " SELECT CUSTOMER_ID, " 
						+ "        CUSTOMER_NAME  "
						+ "   FROM VIEW_CUSTOMERS  "
						//+ "  WHERE DEFAULT_STORE_ID = "+ action[1] 
						+ "  WHERE CUSTOMER_TYPE_ID = "+ action[1] 
						+ "  ORDER BY CUSTOMER_NAME ";
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
		case "TypeList":
			x_QUERY =  " SELECT CCE_ID, CATEGORY "
					+ "   FROM CCE_LIST WHERE DESIGNATION = '"
                                        + action[1]+"' GROUP BY CATEGORY ASC ";
			break;
		case "DecisionList":
			x_QUERY =  " SELECT CCE_DATA_ID, DECISION "
					+ "   FROM E003 GROUP BY DECISION ASC ";
			break;
		case "StatusList":
			x_QUERY =  " SELECT CCE_DATA_ID, STATUS "
					+ "   FROM E003 GROUP BY STATUS ASC ";
			break;
		case "SourceList":
			x_QUERY =  " SELECT CCE_DATA_ID, SOURCE_OF_CCE "
					+ "   FROM E003 GROUP BY SOURCE_OF_CCE ASC ";
			break;
		case "MakeList":
			x_QUERY =  " SELECT CCE_ID, COMPANY "
					+ "   FROM CCE_LIST WHERE DESIGNATION = '"
                                        + action[1]+"' AND CATEGORY = '"
                                        + action[2]+"' GROUP BY COMPANY ASC ";
			break;
		case "ModelList":
			x_QUERY =  " SELECT CCE_ID, MODEL "
					+ "   FROM CCE_LIST WHERE DESIGNATION = '"
                                        + action[1]+"' AND CATEGORY = '"
                                        + action[2]+"' AND COMPANY = '"
                                        + action[3]+"' GROUP BY MODEL ASC ";
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

	public ObservableList<CCEAssetBean> getCCEAssetList() {
		String WHERE_CONDITION = "";
                String[] splittedMonthYear;
		ObservableList<CCEAssetBean> cceData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEAssetService.getCCEAssetList() ");
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
		pstmt = dao.getPreparedStatement("SELECT CCE_ID, "
                                + "MODEL, "
                                + "DESIGNATION, "
                                + "CATEGORY, "
                                + "COMPANY, "
                                + "REFRIGERANT, "
                                + "VOL_NEG, "
                                + "VOL_POS, "
                                + "EXPECTED_WORKING_LIFE, "
                                + "PRICE, "
                                + "TYPE, "
                                + "ENERGY_SOURCE FROM CCE_LIST");
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				CCEAssetBean cceAssetBean = new CCEAssetBean();
                                cceAssetBean.setX_CCE_ASSET_ID(rs.getString("CCE_ID"));
				cceAssetBean.setX_CCE_ASSET_MODEL(rs.getString("MODEL"));
                                cceAssetBean.setX_CCE_ASSET_DESIGNATION(rs.getString("DESIGNATION"));
				cceAssetBean.setX_CCE_ASSET_CATEGORY(rs.getString("CATEGORY"));
				cceAssetBean.setX_CCE_ASSET_COMPANY(rs.getString("COMPANY"));
				cceAssetBean.setX_CCE_ASSET_REFRIGERANT(rs.getString("REFRIGERANT"));
                                cceAssetBean.setX_CCE_ASSET_VOL_NEG(rs.getString("VOL_NEG"));
				cceAssetBean.setX_CCE_ASSET_VOL_POS(rs.getString("VOL_POS"));
                                cceAssetBean.setX_CCE_ASSET_EXPECTED_LIFE(rs.getString("EXPECTED_WORKING_LIFE"));
				cceAssetBean.setX_CCE_ASSET_PRICE(rs.getString("PRICE"));
                                cceAssetBean.setX_CCE_ASSET_TYPE(rs.getString("TYPE"));
                                cceAssetBean.setX_CCE_ASSET_ENERGY(rs.getString("ENERGY_SOURCE"));
				
				
				cceData.add(cceAssetBean);
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

	public boolean saveCCE(CCEAssetBean cceAssetBean,String actionBtnString) throws SQLException {
		boolean flag = true;
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEAssetService.getCCEAssetList() ");
				dao = DatabaseOperation.getDbo();
			}
			if (actionBtnString.equals("add")) {
				pstmt = dao.getPreparedStatement("INSERT INTO CCE_LIST"
                                                                + "(MODEL, "//1
                                                                + "DESIGNATION, "//2
                                                                + "CATEGORY, "//3
                                                                + "COMPANY, "//4
                                                                + "REFRIGERANT, " //5
                                                                + "VOL_NEG, " //6
                                                                + "VOL_POS, " //7
                                                                + "EXPECTED_WORKING_LIFE, " //8
                                                                + "PRICE, " //9
                                                                + "TYPE, " //10
                                                                + "ENERGY_SOURCE, " //11
                                                                + "UPDATED_BY, " //12
                                                                + "UPDATED_ON" 
                                                                + "CREATED_BY, " //13
                                                                + "CREATED_ON)" 
								+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,NOW())");
                                
				pstmt.setString(13, cceAssetBean.getX_CCE_ASSET_CREATED_BY());
			} else {
				pstmt = dao.getPreparedStatement("UPDATE CCE_LIST SET "
                                                                + "MODEL=?, "//1
                                                                + "DESIGNATION=?, "//2
                                                                + "CATEGORY=?, "//3
                                                                + "COMPANY=?, "//4
                                                                + "REFRIGERANT=?, " //5
                                                                + "VOL_NEG=?, " //6
                                                                + "VOL_POS=?, " //7
                                                                + "EXPECTED_WORKING_LIFE=?, " //8
                                                                + "PRICE=?, " //9
                                                                + "TYPE=?, " //10
                                                                + "ENERGY_SOURCE=?, " //11
                                                                + "UPDATED_BY=?, " //12
                                                                + "UPDATED_ON=NOW()"
								+ " WHERE CCE_ID=?"); 
                                
				pstmt.setString(13, cceAssetBean.getX_CCE_ASSET_ID());
			}
                        pstmt.setString(1, cceAssetBean.getX_CCE_ASSET_MODEL());
			pstmt.setString(2, cceAssetBean.getX_CCE_ASSET_DESIGNATION());
			pstmt.setString(3, cceAssetBean.getX_CCE_ASSET_CATEGORY());
			pstmt.setString(4, cceAssetBean.getX_CCE_ASSET_COMPANY());
			pstmt.setString(5, cceAssetBean.getX_CCE_ASSET_REFRIGERANT());
			pstmt.setString(6, cceAssetBean.getX_CCE_ASSET_VOL_NEG());
			pstmt.setString(7, cceAssetBean.getX_CCE_ASSET_VOL_POS());
			pstmt.setString(8, cceAssetBean.getX_CCE_ASSET_EXPECTED_LIFE());
                        pstmt.setString(9,cceAssetBean.getX_CCE_ASSET_PRICE());
			pstmt.setString(10, cceAssetBean.getX_CCE_ASSET_TYPE());
                        pstmt.setString(11,cceAssetBean.getX_CCE_ASSET_ENERGY());
                        pstmt.setString(12,cceAssetBean.getX_CCE_ASSET_UPDATED_BY());
			pstmt.executeUpdate();
		} catch (SQLException | NullPointerException ex) {
			flag = false;
			System.out.println("An error occured while saving/editing CCEAsset, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while saving/editing CCEAsset, error: "+MyLogger.getStackTrace(ex));
			return flag;
		} finally {
			System.out.println("warehouse insert/update query : "+ pstmt.toString());
		}
		return flag;
	}

	public ObservableList<CCEAssetBean> getSearchList(CCEAssetBean toSearchCCEAssetBean) {
		ObservableList<CCEAssetBean> searchData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In CCEAssetService.getCCEAssetList() ");
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
        
        public String summarize(String type, String status, String decision){
            String summary="";
            switch (type) {
		case "Compression Freezer":
			summary = summary+"CF";
                    break;
                case "Absorption  refrigerator":
			summary = summary+"AR";
                    break;
                case "Compression Refrigerator":
			summary = summary+"CR";
                    break;
                case "Absorption freezer":
			summary = summary+"AF";
                    break;
                case "Solar Equipment":
			summary = summary+"SE";
                    break;
                case "Walk-In Freezer Room":
			summary = summary+"WIFR";
                    break;
                case "Walk-In Cold Room":
			summary = summary+"WICR";
                    break;
                case "Domestic Electrical":
			summary = summary+"DCR";
                    break;
                case "Domestic Solar":
			summary = summary+"DCRS";
                    break;
            }
            if(!summary.equals("")){
                summary = summary+"-";
            }
            switch (status) {
		case "Functional":
			summary = summary+"F";
                    break;
                case "Not Functional":
			summary = summary+"NF";
                    break;
                case "Not Installed":
			summary = summary+"NI";
                    break;
            }
            
            if(!summary.equals("")){
                summary = summary+"-";
            }
            switch (decision) {
		case "Good":
			summary = summary+"G";
                    break;
                case "Obsolete":
			summary = summary+"O";
                    break;
                case "Repairable":
			summary = summary+"R";
                    break;
                case "Install":
			summary = summary+"I";
                    break;
            }
            
            return summary;
        }

}
