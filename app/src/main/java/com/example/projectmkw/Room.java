package com.example.projectmkw;


import java.util.ArrayList;


public class Room implements Comparable{
    private int room_id, is_mkw, ol_status, room_start, race_start, n_race, n_members, n_players, conn_successful, mask_active, is_own_room, is_priv_room_host;
    private String type, room_name, game_id4, ol_status_x;
    private float conn_fail, conn_fail2;
    private String[] track = new String[2];
    private ArrayList<Member> members;

    public int getRoom_id() {
        return room_id;
    }

    public int getIs_mkw() {
        return is_mkw;
    }

    public int getOl_status() {
        return ol_status;
    }

    public int getRoom_start() {
        return room_start;
    }

    public int getRace_start() {
        return race_start;
    }

    public int getN_race() {
        return n_race;
    }

    public int getN_members() {
        return n_members;
    }

    public int getN_players() {
        return n_players;
    }

    public int getConn_successful() {
        return conn_successful;
    }

    public int getMask_active() {
        return mask_active;
    }

    public int getIs_own_room() {
        return is_own_room;
    }

    public int getIs_priv_room_host() {
        return is_priv_room_host;
    }

    public String getType() {
        return type;
    }

    public String getRoom_name() {
        return room_name;
    }

    public String getGame_id4() {
        return game_id4;
    }

    public String getOl_status_x() {
        return ol_status_x;
    }

    public float getConn_fail() {
        return conn_fail;
    }

    public float getConn_fail2() {
        return conn_fail2;
    }

    public String[] getTrack() {
        return track;
    }


    public ArrayList<Member> getMembers() {
        return members;
    }

    public boolean isPrivate(){ return "oP".equals(this.getOl_status_x().substring(0, 2)); }

    public Member getMember(long id){
        for (Member aMember : this.members){
            if(aMember.getPid() == id)
                return aMember;
        }
        return null;
    }

    public Member getHost(){
        for (Member aMember : this.members){
            if(aMember.getOl_status_x().charAt(9) == 'h')
                return aMember;
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\t\t\tYou are watching the room " + this.getRoom_name() + "(" + this.getRoom_id() + ")\n");
        sb.append("\t\t\tThis is a " + (this.isPrivate() ? "private" : "public") + " room\n");
        sb.append("\t\t\tLast race : " + this.getTrack()[1] + "\n\n");
        sb.append("\tSlot\t|\tFriendCode\t|\tVersus points\t|\tBattle points\t|\tMii name\n");
        sb.append("______________________________________________________________________________________________________________________\n");
        for (Member member : this.getMembers()) {
            sb.append("\t" + member.getSlot() + "\t|\t" + member.getFc() + "\t|\t\t" + (member.getEv() == -1 ? "----" : member.getEv()) + "\t|\t\t" + (member.getEb() == -1 ? "----" : member.getEb()) + "\t|\t");
            sb.append(member.getNames()[0]);
            if (member.getN_players() == 2) {
                sb.append("/" + member.getNames()[1]);
            }
            sb.append("\n");
        }
        sb.append("\n\n\n\n");
        return sb.toString();

    }


    @Override
    public int compareTo(Object o) {
        Room r = (Room) o;
        if(this.isPrivate() == ((Room) o).isPrivate()) return 0;
        else if(this.isPrivate()) return 1;
        else return -1;
    }
}