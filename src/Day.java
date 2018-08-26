import java.util.HashMap;

public class Day {
  private HashMap<Integer,String> hours;
  private String name;

  public Day(String name) {
    this.hours = new HashMap<>();
    for (int i = 0; i < 24; i++) {
      hours.put(i, "");
    }
    this.name = name;
  }

  public void addEvent(int begin, int end, String event) {
    for (int i = begin; i < end; i++) {
      hours.put(i, event);
    }
  }

  public HashMap<Integer, String> getDay() {
    return hours;
  }

  public String getName() {
    return name;
  }
}
