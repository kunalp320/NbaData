import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class get {

	public static List<Game> getIds() {
		String content;
		try {
			content = new Scanner(new File("filename.txt")).useDelimiter("\\Z")
					.next();
			// System.out.println(content);

			JSONObject inital = new JSONObject(content);

			JSONObject games = (JSONObject) ((JSONObject) (JSONObject) inital
					.get("league")).get("season-schedule");
			JSONArray game = (JSONArray) ((JSONObject) games
					.getJSONObject("games")).getJSONArray("game");

			List<Game> myGames = new ArrayList<Game>(game.length());

			for (int i = 0; i < game.length(); i++) {
				JSONObject temp = game.getJSONObject(i);
				String away = temp.getJSONObject("away").getString("name");
				String home = temp.getJSONObject("home").getString("name");
				String id = temp.getString("id");
				myGames.add(new Game(home, away, id));
			}

			return myGames;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static void getData(List<Game> games) throws ClientProtocolException, IOException {
		String first = "http://api.sportsdatallc.org/nba-t3/games/";
		String second = "/boxscore.xml?api_key=3qkgnjuj8sg7qxte7nvqumq3";
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i<games.size(); i++) {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(first + games.get(i).getId() + second);
			HttpResponse response = client.execute(request);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			String line = "";
		
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}

			
			
		
		}
		JSONObject xmlJSONObj;
		try {
			xmlJSONObj = XML.toJSONObject(sb.toString());
			
			try (PrintStream out = new PrintStream(new FileOutputStream("data.txt"))) {
			    out.print(xmlJSONObj.toString(4));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void getInfo(List<Game> scheduleGames) {
		String content;
		try {
			content = new Scanner(new File("data.txt")).useDelimiter("\\Z")
					.next();

			JSONObject inital = new JSONObject(content);

			JSONArray games = ((JSONArray) inital
					.get("game"));
	
			FileWriter writer = new FileWriter("test.csv");

			for (int i = 0; i < games.length(); i++) {
				
				JSONArray temp =(JSONArray) games.getJSONObject(i).get("team");
				if((int)temp.getJSONObject(0).get("points")>0) {
				
					for(int k = 0; k<2; k++) {
						writer.append( temp.getJSONObject(k).get("name").toString());
						writer.append(",");
						JSONObject quarter = (JSONObject) temp.getJSONObject(k).get("scoring");
						for(int j = 0; j<4; j++) {
							
							//First team points
							writer.append(""+ quarter.getJSONArray("quarter").getJSONObject(j).get("points"));
							writer.append(",");
							writer.append(""+ quarter.getJSONArray("quarter").getJSONObject(j).get("points"));
							writer.append(",");
							writer.append(""+ quarter.getJSONArray("quarter").getJSONObject(j).get("points"));
							writer.append(",");
							writer.append(""+ quarter.getJSONArray("quarter").getJSONObject(j).get("points"));
							writer.append("\n");

						}
					}

				writer.flush();
				}		
			} 
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
			
		}

	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		List<Game> games = getIds();
		getInfo(games);

	}

}
