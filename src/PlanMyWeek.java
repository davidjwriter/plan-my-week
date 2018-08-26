import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Set;

public class PlanMyWeek {

  public static ArrayList<String> hobbies =  new ArrayList<>();// = {"Theological Quandaries", "Wood Working", "Outdoors", "Coffee", "Video Games", "Cooking", "Home video"};
  public static ArrayList<String> jobs =  new ArrayList<>();// = {"Stable", "Wood Working Website", "Shopping List"};
  public static Day[] week = new Day[7];
  public static String HOBBIES = "Hobbies";
  public static String JOBS = "Jobs";
  public static HashMap<String, Integer> days = new HashMap<>();
  public static HashMap<Integer, String> allActivities = new HashMap<>();


  public static void updateWeek(Day[] week) throws FileNotFoundException {
    Scanner scandalous = new Scanner(new File("mandatory.csv"));
    scandalous.useDelimiter("\n");
    if (scandalous.hasNext()) {
      scandalous.next(); //first line needs to be ommitted
    }
    String[] nextThing;
    while(scandalous.hasNext()) {
      nextThing = scandalous.next().split(",");
      for (String s : nextThing[1].split(" ")) {
        week[days.get(s)].addEvent(Integer.parseInt(nextThing[2].trim()), Integer.parseInt(nextThing[3].trim()), nextThing[0]);
      }
    }
  }

  public static void readCsvHobbiesJobs() throws FileNotFoundException {
    Scanner scandalous = new Scanner(new File("list.csv"));
    scandalous.useDelimiter("\n");
    boolean flag = false;
    while (scandalous.hasNext()) {
      Scanner scan = new Scanner(scandalous.next());
      scan.useDelimiter(",");
      scan.next();
      while (scan.hasNext()) {
        if (!flag) {
          hobbies.add(scan.next());
        } else {
          jobs.add(scan.next());
        }
      }
      flag = true;
    }
  }

  public static void printWeek() {
      for (int i = 0; i < week.length; i++) {
        System.out.println("Day " + week[i].getName());
        for (int j = 0; j < 24; j++) {
          System.out.print((j%12)+1);
          System.out.print(":00 ");
          System.out.println(week[i].getDay().get(j));
        }
      }
  }

  public static int findFirstOpenHour(Day day, int earliest) {
    HashMap<Integer, String> hours = day.getDay();
    for (int i = 23; i >= earliest; i--) {
      if (hours.get(i) != "") {
        return i + 1;
      }
    }
    return 0;
  }

  public static String getaThing() {
    Random rand = new Random();
    //First pick either a hobby or a job
    if (rand.nextInt() % 2 == 0) {
      //Pick a random hobby
      int i = rand.nextInt();
      if (i < 0) {
        i *= -1;
      }
      return hobbies.get(i % hobbies.size());
    }
    else {
      int j = rand.nextInt();
      if (j < 0) {
        j *= -1;
      }
      //Pick a random job
      return jobs.get(j % jobs.size());
    }
  }



  public static void main(String[] args) throws FileNotFoundException {
    /*
      * Hobbies: Bible blog, wood working, outdoors, coffee, video games, cooking, martial arts, making a home video
      * Side Jobs: Stable, wood working, shopping list
      * Cleaning: bathroom, living room, kitchen, vacuum
      * Mandatory: work, study Bible, workout
    */
    days.put("Monday",0);
    days.put("Tuesday",1);
    days.put("Wednesday",2);
    days.put("Thursday",3);
    days.put("Friday",4);
    days.put("Saturday",5);
    days.put("Sunday",6);
    readCsvHobbiesJobs();
    week[0] = new Day("Monday");
    week[1] = new Day("Tuesday");
    week[2] = new Day("Wednesday");
    week[3] = new Day("Thursday");
    week[4] = new Day("Friday");
    week[5] = new Day("Saturday");
    week[6] = new Day("Sunday");
    updateWeek(week);



    /*
      ************** Now add all the random events ************
    */
    // First we need some user preference data
    Scanner user = new Scanner(System.in);
    System.out.println("================== Welcome to your new week =====================");
    System.out.println("We are ready to prep your activities for the week!");
    System.out.println("We just need some info first...");
    System.out.println("What is the earliest you would want an activity scheduled?");
    int earliest = user.nextInt();
    System.out.println("Great! Thanks!");
    System.out.println("Next, how many activities do you want scheduled per day?");
    int numberOfActs = user.nextInt();
    System.out.println("Great! Thanks!");
    System.out.println("Next, here is a list of all your loaded activites. Are there any you want us to schedule?");
    int counter = 0;
    for (String hobby : hobbies) {
      System.out.print("[");
      System.out.print(counter);
      System.out.print("] ");
      System.out.println(hobby);
      allActivities.put(counter, hobby);
      counter++;
    }
    for (String job : jobs) {
      System.out.print("[");
      System.out.print(counter);
      System.out.print("] ");
      System.out.println(job);
      allActivities.put(counter, job);
      counter++;
    }
    System.out.println("Please enter the number for the activity followed by a comma and the number of hours to schedule it");
    System.out.println("Enter q to stop adding");
    String next = user.next();
    HashMap<Integer, Integer> actsToSchedule = new HashMap<>();
    while(!next.equals("q")) {
      String[] act = next.split(",");
      actsToSchedule.put(Integer.parseInt(act[0].trim()), Integer.parseInt(act[1].trim()));
      System.out.println("Enter q to stop adding");
      next = user.next();
    }
    System.out.println("Great! Thanks for your info. We will now schedule your week");

    int[] openHours = new int[7];
    for (int j = 0; j < week.length; j++) {
      openHours[j] = findFirstOpenHour(week[j], earliest);
    }

    while (numberOfActs > 0) {
      for (int i = 0; i < week.length; i++) {
        int start = openHours[i];
        // Randomly choose to schedule random thing or schedule mandatory
        Random rand = new Random();
        if (((rand.nextInt() % 2) == 0) && actsToSchedule.size() != 0) {
          Set<Integer> allActs = actsToSchedule.keySet();
          int randomAct = rand.nextInt() % allActs.size();
          String activity = allActivities.get(randomAct);
          actsToSchedule.put(randomAct, actsToSchedule.get(randomAct) - 1);
          if (actsToSchedule.get(randomAct) == 0) {
            actsToSchedule.remove(randomAct);
          }
          week[i].addEvent(start, start+1, activity);
        } else {
          week[i].addEvent(start, start+1, getaThing());
        }
      }
      for (int j = 0; j < week.length; j++) {
        openHours[j] = findFirstOpenHour(week[j], earliest);
      }
      numberOfActs--;
    }


    /*
      Now we create the csv of the schedule
    */
    PrintWriter pw = new PrintWriter(new File("schedule.csv"));
    StringBuilder sb = new StringBuilder();
    sb.append("Day,");
    sb.append("Monday,");
    sb.append("Tuesday,");
    sb.append("Wednesday,");
    sb.append("Thursday,");
    sb.append("Friday,");
    sb.append("Saturday,");
    sb.append("Sunday,");
    sb.append("\n");
    for (int j = 0; j < 24; j++) {
      // First print the time
      if ((j%12) == 0) {
        sb.append("12");
      } else {
        sb.append((j%12));
      }
      sb.append(":00");
      if (j > 11) {
        sb.append("pm,");
      } else {
        sb.append("am,");
      }

      // Now go through all the weeks and print the event
      for (int i = 0; i < week.length; i++) {
        sb.append(week[i].getDay().get(j));
        sb.append(",");
      }
      sb.append("\n");
    }
    pw.write(sb.toString());
    pw.close();
    printWeek();
  }

}
