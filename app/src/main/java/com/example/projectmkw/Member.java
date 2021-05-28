package com.example.projectmkw;

import java.util.Arrays;

public class Member {
	private int slot, status, ol_status, hoststate, suspend, ev, eb, oh, is_owner, ctgp_outdated,
			ctgp_flags, n_races, n_players, flags_local, flags_connect, flags_nn_con, flags_nn_ack, flags_nn_suc, is_active;
	private String fc, ol_status_x, region, rk, vworld, patcher_info, ol_role;
	private long ls_status, last_nn, pid;
	private float conn_fail;
	
	public int getSlot() {
		return slot;
	}

	public long getPid() {
		return pid;
	}

	public int getStatus() {
		return status;
	}

	public long getLs_status() {
		return ls_status;
	}

	public int getOl_status() {
		return ol_status;
	}

	public int getHoststate() {
		return hoststate;
	}

	public int getSuspend() {
		return suspend;
	}

	public int getEv() {
		return ev;
	}

	public int getEb() {
		return eb;
	}

	public int getOh() {
		return oh;
	}

	public int getIs_owner() {
		return is_owner;
	}

	public int getCtgp_outdated() {
		return ctgp_outdated;
	}

	public int getCtgp_flags() {
		return ctgp_flags;
	}

	public int getN_races() {
		return n_races;
	}

	public int getN_players() {
		return n_players;
	}

	public int getFlags_local() {
		return flags_local;
	}

	public int getFlags_connect() {
		return flags_connect;
	}

	public int getFlags_nn_con() {
		return flags_nn_con;
	}

	public int getFlags_nn_ack() {
		return flags_nn_ack;
	}

	public int getFlags_nn_suc() {
		return flags_nn_suc;
	}

	public long getLast_nn() {
		return last_nn;
	}

	public int getIs_active() {
		return is_active;
	}

	public String getFc() {
		return fc;
	}

	public String getOl_status_x() {
		return ol_status_x;
	}

	public String getRegion() {
		return region;
	}

	public String getRk() {
		return rk;
	}

	public String getVworld() {
		return vworld;
	}

	public String getPatcher_info() {
		return patcher_info;
	}

	public String getOl_role() {
		return ol_role;
	}

	public float getConn_fail() {
		return conn_fail;
	}

	public String[] getNames() {
		return names;
	}

	public String[] getTrack() {
		return track;
	}

	public String[][] getName() {
		return name;
	}

	private String[] names = new String[2];
    private String[] track = new String[2];
	private String[][] name = new String[2][2];

	@Override
	public String toString() {
		return "Member [slot=" + slot + ", pid=" + pid + ", status=" + status + ", ls_status=" + ls_status
				+ ", ol_status=" + ol_status + ", hoststate=" + hoststate + ", suspend=" + suspend + ", ev=" + ev
				+ ", eb=" + eb + ", oh=" + oh + ", is_owner=" + is_owner + ", ctgp_outdated=" + ctgp_outdated
				+ ", ctgp_flags=" + ctgp_flags + ", n_races=" + n_races + ", n_players=" + n_players + ", flags_local="
				+ flags_local + ", flags_connect=" + flags_connect + ", flags_nn_con=" + flags_nn_con
				+ ", flags_nn_ack=" + flags_nn_ack + ", flags_nn_suc=" + flags_nn_suc + ", last_nn=" + last_nn
				+ ", conn_fail=" + conn_fail + ", is_active=" + is_active + ", fc=" + fc + ", ol_status_x="
				+ ol_status_x + ", region=" + region + ", rk=" + rk + ", vworld=" + vworld + ", patcher_info="
				+ patcher_info + ", ol_role=" + ol_role + ", names=" + Arrays.toString(names) + ", track="
				+ Arrays.toString(track) + ", name=" + Arrays.toString(name) + "]";
	}

}