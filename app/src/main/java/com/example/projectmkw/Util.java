package com.example.projectmkw;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class Util {

	public static String getJSON(long id, RoomLookingType type) {
		try {
			URL url = new URL("https://wiimmfi.de/stats/mkw/room/" + (RoomLookingType.member.equals(type) ? "p" : "r")
					+ id + "?m=json");
			String json = new String();
			Scanner s = new Scanner(url.openStream());
			StringBuilder sb = new StringBuilder();
			while (s.hasNext()) {
				sb.append(s.nextLine());
			}
			json = sb.toString();
			s.close();
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String getJSON(int id) {
		return getJSON(id, RoomLookingType.room);
	}

	public static Room extractRoomFromJSON(String json) {
		Gson gson = new Gson();
		Room[] roomWhy = gson.fromJson(json, Room[].class);
		if ("room".equals(roomWhy[1].getType())) {
			Room room = roomWhy[1];
			return room;
		}
		return null;
	}

	public static List<Room> getAllRooms() {
		try {
			URL url = new URL("https://wiimmfi.de/stats/mkw/?m=json");
			String json = new String();
			Scanner s;
			s = new Scanner(url.openStream());

			StringBuilder sb = new StringBuilder();
			while (s.hasNext()) {
				sb.append(s.nextLine());
			}
			json = sb.toString();
			s.close();

			Gson gson = new Gson();
			Room[] roomWhy = gson.fromJson(json, Room[].class);
			List<Room> rooms = new ArrayList<>();
			for (int i = 0; i < roomWhy.length; i++) {
				if ("room".equals(roomWhy[i].getType()))
					rooms.add(roomWhy[i]);

			}
			return rooms;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}


	public static List<Favourite> getFavourites(Context context){
		String json = readFileFromInternalStorage(context, "fav.json");
		if (json != null && !json.equals("{}")) {
			Gson g = new Gson();
			Favourite[] favs = g.fromJson(json, Favourite[].class);
			List<Favourite> res;
			if(favs != null) {
				res = new ArrayList<>(Arrays.asList(favs));
			}
			else {
				res = new ArrayList<>();
			}
			return res;
		} else {
			return new ArrayList<>();
		}
	}

	public static List<Favourite> addFavourite(List <Favourite> favourites, Member favourite){
		boolean alreadyExists = false;
		for(Favourite f : favourites){
			if(favourite.getPid() == f.getPid()) alreadyExists = true;
		}
		if(!alreadyExists) favourites.add(new Favourite(favourite));
		return favourites;
	}

	public static void updateFavourite(Context context, List <Favourite> favourites){
		Gson g = new Gson();
		String json = g.toJson(favourites);
		writeFileOnInternalStorage(context, "fav.json", json);
	}

	public static void deleteFavourite(List<Favourite> favourites, long favPid){
		for (Favourite aFavourite : favourites){
			if (aFavourite.getPid() == favPid){
				favourites.remove(aFavourite);
				return;
			}
		}
	}

	public static void writeFileOnInternalStorage(Context context, String sFileName, String sBody){
		create(context, sFileName, sBody) ;
	}

	public static String readFileFromInternalStorage(Context context, String sFileName){
		if (!isFilePresent(context, sFileName)){
			create(context, sFileName);
		}
		try {
			FileInputStream fis = context.openFileInput(sFileName);
			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader bufferedReader = new BufferedReader(isr);
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (FileNotFoundException fileNotFound) {
			return null;
		} catch (IOException ioException) {
			return null;
		}
	}

	public static int averageEv(Room room){
		int ret = 0;
		for(Member m : room.getMembers()){
			ret += m.getEv();
		}
		ret /= room.getN_members();

		return ret;
	}

	private static boolean create(Context context, String fileName, String jsonString){
		try {
			FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);
			if (jsonString != null) {
				fos.write(jsonString.getBytes());
			}
			fos.close();
			return true;
		} catch (FileNotFoundException fileNotFound) {
			return false;
		} catch (IOException ioException) {
			return false;
		}

	}

	private static boolean create(Context context, String fileName) {
		return create(context, fileName, "{}");
	}

		public static boolean isFilePresent(Context context, String fileName) {
		String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
		File file = new File(path);
		return file.exists();
	}



}
