package org.sys.rate.model;

import lombok.Data;

import java.util.HashMap;

@Data
public class HashFianlScore {
    private HashMap<Integer,Participates> map;//<participantsID,participants>
    private HashMap<Integer, String> Tmap; // totalItem的name
}
