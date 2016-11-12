package com.imgpng.qp.news.bean;

import java.io.Serializable;

/**
 * Created by qp on 2016/11/12.
 */

public class XMLBean implements Serializable{
    public String xmlTitle;
    public String  xmlUrl;
    public XMLBean(String title,String url){
        this.xmlTitle = title;
        this.xmlUrl = url;
    }
}
