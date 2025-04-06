package com.pms.publicationmanagement.service.scraping.googlescholar.locators;

public class GoogleScholarProfilePageLocators {
    public static final String CITATIONS_CONTAINER_ID = "#gs_res_ccl_mid"; //se cheama la fel cu rezultatul de la search initial(pt dorel lucanu)
    public static final String START_URL = "https://scholar.google.com/?hl=en&as_sdt=0,5";
    public static final String SCHOLAR_START_INPUT = "#gs_hdr_tsi";
    public static final String SCHOLAR_START_SEARCH = "#gs_hdr_tsb";
    public static final String SCHOLAR_START_RESULT = "[class=\"gs_r\"]";
    public static final String PROFILE_SEARCH_RESULT = "#gsc_sa_ccl";
    public static final String PROFILE_PHOTO_ICON = "[class=\"gs_ai_pho\"]";
    public static final String PROFILE_PAGE_DOCUMENTS_BY_YEAR = "#gsc_a_ha";
    public static final String PROFILE_PAGE_DETAILS = "#gsc_prf";
    public static final String PROFILE_PAGE_NAME = "#gsc_prf_in";
    public static final String PROFILE_PAGE_ROLE = "[class=\"gsc_prf_il\"]";
    public static final String PROFILE_PAGE_INSTITUTION = "#gsc_prf_ivh";
}
