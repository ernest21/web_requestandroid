package com.test.webrequestdesarrollomovil;

import java.util.HashMap;
import java.util.Map;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class MovieOMDB {

    private List<Search> Search = new ArrayList<Search>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The Search
     */
    public List<Search> getSearch() {
        return Search;
    }

    /**
     *
     * @param Search
     * The Search
     */
    public void setSearch(List<Search> Search) {
        this.Search = Search;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}


