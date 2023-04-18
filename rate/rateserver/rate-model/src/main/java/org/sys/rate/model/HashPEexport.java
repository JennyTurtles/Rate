package org.sys.rate.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class HashPEexport {
    private HashMap<Integer,HashMap<Integer,HashMap<Integer,Participates>>> map;//评分项，名字和id
    private HashMap<Integer, String> Gmap;
    private HashMap<Integer, String> Emap;
    private HashMap<Integer, String> Smap;
    private HashMap<Integer, String> SNotByEmap;
    private HashMap<Integer,HashMap<Integer,Participates>> null_map;//非专家打分
    private HashMap<Integer,HashMap<String,DisplayItem>> Dmap; //展示项
}
