	public class Game{
		public String homeTeam;
		public String awayTeam; 
		public String id; 
		public Game(String h, String a, String id){
			this.homeTeam = h; 
			this.awayTeam = a; 
			this.id = id; 
		}
		public String toString(){
			return String.format("%s,%s,%s", this.homeTeam, this.awayTeam, this.id); 
		}	
		public String getId() {
			return id;
		}
	}