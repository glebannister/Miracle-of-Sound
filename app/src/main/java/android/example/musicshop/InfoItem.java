package android.example.musicshop;

public class InfoItem {

    private String title;
    private String information;

    public InfoItem(String title, String information) {
        this.title = title;
        this.information = information;
    }

    public String getTitle() {
        return title;
    }
    public String getInformation() {
        return information;
    }
}
