public class Score {
	public int q1;
	public int q2;
	public int q3;
	public int q4;



	public Score(int q1, int q2,int q3,int q4) {
		this.q1 = q1;
		this.q2 = q2;
		this.q3 = q3;
		this.q4 = q4;
	}

	public String toString() {
		return String.format("%s,%s,%s,%s", this.q1, this.q2, this.q3, this.q4);
	}


}