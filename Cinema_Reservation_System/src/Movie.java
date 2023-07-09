public class Movie {
	
    private String title;
    private String time;
    private String date;
    private int availableSeats;
    

    public Movie(String title, String time, String date, int availableSeats) {
        this.title = title;
        this.time = time;
        this.date = date;
        this.availableSeats = availableSeats;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void reserveSeat() {
        availableSeats--;
    }
}

