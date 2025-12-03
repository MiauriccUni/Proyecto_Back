package com.mahou.mahouback.logic.DTO;

public class RelationshipDTO {
    public int from;
    public int to;
    public int type;

    public RelationshipDTO(int from, int to, int type) {
        this.from = from;
        this.to = to;
        this.type = type;
    }
}
